@file:Suppress("DEPRECATION")

package app.mulipati_agent.ui.trip

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import app.mulipati_agent.R
import app.mulipati_agent.adapters.PagerAdapter
import app.mulipati_agent.databinding.FragmentAddTripBinding
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import app.mulipati_agent.network.responses.Basic
import app.mulipati_agent.shared_preferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class AddTripFragment : Fragment() {

    private lateinit var addTripBinding: FragmentAddTripBinding

    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        addTripBinding = FragmentAddTripBinding.inflate(inflater, container, false)
        addTripBinding.lifecycleOwner = this

        return addTripBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addTripBinding.addTripBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpPagers()
    }

    private fun setUpPagers(){

        val adapter = PagerAdapter(requireActivity().supportFragmentManager)

        adapter.addFragment(VerificationFragment() , " One ")
        adapter.addFragment(TripDetailsFragment() , " two ")
        adapter.addFragment(PricingFragment() , " three ")
        adapter.addFragment(RideFragment() , " four ")

        addTripBinding.addTripPager.adapter = adapter

        addTripBinding.addTripsDots.setViewPager(addTripBinding.addTripPager)

        addTripBinding.addTripPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when(position){
                    0 ->{

                        addTripBinding.nextPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 1
                        }

                    }
                    1-> {
                        addTripBinding.nextPage.text = getString(R.string.next)
                        addTripBinding.nextPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 2
                        }
                        addTripBinding.previousPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 0
                        }
                    }
                    2-> {
                        addTripBinding.nextPage.text = getString(R.string.next)
                        addTripBinding.nextPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 3
                        }
                        addTripBinding.previousPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 1
                        }
                    }
                    3->{

                        addTripBinding.nextPage.text = getString(R.string.finish)
                        addTripBinding.nextPage.setOnClickListener {
                            finishAdd()
                        }
                        addTripBinding.previousPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 2
                        }

                    }else->{
                    addTripBinding.previousPage.visibility = View.VISIBLE
                    addTripBinding.nextPage.visibility = View.VISIBLE
                }
                }
            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun finishAdd(){

        dialog = Dialog(requireContext())
        val view = dialog.layoutInflater.inflate(R.layout.dialog_trip_summary, null)

        val price = view.findViewById<TextView>(R.id.summaryPrice)
        val route = view.findViewById<TextView>(R.id.tripRoute)
        val startTime = view.findViewById<TextView>(R.id.tripDate)
        val carType = view.findViewById<TextView>(R.id.addTripCar)

        val idGet = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        val locationGet = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("location", "")
        val priceGet = SharedPreferences(requireContext()).getTripsPrefs("price", "")
        val routeGet = SharedPreferences(requireContext()).getTripsPrefs("start", "") + " to " + SharedPreferences(requireContext()).getTripsPrefs("destination", "")
        val start = SharedPreferences(requireContext()).getTripsPrefs("start", "")
        val destination = SharedPreferences(requireContext()).getTripsPrefs("destination", "")
        val startTimeGet = SharedPreferences(requireContext()).getTripsPrefs("start_time", "")
        val carTypeGet = SharedPreferences(requireContext()).getTripsPrefs("car_type", "")
        val passengersGet = SharedPreferences(requireContext()).getTripsPrefs("number_of_passengers", "")
        val carPhoto = SharedPreferences(requireContext()).getTripsPrefs("car_photo", "")
        val pickUpPlace = SharedPreferences(requireContext()).getTripsPrefs("pick_up_place", "")


        price.text = "MK$priceGet"
        route.text = routeGet
        startTime.text = startTimeGet
        carType.text = carTypeGet

        dialog.setContentView(view)
        dialog.show()

        val finish = view.findViewById<Button>(R.id.addTripFinish)
        finish.setOnClickListener {
            addNewTrip(
                idGet!!, start!!, destination!!, startTimeGet!!, startTimeGet,  pickUpPlace!!, locationGet.toString(), passengersGet!!, priceGet!!, carTypeGet!!, carPhoto!!
            )
        }
    }

    private fun addNewTrip(userId: Int, start: String, destination: String, start_time: String, end_time: String,
                           pick_up_place: String, location: String, number_of_passengers: String,
                           passenger_fare: String, car_type: String, car_photo: String){
        dialog.dismiss()

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val addTrip: Call<Basic?>? = apiClient.addNewTrip(userId, start, destination, start_time, end_time, pick_up_place, location, number_of_passengers, passenger_fare, car_type, car_photo)

        addTrip?.enqueue(object : Callback<Basic?> {
            override fun onFailure(call: Call<Basic?>, t: Throwable) {
                progressDialog.dismiss()
                Timber.e(t)
                Toast.makeText(
                    requireContext(),
                    "A network error occurred",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onResponse(call: Call<Basic?>, response: Response<Basic?>) {
                progressDialog.dismiss()
                when(response.code()){
                    200 ->{

                        val successDialog = Dialog(requireContext())
                        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.success)
                        mediaPlayer.start()
                        successDialog.setContentView(R.layout.dialog_success)
                        successDialog.show()
                        Handler().postDelayed({
                            successDialog.dismiss()
                            findNavController().navigate(R.id.action_addTripFragment_to_dashboardFragment)
                        }, 3000)

                    }else ->{
                    Timber.e(response.errorBody()?.string())
                }
                }
            }


        })
    }
}