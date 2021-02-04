package app.mulipati_agent.epoxy

import android.util.DisplayMetrics
import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import com.airbnb.epoxy.Typed2EpoxyController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.reflect.Method

class TripsEpoxyController: Typed2EpoxyController<Boolean?, List<Trips>>() {
    override fun buildModels(status: Boolean?, trips: List<Trips>?) {
        if (trips != null) {
            for (trip in trips){
                TripsEpoxyModel_()
                    .id(trip.id)
                    .data(trip)
                        .click { _, parentView, _, _ ->
                            val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                            popupMenu.menuInflater.inflate(R.menu.trips, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId) {
                                    R.id.edit -> {

                                        val dialog = parentView.title?.context?.let {
                                            BottomSheetDialog(
                                                it
                                            )
                                        }
                                        val view = dialog?.layoutInflater?.inflate(R.layout.dialog_edit_trip, null)
                                        if (view != null) {
                                            dialog.setContentView(view)
                                        }
                                        val metrics = DisplayMetrics()
                                        dialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                                        dialog?.behavior?.peekHeight = metrics.heightPixels
                                        dialog?.dismissWithAnimation = true
                                        dialog?.show()
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