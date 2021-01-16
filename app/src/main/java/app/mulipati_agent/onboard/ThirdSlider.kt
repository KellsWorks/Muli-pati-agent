package app.mulipati_agent.onboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati_agent.MainActivity
import app.mulipati_agent.databinding.FragmentThirdSliderBinding


class ThirdSlider : Fragment() {

    private lateinit var thirdSliderBinding: FragmentThirdSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        thirdSliderBinding = FragmentThirdSliderBinding.inflate(inflater, container, false)
        thirdSliderBinding.lifecycleOwner = this

        return thirdSliderBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thirdSliderBinding.getStarted.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireActivity(),
                    MainActivity::class.java
                )
            )
        }
    }

}