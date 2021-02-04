package app.mulipati_agent.epoxy.bookings

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati_agent.R
import app.mulipati_agent.data.Bookings
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_booking)
abstract class BookingsEpoxyModel : EpoxyModelWithHolder<BookingsEpoxyModel.BookingsEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Bookings? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): BookingsEpoxyModelViewHolder {
        return BookingsEpoxyModelViewHolder()
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: BookingsEpoxyModelViewHolder) {
        super.bind(holder)

        holder.bookerLocation!!.text = data!!.bookerLocation
        holder.bookerName!!.text = data!!.bookerName
        holder.date!!.text = data!!.date
        holder.day!!.text = data!!.day



        holder.chat!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_booking
    }

    class BookingsEpoxyModelViewHolder : EpoxyHolder() {

        var date: TextView? = null
        var day: TextView? = null
        var bookerName: TextView? = null
        var bookerLocation: TextView? = null

        var chat: ImageView? = null
        var id: Int? =  null

        override fun bindView(itemView: View) {

            date = itemView.findViewById(R.id.bookDate)
            day = itemView.findViewById(R.id.bookDay)
            bookerName = itemView.findViewById(R.id.booker)
            bookerLocation = itemView.findViewById(R.id.bookerLocation)

            chat = itemView.findViewById(R.id.bookChat)
        }

    }
}