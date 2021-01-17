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
                TripsEpoxyModel_()
                    .id(trip.title)
                    .data(trip)
                        .click { _, parentView, _, _ ->
                            val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                            popupMenu.menuInflater.inflate(R.menu.trips, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId) {
                                    R.id.edit -> {
                                        Toast.makeText(parentView.route!!.context, "Edited!", Toast.LENGTH_SHORT)
                                                .show()
                                    }

                                    R.id.delete -> {
                                        Toast.makeText(parentView.route!!.context, "Deleted!", Toast.LENGTH_SHORT)
                                                .show()
                                    }
                                }
                                true
                            }

                            try {
                                val classPopupMenu = Class.forName(
                                        popupMenu
                                                .javaClass.name
                                )
                                val mPopup =
                                        classPopupMenu.getDeclaredField("mPopup")
                                mPopup.isAccessible = true
                                val menuPopupHelper = mPopup[popupMenu]
                                val classPopupHelper = Class.forName(
                                        menuPopupHelper
                                                .javaClass.name
                                )
                                val setForceIcons: Method = classPopupHelper.getMethod(
                                        "setForceShowIcon", Boolean::class.javaPrimitiveType
                                )
                                setForceIcons.invoke(menuPopupHelper, true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            popupMenu.show()
                        }
                    .addTo(this)
            }
        }
    }
}