package app.mulipati_agent.network

import app.mulipati.data.LocationResponse
import app.mulipati_agent.firebase.receiver.SendToken
import app.mulipati_agent.network.responses.*
import app.mulipati_agent.network.responses.account.ImageResponse
import app.mulipati_agent.network.responses.chat.MessageSent
import app.mulipati_agent.network.responses.chat.MessagesResponse
import retrofit2.Call
import retrofit2.http.*


interface Routes {

    //Notifications token

    @POST("v1/fcm-token/save")
    @FormUrlEncoded
    fun sendToken(
            @Field("id") id: Int?,
            @Field("token") token: String?
    ): Call<SendToken?>?

    //User routes

    @POST(" v1/register-admin")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse?>?

    @POST("v1/login")
    @FormUrlEncoded
    fun login(
        @Field("phone") phone: String?,
        @Field("password") password: String?
    ): Call<User?>?

    @POST("v1/update-photo")
    @FormUrlEncoded
    fun photoUpdate(
        @Field("id") id: Int?,
        @Field("photo") photo: String?
    ): Call<ImageResponse?>?

    @POST("v1/update-location")
    @FormUrlEncoded
    fun photoLocation(
        @Field("id") id: Int?,
        @Field("location") location: String?
    ): Call<LocationResponse?>?

    @POST("v1/update-account")
    @FormUrlEncoded
    fun accountUpdate(
        @Field("id") id: Int?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("phone") phone: String?
    ): Call<AccountUpdateResponse?>?

    @POST("v1/delete")
    @FormUrlEncoded
    fun accountDelete(
        @Field("id") id: Int?
    ): Call<Basic?>?

    //Subscriptions
    @POST(" v1/subscription/subscribe")
    @FormUrlEncoded
    fun subscribe(
        @Field("agent_id") agent_id: Int?,
        @Field("plan") plan: String?
    ): Call<SubscriptionResponse?>?

    //Subscriptions
    @POST(" v1/subscription/subscribe")
    @FormUrlEncoded
    fun addTrip(
            @Field("agent_id") agent_id: Int?,
            @Field("plan") plan: String?
    ): Call<SubscriptionResponse?>?


    ///Trips create *******important routes*********
    @POST(" v1/trips/create")
    @FormUrlEncoded
    fun addNewTrip(
        @Field("id") id: Int?,
        @Field("start") start: String?,
        @Field("destination") destination: String?,
        @Field("start_time") start_time: String?,
        @Field("end_time") end_time: String?,
        @Field("pick_up_place") pick_up_place: String?,
        @Field("location") location: String?,
        @Field("number_of_passengers") number_of_passengers: String?,
        @Field("passenger_fare") passenger_fare: String?,
        @Field("car_type") car_type: String?,
        @Field("car_photo") car_photo: String?
    ): Call<Basic?>?

    @POST("v1/trips/delete")
    @FormUrlEncoded
    fun deleteTrip(
        @Field("id") id : Int?,
        @Field("userId") userId : Int
    ): Call<Basic>


    //Notifications routes
    @POST("v1/notifications/user-mark-notification")
    @FormUrlEncoded
    fun markAsRead(
        @Field("id") id: Int?
    ): Call<Basic?>?

    @POST("v1/notifications/user-notification-delete")
    @FormUrlEncoded
    fun deleteNotification(
        @Field("id") id: Int?
    ): Call<Basic?>?


    //Messages
    @POST("v1/message/get-messages")
    @FormUrlEncoded
    fun tripMessages(
        @Field("toId")toId: Int?,
        @Field("fromId")fromId: Int?
    ): Call<MessagesResponse>

    @POST("v1/message/create")
    @FormUrlEncoded
    fun sendMessage(
        @Field("to") to: Int?,
        @Field("from") from: Int?,
        @Field("message") message: String?,
        @Field("time") time: String?
    ): Call<MessageSent>


}