package app.mulipati_agent.network

import app.mulipati_agent.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal object ApiClient {

    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}