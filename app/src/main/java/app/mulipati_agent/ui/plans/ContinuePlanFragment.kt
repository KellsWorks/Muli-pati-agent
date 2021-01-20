package app.mulipati_agent.ui.plans

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.MainActivity
import app.mulipati_agent.databinding.FragmentContinuePlanBinding


class ContinuePlanFragment : Fragment() {

    private lateinit var continuePlanBinding: FragmentContinuePlanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        continuePlanBinding = FragmentContinuePlanBinding.inflate(inflater, container, false)
        continuePlanBinding.lifecycleOwner = this

        return continuePlanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continuePlanBinding.continuePlanBack.setOnClickListener {
            findNavController().navigateUp()
        }

        continuePlanBinding.continuePlan.setOnClickListener {
            requireActivity()
                .startActivity(
                    Intent(
                        requireActivity(), MainActivity::class.java
                    )
                )
        }
    }
}