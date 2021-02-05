package app.mulipati_agent.ui.subscriptions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.data.Subscriptions
import app.mulipati_agent.databinding.FragmentSubscriptionsBinding
import app.mulipati_agent.epoxy.subscriptions.SubscriptionsEpoxyController


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

        bindPlan()
    }

    @SuppressLint("SetTextI18n")
    private fun bindPlan(){
        val plan = context?.getSharedPreferences("plan", Context.MODE_PRIVATE)?.getString("current_plan", "")
        subscribeBinding.currentPlan.text = plan

        when(plan){
            "silver" -> {
                subscribeBinding.ridesCount.text = "Post 3 rides per day"
            }
            "bronze" -> {
                subscribeBinding.ridesCount.text = "Post 6 rides per day"
            }
            "premium" -> {
                subscribeBinding.ridesCount.text = "Post 10 rides per day"
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val subscriptions = ArrayList<Subscriptions>()

        subscriptions.add(Subscriptions("Silver plan", "MK500 per month"))
        subscriptions.add(Subscriptions("Bronze plan", "MK1, 000 per month"))
        subscriptions.add(Subscriptions("Premium plan", "MK1, 5000 per month"))

        val controller = SubscriptionsEpoxyController()
        controller.setData(false, subscriptions)

        subscribeBinding.subscriptionsRecycler.setController(controller)

    }
}