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
                    .click { model, parentView, clickedView, position -> SharedPreferences(
                        parentView.name!!.context
                    ).addTripPrefs("car_type", parentView.name!!.text.toString())
                        SharedPreferences(
                            parentView.name!!.context
                        ).addTripPrefs("car_photo", "mazda_demio.png")
                        parentView.checkbox?.isChecked = true
                    }
                    .addTo(this)

            }
        }
    }
}