package app.mulipati_agent.epoxy.cars

import app.mulipati_agent.data.Cars
import com.airbnb.epoxy.Typed2EpoxyController

class CarsEpoxyController: Typed2EpoxyController<Boolean, List<Cars>>() {
    override fun buildModels(status: Boolean?, cars: List<Cars>?) {
        if (cars != null) {
            for (car in cars){
                CarsEpoxyModel_()
                    .id(car.name)
                    .data(car)
                    .addTo(this)
            }
        }
    }
}