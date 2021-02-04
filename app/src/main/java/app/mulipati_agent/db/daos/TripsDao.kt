package app.mulipati_agent.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mulipati_agent.network.responses.trips.Trip

@Dao
interface TripsDao {

    @Query("SELECT * FROM trips")
    fun getTrips() : LiveData<List<Trip>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<Trip>)

    @Query("DELETE FROM trips")
    fun deleteTrips()
}