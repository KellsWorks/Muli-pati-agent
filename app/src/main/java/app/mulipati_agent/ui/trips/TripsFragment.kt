package app.mulipati_agent.ui.trips

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.mulipati.util.Resource
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import app.mulipati_agent.databinding.FragmentTripsBinding
import app.mulipati_agent.epoxy.TripsEpoxyController
import app.mulipati_agent.ui.dashboard.TripsViewModel
import app.mulipati_agent.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripsFragment : Fragment() {

    private var tripsBinding: FragmentTripsBinding by autoCleared()

    private lateinit var controller: TripsEpoxyController

    private val tripsViewModel: TripsViewModel by viewModels()

    private lateinit var tripsList: ArrayList<Trips>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tripsBinding = FragmentTripsBinding.inflate(inflater, container, false)
        tripsBinding.lifecycleOwner = this

        return tripsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripsBinding.tripsBack.setOnClickListener {
            findNavController().navigate(R.id.action_tripsFragment_to_dashboardFragment)
        }

        tripsBinding.refreshTrips.setOnRefreshListener {
            setUpObservers()
        }
        setUpObservers()
    }

    private fun setUpObservers() {
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        tripsViewModel.trips.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    tripsBinding.refreshTrips.isRefreshing = false
                    if (it.data!!.isNotEmpty()) {

                        tripsList = ArrayList()

                        for (trip in it.data) {

                            if (trip.user_id == getId) {
                                tripsList.add(
                                    Trips(
                                        trip.car_photo,
                                        trip.car_type,
                                        trip.created_at,
                                        trip.destination,
                                        trip.end_time,
                                        trip.id,
                                        trip.location,
                                        trip.number_of_passengers,
                                        trip.passenger_fare,
                                        trip.pick_up_place,
                                        trip.start,
                                        trip.start_time,
                                        trip.status,
                                        trip.updated_at,
                                        trip.user_id
                                    )
                                )
                            }
                        }

                        setUpRecycler(tripsList)
                    }
                }
                Resource.Status.LOADING -> {
                    tripsBinding.refreshTrips.isRefreshing = true
                }
                Resource.Status.ERROR -> {
                    tripsBinding.refreshTrips.isRefreshing = false
                }
            }
        })

    }

    private fun setUpRecycler(data: List<Trips>){

        if (data.isEmpty()){
            tripsBinding.tripsRecycler.visibility = View.GONE
            tripsBinding.noTripsError.visibility = View.VISIBLE
        }else{
            tripsBinding.tripsRecycler.visibility = View.VISIBLE
            tripsBinding.noTripsError.visibility = View.GONE
        }

        controller = TripsEpoxyController()
        controller.setData(false, data)

        tripsBinding.tripsRecycler
            .setController(controller)
    }

    }