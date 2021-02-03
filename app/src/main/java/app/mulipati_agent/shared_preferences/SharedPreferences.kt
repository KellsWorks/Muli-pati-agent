package app.mulipati_agent.shared_preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences.Editor = context.getSharedPreferences("addTrips", Context.MODE_PRIVATE).edit()

    private val getPreferences: SharedPreferences = context.getSharedPreferences("addTrips", Context.MODE_PRIVATE)

    fun addTripPrefs(key: String, value: Any){
        sharedPreferences.putString(key, value.toString())
        sharedPreferences.apply()
    }

    fun getTripsPrefs(key: String, value: Any): String? {
        return getPreferences.getString(key, value.toString())
    }
}