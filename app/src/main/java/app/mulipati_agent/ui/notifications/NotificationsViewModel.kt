package app.mulipati_agent.ui.notifications

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.util.Resource
import app.mulipati_agent.db.repositories.NotificationsRepository
import app.mulipati_agent.network.responses.notifications.Notification

class NotificationsViewModel @ViewModelInject constructor(
    repository: NotificationsRepository
) : ViewModel() {

    val notications: LiveData<Resource<List<Notification>>> = repository.getNotifications()

}