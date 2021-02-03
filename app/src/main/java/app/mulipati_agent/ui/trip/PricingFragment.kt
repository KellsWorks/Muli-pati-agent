package app.mulipati_agent.ui.trip

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati_agent.databinding.FragmentPriocingBinding
import app.mulipati_agent.shared_preferences.SharedPreferences


class PricingFragment : Fragment() {

    private lateinit var pricingBinding: FragmentPriocingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pricingBinding = FragmentPriocingBinding.inflate(inflater, container, false)
        pricingBinding.lifecycleOwner = this

        return pricingBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pricingBinding.addTripPricing.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                SharedPreferences(requireContext()).addTripPrefs("price", pricingBinding.addTripPricing.text.toString())
                return@OnKeyListener true
            }
            false
        })
    }
}