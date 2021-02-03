package app.mulipati_agent.epoxy.cars

import app.mulipati_agent.data.Cars
import app.mulipati_agent.shared_preferences.SharedPreferences
import com.airbnb.epoxy.Typed2EpoxyController

class CarsEpoxyController: Typed2EpoxyController<Boolean, List<Cars>>() {
    override fun buildModels(status: Boolean?, cars: List<Cars>?) {
        if (cars != null) {
            for (car in cars){
                CarsEpoxyModel_()
                    .id(car.name)
                    .data(car)
                    .click { _, parentView, _, _ ->
                            SharedPreferences(parentView.name!!.context).addTripPrefs("car_type", parentView.name!!.text.toString())
                    }
                    .addTo(this)
            }
        }
    }
}