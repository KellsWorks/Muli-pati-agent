package app.mulipati_agent.epoxy

import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import com.airbnb.epoxy.Typed2EpoxyController
import java.lang.reflect.Method

class TripsEpoxyController: Typed2EpoxyController<Boolean?, List<Trips>>() {
    override fun buildModels(status: Boolean?, trips: List<Trips>?) {
        if (trips != null) {
            for (trip in trips){

            }
        }
    }
}