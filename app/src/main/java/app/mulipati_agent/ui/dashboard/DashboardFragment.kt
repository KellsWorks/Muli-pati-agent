package app.mulipati_agent.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.mulipati.data.LocationResponse
import app.mulipati.util.Resource
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import app.mulipati_agent.databinding.FragmentDashboardBinding
import app.mulipati_agent.epoxy.TripsEpoxyController
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import app.mulipati_agent.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var dashboardBinding: FragmentDashboardBinding by autoCleared()

    private lateinit var controller: TripsEpoxyController

    private val tripsViewModel: TripsViewModel by viewModels()

    private lateinit var tripsList: ArrayList<Trips>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardBinding.lifecycleOwner = this

        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dashboardBinding.addTrip.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addTripFragment)
        }

        bindLocation()
    }

    private fun bindLocation() {

        val locationPrefs = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.districts,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        dashboardBinding.districtSelect.adapter = adapter

        val location = adapter.getPosition(
            locationPrefs?.getString("location", "")?.toUpperCase(
                Locale.ROOT
            )
        )


        dashboardBinding.districtSelect.setSelection(location)
        dashboardBinding.districtSelect.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    updateLocation(dashboardBinding.districtSelect.selectedItem as String)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }


    private fun updateLocation(location: String){

        val api = ApiClient.client!!.create(Routes::class.java)

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()

        val upload: Call<LocationResponse?>? = api.photoLocation(user?.getInt("profile_id", 0), location)
        upload?.enqueue(object : Callback<LocationResponse?> {
            override fun onFailure(call: Call<LocationResponse?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error updating location",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onResponse(call: Call<LocationResponse?>, response: retrofit2.Response<LocationResponse?>) {
                when (response.code()){
                    200 -> {
                        userPreferences?.putString("location", location)
                        userPreferences?.apply()
                    }
                }
            }

        })
    }

    private fun setUpObservers(){
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        tripsViewModel.trips.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it.status){
                Resource.Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()){

                        tripsList = ArrayList()

                        for (trip in it.data){

                            if (trip.user_id == getId){
                                tripsList.add(Trips(
                                        trip.car_photo, trip.car_type, trip.created_at, trip.destination, trip.end_time, trip.id,
                                        trip.location, trip.number_of_passengers, trip.passenger_fare, trip.pick_up_place, trip.start,
                                        trip.start_time, trip.status, trip.updated_at, trip.user_id
                                ))
                            }
                        }

                        setUpRecycler(tripsList)
                    }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    Timber.e("Error")
                }
            }
        })
    }

    private fun setUpRecycler(data: List<Trips>){
        controller = TripsEpoxyController()
        controller.setData(false, data)

        dashboardBinding.tripsRecycler
            .setController(controller)
    }
}