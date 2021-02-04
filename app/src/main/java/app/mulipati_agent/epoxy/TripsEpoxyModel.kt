package app.mulipati_agent.epoxy

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import app.mulipati_agent.util.Constants
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView


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

        Glide
            .with(holder.image!!.context)
            .load(Constants.PROFILE_URL+ data!!.car_photo)
            .centerCrop()
            .into(holder.image!!)

        holder.title!!.text = data!!.destination + " trip "
                holder.route!!.text = data!!.start + " - " + data!!.destination
        holder.price!!.text = data!!.passenger_fare


        holder.menu!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_trips
    }

    class RecentTripsEpoxyModelViewHolder : EpoxyHolder() {

        var image: RoundedImageView? = null
        var title: TextView? = null
        var route: TextView? = null
        var price: TextView? = null

        var menu: ImageView? =  null

        override fun bindView(itemView: View) {

            image = itemView.findViewById(R.id.tripsImage)
            title = itemView.findViewById(R.id.tripsTitle)
            route = itemView.findViewById(R.id.tripsRoutw)
            price = itemView.findViewById(R.id.tripsPrice)

            menu = itemView.findViewById(R.id.tripsMenu)
        }

    }
}