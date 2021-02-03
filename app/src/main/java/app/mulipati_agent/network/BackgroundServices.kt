package app.mulipati_agent.network

import android.content.Context
import android.location.Address
import android.location.Geocoder
import app.mulipati_agent.firebase.receiver.SendToken
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class BackgroundServices{

     fun sendTokenToServer(token: String, id: Int){

        GlobalScope.launch(Dispatchers.IO) {

            val api = ApiClient.client!!.create(Routes::class.java)
            val sendToken = api.sendToken(id, token)

            sendToken!!.enqueue(object: Callback<SendToken?>{

                override fun onFailure(call: Call<SendToken?>, t: Throwable) {
                    Timber.e("Failed to send token to mulipati.com server")
                }

                override fun onResponse(call: Call<SendToken?>, response: Response<SendToken?>) {
                    when(response.code()){
                        200 ->{
                            Timber.e("Token sent to mulipati.com server")
                        }else->{
                            Timber.e(response.errorBody()?.string())
                        }
                    }
                }

            })
        }
    }

//
//    private fun decodeAddress(context: Context, location: String): LatLng {
//
//        GlobalScope.launch(Dispatchers.IO) {
//            val geocode = Geocoder(context)
//
//            val addresses: List<Address> = geocode.getFromLocationName(location, 1)
//            val address = addresses[0]
//            val longitude = address.longitude
//            val latitude = address.latitude
//
//            return LatLng(latitude, longitude)
//
//        }
//
//    }
//


}