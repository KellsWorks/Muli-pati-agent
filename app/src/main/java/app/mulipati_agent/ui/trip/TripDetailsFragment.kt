package app.mulipati_agent.ui.trip

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentTripDetailsBinding
import app.mulipati_agent.util.GoogleMapDTO
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber


class TripDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var tripDetailsBinding: FragmentTripDetailsBinding

    private lateinit var mGoogleMap: GoogleMap

    private lateinit var mPlacesClient: PlacesClient

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




            val start = LatLng(-15.811457319276274, 35.055391163368206)
            mGoogleMap.addMarker(MarkerOptions().position(start).title("Start").snippet("Trip will start here"))

            val destination = LatLng(-14.00050023135735, 33.82105838913558)
            mGoogleMap.addMarker(MarkerOptions().position(destination).title("Destination").snippet ("Trip will end here"))

            val url = getDirectionURL(start, destination)
            GetDirection(url).execute()

            val camera = CameraPosition.builder().target(start).zoom(
                6F
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
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        tripDetailsBinding.hidePreview.setOnClickListener {
            tripDetailsBinding.showMap.visibility = View.GONE
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun getDirectionURL(origin:LatLng, dest:LatLng) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving"
    }

    private inner class GetDirection(val url : String) : AsyncTask<Void,Void,List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            Timber.e(" data : $data")
            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)

                val path =  ArrayList<LatLng>()

                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.CYAN)
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