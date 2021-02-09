package app.mulipati_agent.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.mulipati_agent.R
import app.mulipati_agent.data.Cars
import app.mulipati_agent.databinding.FragmentRideBinding
import app.mulipati_agent.epoxy.cars.CarsEpoxyController
import app.mulipati_agent.shared_preferences.SharedPreferences

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

        val cars = ArrayList<Cars>()

        cars.add(Cars(R.drawable.mazda_demio, "Mazda Demio", "3 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Voxy", "6 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Vitz", "3 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Mazda Demio", "3 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Mazda Demio", "3 passengers"))

        val controller = CarsEpoxyController()
        controller.setData(false, cars)

        val layout = LinearLayoutManager(requireContext())
        layout.orientation = RecyclerView.HORIZONTAL

        rideBinding.carsRecycler.setController(controller)
        rideBinding.carsRecycler.layoutManager = layout

        rideBinding.passengersOne.setOnClickListener {
            if (rideBinding.passengers.text.toString() != ""){
                SharedPreferences(requireContext()).addTripPrefs("number_of_passengers", rideBinding.passengers.text.toString())
            }else{
                Toast.makeText(requireContext(), "Number of passengers can not be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}