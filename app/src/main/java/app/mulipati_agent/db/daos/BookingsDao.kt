package app.mulipati_agent.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati_agent.network.responses.bookings.Booking

@Dao
interface BookingsDao {

    @Query("SELECT * FROM bookings")
    fun getBookings() : LiveData<List<Booking>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<Booking>)

    @Query("DELETE FROM bookings")
    fun deleteBookings()

}