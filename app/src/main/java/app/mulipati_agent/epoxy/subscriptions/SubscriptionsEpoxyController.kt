package app.mulipati_agent.epoxy.subscriptions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import app.mulipati_agent.R
import app.mulipati_agent.auth.Authentication
import app.mulipati_agent.data.Subscriptions
import com.airbnb.epoxy.Typed2EpoxyController

class SubscriptionsEpoxyController : Typed2EpoxyController<Boolean, List<Subscriptions>>() {

    override fun buildModels(data1: Boolean?, sub: List<Subscriptions>?) {
        if (sub != null) {
            for (item in sub){
                SubscriptionsEpoxyModel_()
                    .id(item.plan)
                    .data(item)
                    .click { _, parentView, _, _ ->
                        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    parentView.title?.context?.getSharedPreferences("plan", Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
                                    parentView.title?.context?.startActivity(
                                        Intent(
                                            parentView.title?.context, Authentication::class.java
                                        )
                                    )
                                }
                                DialogInterface.BUTTON_NEGATIVE -> {}
                                DialogInterface.BUTTON_NEUTRAL -> {}
                            }
                        }

                        val mBuilder = AlertDialog.Builder(parentView.title?.context)
                            .setTitle("Subscriptions")
                            .setMessage("This will reset your current plan. Do you want to change your subscription plan?")
                            .setIcon(R.drawable.ic_icon)
                            .setNegativeButton("No", dialogClickListener)
                            .setPositiveButton("Yes", dialogClickListener)

                        mBuilder.show()
                    }
                    .addTo(this)
            }
        }
    }
}