package app.mulipati_agent.ui.bookings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.mulipati.util.Resource
import app.mulipati_agent.data.Bookings
import app.mulipati_agent.databinding.FragmentBookingsBinding
import app.mulipati_agent.epoxy.bookings.BookingsEpoxyController
import app.mulipati_agent.ui.dashboard.BookingsViewModel
import app.mulipati_agent.ui.dashboard.TripsViewModel
import app.mulipati_agent.ui.dashboard.UsersViewModel
import app.mulipati_agent.util.autoCleared
import app.mulipati_agent.util.getDay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment : Fragment() {

    private var bookingsBinding: FragmentBookingsBinding by autoCleared()

    private val bookingsViewModel: BookingsViewModel by viewModels()

    private val tripsViewModel: TripsViewModel by viewModels()

    private val usersViewModel: UsersViewModel by viewModels()

    private lateinit var bookingsList: ArrayList<Bookings>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bookingsBinding = FragmentBookingsBinding.inflate(inflater, container, false)
        bookingsBinding.lifecycleOwner = this

        return bookingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        bookingsBinding.bookingsBack.setOnClickListener {
            findNavController().navigate(app.mulipati_agent.R.id.action_bookingsFragment_to_dashboardFragment
            )
        }

        bookingsBinding.refreshBookings.setOnRefreshListener {
            setUpObservers()
        }
    }

    private fun setUpObservers(){

        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        val location = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("location", "")

        bookingsViewModel.bookings.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it.status){
                Resource.Status.SUCCESS -> {
                    bookingsBinding.refreshBookings.isRefreshing = false

                    val ids = getId?.let { it1 -> getTrips(it1) }

                    if (ids != null) {
                        for (id in ids){
                            bookingsList = ArrayList()

                                for (trip in it.data!!){

                                    if (trip.trip_id == id){
                                        bookingsList.add(Bookings(trip.id, getUserName(trip.id), getDay(trip.created_at), getDay(trip.created_at), trip.user_id.toString(), location.toString()))
                                    }


                                }

                                setUpBookings(bookingsList)
                        }
                    }
                }
                Resource.Status.LOADING -> {
                    bookingsBinding.refreshBookings.isRefreshing = true
                }
                Resource.Status.ERROR -> {
                    bookingsBinding.refreshBookings.isRefreshing = false
                }
            }
        })
    }

    private fun setUpBookings(data: List<Bookings>){

        if (data.isEmpty()){
            bookingsBinding.bookingRecycler.visibility = View.GONE
            bookingsBinding.noBookingsError.visibility = View.VISIBLE
        }else{
            bookingsBinding.bookingRecycler.visibility = View.VISIBLE
           bookingsBinding.noBookingsError.visibility = View.GONE
        }

        val bookingsEpoxyController = BookingsEpoxyController()
        bookingsEpoxyController.setData(false, data)

        bookingsBinding.bookingRecycler.setController(bookingsEpoxyController)
        bookingsBinding.bookingRecycler.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun getTrips(tripId: Int) : List<Int>{

        val arrayList = ArrayList<Int>()

        tripsViewModel.trips.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it.status){
                Resource.Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()){

                        for (trip in it.data){

                            if (trip.user_id == tripId){
                                 arrayList.add(trip.id)
                            }

                        }

                    }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {

                }
            }
        })

        return arrayList
    }

    private fun getUserName(id: Int): String{
        var user = ""

        usersViewModel.users.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            when(it.status){
                Resource.Status.SUCCESS -> {
                    for(i in it.data!!){
                            user = i.name
                        }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {

                }
            }

        })

        return user
    }

}