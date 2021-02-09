package app.mulipati_agent.ui.trip

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.mulipati_agent.R
import app.mulipati_agent.data.Cars
import app.mulipati_agent.databinding.FragmentRideBinding
import app.mulipati_agent.epoxy.cars.CarsEpoxyController
import app.mulipati_agent.shared_preferences.SharedPreferences
import java.util.*
import kotlin.collections.ArrayList

class RideFragment : Fragment() {

    private lateinit var rideBinding: FragmentRideBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rideBinding = FragmentRideBinding.inflate(inflater, container, false)
        rideBinding.lifecycleOwner = this

        return rideBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cars,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        rideBinding.districtSelect.adapter = adapter

        rideBinding.districtSelect.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    SharedPreferences(
                       requireContext()
                    ).addTripPrefs("car_type", rideBinding.districtSelect.selectedItem as String)
                    SharedPreferences(
                        requireContext()
                    ).addTripPrefs("car_photo", "mazda_demio.png")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        rideBinding.passengersOne.setOnClickListener {

            if (rideBinding.passengers.text.toString() != ""){
                SharedPreferences(requireContext()).addTripPrefs("number_of_passengers", rideBinding.passengers.text.toString())
                rideBinding.passengersOne.visibility = View.GONE
            }else{
                Toast.makeText(requireContext(), "Number of passengers can not be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}