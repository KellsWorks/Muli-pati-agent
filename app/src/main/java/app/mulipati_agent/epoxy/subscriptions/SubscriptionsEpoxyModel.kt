package app.mulipati_agent.epoxy.subscriptions

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import app.mulipati_agent.R
import app.mulipati_agent.data.Subscriptions
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_subscriptions)
abstract class SubscriptionsEpoxyModel : EpoxyModelWithHolder<SubscriptionsEpoxyModel.SubscriptionsEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Subscriptions? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): SubscriptionsEpoxyModelViewHolder {
        return SubscriptionsEpoxyModelViewHolder()
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: SubscriptionsEpoxyModelViewHolder) {
        super.bind(holder)

        holder.title!!.text = data!!.plan
        holder.specs!!.text = data!!.specs

        holder.check!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_subscriptions
    }

    class SubscriptionsEpoxyModelViewHolder : EpoxyHolder() {

        var title: TextView? = null
        var specs: TextView? = null

        var check: CheckBox? = null

        override fun bindView(itemView: View) {

            title = itemView.findViewById(R.id.modelSubscribeTitle)
            specs = itemView.findViewById(R.id.modelSubscribeSpecs)

            check = itemView.findViewById(R.id.modelSubscribeCheck)
        }

    }
}