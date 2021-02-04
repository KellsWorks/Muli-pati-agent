package app.mulipati_agent.epoxy

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_trips)
abstract class TripsEpoxyModel : EpoxyModelWithHolder<TripsEpoxyModel.RecentTripsEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Trips? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): RecentTripsEpoxyModelViewHolder {
        return RecentTripsEpoxyModelViewHolder()
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: RecentTripsEpoxyModelViewHolder) {
        super.bind(holder)

        holder.title!!.text = data!!.destination + " trip "
                holder.route!!.text = data!!.start + " - " + data!!.destination
        holder.passengers!!.text = "0 passengers | 3 remaining"


        holder.menu!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_trips
    }

    class RecentTripsEpoxyModelViewHolder : EpoxyHolder() {

        var title: TextView? = null
        var route: TextView? = null
        var passengers: TextView? = null

        var menu: ImageView? =  null

        override fun bindView(itemView: View) {

            title = itemView.findViewById(R.id.tripsTitle)
            route = itemView.findViewById(R.id.tripsRoutw)
            passengers = itemView.findViewById(R.id.tripsPassengers)
            menu = itemView.findViewById(R.id.tripsMenu)
        }

    }
}