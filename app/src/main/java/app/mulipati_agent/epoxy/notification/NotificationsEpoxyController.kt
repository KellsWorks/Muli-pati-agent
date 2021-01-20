package app.mulipati_agent.epoxy.notification

import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati.data.Notifications
import com.airbnb.epoxy.Typed2EpoxyController
import java.lang.reflect.Method


class NotificationsEpoxyController: Typed2EpoxyController<Boolean?, List<Notifications>>() {
    override fun buildModels(status: Boolean?, notifications: List<Notifications>?) {

    }
}