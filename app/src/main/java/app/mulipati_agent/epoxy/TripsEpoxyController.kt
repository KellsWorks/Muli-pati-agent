package app.mulipati_agent.epoxy

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.DisplayMetrics
import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import app.mulipati_agent.network.responses.Basic
import com.airbnb.epoxy.Typed2EpoxyController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Method

class TripsEpoxyController: Typed2EpoxyController<Boolean?, List<Trips>>() {
    override fun buildModels(status: Boolean?, trips: List<Trips>?) {
        if (trips != null) {
            for (trip in trips){
                TripsEpoxyModel_()
                    .id(trip.id)
                    .data(trip)
                        .click { model, parentView, _, _ ->
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
                                        val id = parentView?.title?.context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
                                        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                                            when (which) {
                                                DialogInterface.BUTTON_POSITIVE -> {

                                                    val apiClient  = ApiClient.client!!.create(Routes::class.java)
                                                    val delete = id?.let {
                                                        apiClient.deleteTrip(model.data?.id,
                                                            it
                                                        )
                                                    }

                                                    delete?.enqueue(object: Callback<Basic>{
                                                        override fun onFailure(
                                                            call: Call<Basic>,
                                                            t: Throwable
                                                        ) {
                                                            Timber.e(t)
                                                        }

                                                        override fun onResponse(
                                                            call: Call<Basic>,
                                                            response: Response<Basic>
                                                        ) {
                                                            when(response.code()){
                                                                200->{
                                                                    Toast.makeText(parentView.title?.context, "Trip deleted", Toast.LENGTH_SHORT).show()
                                                                }else->{
                                                                Timber.e(response.errorBody()?.string())
                                                            }
                                                            }
                                                        }

                                                    })
                                                }
                                                DialogInterface.BUTTON_NEGATIVE -> {}
                                                DialogInterface.BUTTON_NEUTRAL -> {}
                                            }
                                        }

                                        val mBuilder = AlertDialog.Builder(parentView.title?.context)
                                            .setTitle("Delete trip")
                                            .setMessage("Notice that this action can not be reversed. Do you wish ti continue?")
                                            .setIcon(R.drawable.ic_icon)
                                            .setNegativeButton("No", dialogClickListener)
                                            .setPositiveButton("Yes", dialogClickListener)

                                        mBuilder.show()

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