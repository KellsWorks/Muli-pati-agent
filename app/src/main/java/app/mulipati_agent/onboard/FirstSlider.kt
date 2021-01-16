package app.mulipati_agent.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentFirstSliderBinding


class FirstSlider : Fragment() {

    private lateinit var firstSliderBinding: FragmentFirstSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firstSliderBinding = FragmentFirstSliderBinding.inflate(inflater, container, false)
        firstSliderBinding.lifecycleOwner = this

        return  firstSliderBinding.root
    }


}