package app.mulipati_agent.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentAddTripBinding


class AddTripFragment : Fragment() {

    private lateinit var addTripBinding: FragmentAddTripBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        addTripBinding = FragmentAddTripBinding.inflate(inflater, container, false)
        addTripBinding.lifecycleOwner = this

        return addTripBinding.root
    }

}