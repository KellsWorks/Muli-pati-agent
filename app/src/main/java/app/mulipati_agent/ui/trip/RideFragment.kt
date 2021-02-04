package app.mulipati_agent.ui.trip

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cars = ArrayList<Cars>()
        cars.add(Cars(R.drawable.mazda_demio, "Mazda Demio", "3 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Voxy", "6 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Vitz", "3 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Mazda Demio", "3 passengers"))
        cars.add(Cars(R.drawable.mazda_demio, "Mazda Demio", "3 passengers"))

        val controller = CarsEpoxyController()
        controller.setData(false, cars)

        rideBinding.carsRecycler.setController(controller)

            rideBinding.passengersOne.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    SharedPreferences(requireContext()).addTripPrefs("number_of_passengers", rideBinding.passengersOne.text.toString())
                    return@OnKeyListener true
                }
                false
            })

    }
}