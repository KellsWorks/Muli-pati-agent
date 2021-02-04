package app.mulipati_agent.epoxy.cars

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.mulipati_agent.R
import app.mulipati_agent.data.Cars
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_cars)
abstract class CarsEpoxyModel : EpoxyModelWithHolder<CarsEpoxyModel.CarsEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Cars? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): CarsEpoxyModelViewHolder {
        return CarsEpoxyModelViewHolder()
    }

    override fun bind(holder: CarsEpoxyModelViewHolder) {
        super.bind(holder)

        holder.image?.setImageResource(data!!.image)
        holder.name!!.text = data!!.name
        holder.passengers!!.text = data!!.passengers

        holder.main!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_cars
    }

    class CarsEpoxyModelViewHolder : EpoxyHolder() {

        var image: ImageView? = null
        var name: TextView? = null
        var passengers: TextView? = null


        var checkbox: CheckBox? =  null
        var main: LinearLayout? = null

        override fun bindView(itemView: View) {

            image = itemView.findViewById(R.id.carImage)
            name = itemView.findViewById(R.id.carName)
            passengers = itemView.findViewById(R.id.carPassengers)

            checkbox = itemView.findViewById(R.id.checkCar)
            main = itemView.findViewById(R.id.selectCar)
        }

    }
}