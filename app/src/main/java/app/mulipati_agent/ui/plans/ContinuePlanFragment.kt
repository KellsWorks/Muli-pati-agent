@file:Suppress("DEPRECATION")

package app.mulipati_agent.ui.plans

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.MainActivity
import app.mulipati_agent.databinding.FragmentContinuePlanBinding
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import app.mulipati_agent.network.responses.SubscriptionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        continuePlanBinding.continuePlanBack.setOnClickListener {
            findNavController().navigateUp()
        }

        continuePlanBinding.continuePlan.setOnClickListener {
            val subscribed = context?.getSharedPreferences("subscription", Context.MODE_PRIVATE)

            if (subscribed?.getBoolean("isSubscribed", false) == true){
                requireActivity()
                    .startActivity(
                        Intent(
                            requireActivity(), MainActivity::class.java
                        )
                    )
            }else{
                continuePlanBinding.subscribeBanner.text = "You do not have active subscriptions"
            }

        }

        bindItems()
    }

    @SuppressLint("SetTextI18n")
    private fun bindItems(){

        val id = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)

        val plan = context?.getSharedPreferences("plan", Context.MODE_PRIVATE)?.getString("current_plan", "")
        continuePlanBinding.continuePlanTitle.text = "Continue with $plan plan"

        continuePlanBinding.subscribeMpamba.setOnClickListener{
            if (id != null) {
                subscribe(id, plan.toString())
            }
        }
        continuePlanBinding.subscribeAirtel.setOnClickListener {
            if (id != null) {
                subscribe(id, plan.toString())
            }
        }
    }

    private fun subscribe(agentId: Int, plan: String){

        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Subscribing...")
        dialog.setCancelable(false)

        dialog.show()

        val api = ApiClient.client!!.create(Routes::class.java)

        val subscribe: Call<SubscriptionResponse?>? = api.subscribe(agentId, plan)
        subscribe?.enqueue(object : Callback<SubscriptionResponse?> {
            override fun onFailure(call: Call<SubscriptionResponse?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "A network error occurred...",
                    Toast.LENGTH_SHORT
                ).show()
                Timber.e(t)
                dialog.dismiss()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SubscriptionResponse?>, response: Response<SubscriptionResponse?>) {

                when(response.code()){
                    200 ->{
                        val subscribed = context?.getSharedPreferences("subscription", Context.MODE_PRIVATE)
                            ?.edit()
                        if (response.body()?.subscription?.subscribed == 1)
                        {
                            subscribed?.putBoolean("isSubscribed", true)
                            subscribed?.apply()

                            continuePlanBinding.subscribeBanner.text = "You are already subscribed"

                            startActivity(
                                Intent(
                                    requireActivity(), MainActivity::class.java
                                )
                            )
                            requireActivity().overridePendingTransition(
                                android.R.anim.slide_out_right, android.R.anim.slide_in_left
                            )
                        }else{
                            subscribed?.putBoolean("isSubscribed", false)
                            subscribed?.apply()

                            Toast.makeText(
                                requireContext(),
                                "You do not have an active subscription",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                Toast.makeText(
                    requireContext(),
                    "Subscription success",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                        Intent(
                                requireActivity(), MainActivity::class.java
                        )
                )
                requireActivity().overridePendingTransition(
                        android.R.anim.slide_out_right, android.R.anim.slide_in_left)

                dialog.dismiss()
            }

        })
    }
}