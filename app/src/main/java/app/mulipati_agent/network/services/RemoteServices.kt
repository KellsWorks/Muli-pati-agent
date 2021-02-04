package app.mulipati_agent.network.services

import app.mulipati_agent.network.responses.notifications.NotificationsResponse
import retrofit2.Response
import retrofit2.http.POST

interface RemoteServices {

    @POST("v1/notifications/user-notifications")
    suspend fun getUserNotifications(
    ) : Response<NotificationsResponse>

}