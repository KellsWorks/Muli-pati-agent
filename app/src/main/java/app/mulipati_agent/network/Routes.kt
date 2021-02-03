package app.mulipati_agent.network

import app.mulipati.data.LocationResponse
import app.mulipati_agent.firebase.receiver.SendToken
import app.mulipati_agent.network.responses.*
import app.mulipati_agent.network.responses.account.ImageResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


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


}