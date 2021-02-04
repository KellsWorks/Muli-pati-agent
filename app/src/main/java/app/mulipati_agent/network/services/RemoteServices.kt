package app.mulipati_agent.network.services

import app.mulipati_agent.network.responses.notifications.NotificationsResponse
import app.mulipati_agent.network.responses.trips.TripsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteServices {

    @POST("v1/notifications/user-notifications")
    suspend fun getUserNotifications(
    ) : Response<NotificationsResponse>

    @GET("v1/trips/all")
    suspend fun getTrips(
    ) : Response<TripsResponse>


}