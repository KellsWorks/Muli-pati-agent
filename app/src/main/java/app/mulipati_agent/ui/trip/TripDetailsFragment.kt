package app.mulipati_agent.ui.trip

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentTripDetailsBinding
import app.mulipati_agent.shared_preferences.SharedPreferences
import app.mulipati_agent.util.GoogleMapDTO
import app.mulipati_agent.util.convertDate
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


open class TripDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var tripDetailsBinding: FragmentTripDetailsBinding

    private lateinit var mGoogleMap: GoogleMap

    private lateinit var mPlacesClient: PlacesClient

    private lateinit var locationStart: LatLng

    private lateinit var locationDestination: LatLng


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tripDetailsBinding =         FragmentTripDetailsBinding
            .inflate(inflater, container, false)
        tripDetailsBinding
            .lifecycleOwner = this
        return tripDetailsBinding.root
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (checkPermissions()){
            MapsInitializer.initialize(requireContext())
            if (p0 != null) {
                mGoogleMap = p0
            }
            p0!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            p0.isMyLocationEnabled = true
            p0.isTrafficEnabled = true
            p0.isBuildingsEnabled = true
            p0.isIndoorEnabled = true
            p0.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                requireActivity(), R.raw.map_style))

            val location = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("location", "")

            locationStart = LatLng(-15.764963723689691, 35.011536779590564)
            locationDestination = LatLng(-15.764963723689691, 35.011536779590564)

            when(location){
                "Blantyre"->{
                    locationStart = LatLng(-15.764963723689691, 35.011536779590564)
                    locationDestination = LatLng(-15.764963723689691, 35.011536779590564)
                }
                "Lilongwe"->{
                    locationStart = LatLng(-13.96518639724504, 33.80457907535407)
                    locationDestination = LatLng(-13.96518639724504, 33.80457907535407)
                }
                "Zomba"->{
                    locationStart = LatLng(-15.375668824861341, 35.335528191089345)
                    locationDestination = LatLng(-15.375668824861341, 35.335528191089345)
                }
                "Mzuzu"->{
                    locationStart = LatLng(-11.437706025609216, 34.00710504561069)
                    locationDestination = LatLng(-11.437706025609216, 34.00710504561069)
                }
            }


            mGoogleMap.addMarker(MarkerOptions().position(locationStart).title("Start").snippet("Trip will start here"))

            mGoogleMap.addMarker(MarkerOptions().position(locationDestination).title("Destination").snippet ("Trip will end here"))

            val url = getDirectionURL(locationStart, locationDestination)
            GetDirection(url).execute()

            val camera = CameraPosition.builder().target(locationStart).zoom(
                8F
            ).bearing(0F).tilt(45F).build()
            p0.moveCamera(CameraUpdateFactory.newCameraPosition(camera))


        }else{
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.e("location checked")
            }else{
                requestPermissions()
            }

        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripDetailsBinding.previewMap.onCreate(null)
        tripDetailsBinding.previewMap.onResume()
        tripDetailsBinding.previewMap.getMapAsync(this)

        Places.initialize(requireActivity(), resources.getString(R.string.map_key))
        mPlacesClient = Places.createClient(requireActivity())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tripDetailsBinding.showAPreview.setOnClickListener {
            tripDetailsBinding.showMap.visibility = View.VISIBLE
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            val startLatLng = getLocationFromAddress(requireContext(), tripDetailsBinding.tripStart.text.toString()+"Malawi")
            val destLatLng = getLocationFromAddress(requireContext(), tripDetailsBinding.tripDestination.text.toString()+"Malawi")

            val url = getDirectionURL(startLatLng!!, destLatLng!!)
            GetDirection(url).execute()

            mGoogleMap.addMarker(MarkerOptions().position(startLatLng).title(tripDetailsBinding.tripStart.text.toString()).snippet("Trip will start here"))

            mGoogleMap.addMarker(MarkerOptions().position(destLatLng).title(tripDetailsBinding.tripDestination.text.toString()).snippet ("Trip will end here"))


            val cameraUpdate: CameraUpdate =
                CameraUpdateFactory.newLatLngZoom(startLatLng, 8F)
            mGoogleMap.animateCamera(cameraUpdate)

        }
        tripDetailsBinding.hidePreview.setOnClickListener {
            tripDetailsBinding.showMap.visibility = View.GONE
            requireActivity().overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
        tripDetailsBinding.setStartTime.setOnClickListener {
            dateTimePicker()
        }


    }

    open fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    @SuppressLint("SetTextI18n")
    private fun dateTimePicker(){

        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                if (year.toString().isNotEmpty() && monthOfYear.toString().isNotEmpty() && dayOfMonth.toString().isNotEmpty()){
                    val mHour = c[Calendar.HOUR_OF_DAY]
                    val  mMinute = c[Calendar.MINUTE]
                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                            val getMonth = convertDate(monthOfYear)

                            val timeExtension: String = if (hourOfDay > 12 ){
                                "PM"
                            }else{
                                "AM"
                            }

                            tripDetailsBinding.setStartTime.text = "$dayOfMonth $getMonth, $year $hourOfDay:$minute $timeExtension"

                            SharedPreferences(requireContext()).addTripPrefs("start_time", "$dayOfMonth-$monthOfYear-$year $hourOfDay:$minute")
                            SharedPreferences(requireContext()).addTripPrefs("start", tripDetailsBinding.tripStart.text.toString())
                            SharedPreferences(requireContext()).addTripPrefs("destination", tripDetailsBinding.tripDestination.text.toString())
                            SharedPreferences(requireContext()).addTripPrefs("pick_up_place", tripDetailsBinding.tripPickUp.text.toString())



                        }, mHour, mMinute, false
                    )
                    timePickerDialog.show()
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.show()


    }

    private fun getDirectionURL(origin:LatLng, dest:LatLng) : String{
        val apiKey = "AIzaSyAT8TT5ONNjFdWWp3enIDIZrVm5bKcm_G4"
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving&key=$apiKey"
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }



    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void,Void,List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val result =  ArrayList<List<LatLng>>()


            if (isNetworkAvailable(requireContext())){
                val response = client.newCall(request).execute()
                try{
                    val data = response.body!!.string()

                    val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)

                    val path =  ArrayList<LatLng>()

                    for (i in 0 until respObj.routes[0].legs[0].steps.size){
                        path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                    }
                    result.add(path)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            else{

            }


            return result
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(requireActivity().getColor(R.color.red))
                lineoption.geodesic(true)
            }
            mGoogleMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )
    }




}