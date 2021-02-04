package app.mulipati_agent.network.responses.bookings

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bookings")
data class Booking(
    val created_at: String,
    @PrimaryKey
    val id: Int,
    val status: String,
    val trip_id: Int,
    val updated_at: String,
    val user_id: Int
)