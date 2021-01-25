package app.mulipati_agent.auth.subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentSubscribeBinding


class SubscribeFragment : Fragment() {

    private lateinit var subscribeBinding: FragmentSubscribeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        subscribeBinding = FragmentSubscribeBinding.inflate(inflater, container, false)
        subscribeBinding.lifecycleOwner = this

        return subscribeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeBinding.subscribeBack.setOnClickListener {
            findNavController().navigateUp()
        }

        subscribeBinding.silverSubscription.setOnClickListener {
            findNavController().navigate(R.id.action_subscribeFragment_to_continuePlanFragment)
        }

        subscribeBinding.bronzeSubscription.setOnClickListener {
            findNavController().navigate(R.id.action_subscribeFragment_to_continuePlanFragment)
        }

        subscribeBinding.goldSubscription.setOnClickListener {
            findNavController().navigate(R.id.action_subscribeFragment_to_continuePlanFragment)
        }
    }

}