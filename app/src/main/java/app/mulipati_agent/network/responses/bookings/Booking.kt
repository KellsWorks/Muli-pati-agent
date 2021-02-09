package app.mulipati_agent.network.responses.bookings

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "bookings")
data class Booking(
    @SerializedName("created_at")
    val created_at: String,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("start")
    var start: String,
    @SerializedName("destination")
    var destination: String,
    @SerializedName("trip_id")
    val trip_id: Int,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user_id")
    val user_id: Int
)