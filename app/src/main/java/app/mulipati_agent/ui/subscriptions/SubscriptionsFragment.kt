package app.mulipati_agent.ui.subscriptions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentSubscribeBinding
import app.mulipati_agent.databinding.FragmentSubscriptionsBinding


class SubscriptionsFragment : Fragment() {

    private lateinit var subscribeBinding: FragmentSubscriptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        subscribeBinding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        subscribeBinding.lifecycleOwner = this

        return subscribeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeBinding.subscriptionBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}