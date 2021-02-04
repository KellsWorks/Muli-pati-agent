package app.mulipati_agent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.mulipati_agent.db.daos.BookingsDao
import app.mulipati_agent.db.daos.NotificationsDao
import app.mulipati_agent.db.daos.TripsDao
import app.mulipati_agent.network.responses.bookings.Booking
import app.mulipati_agent.network.responses.notifications.Notification
import app.mulipati_agent.network.responses.trips.Trip
import app.mulipati_agent.util.Constants

@Database(entities = [Notification::class, Trip::class, Booking::class], version = 1, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun notificationsDao(): NotificationsDao
    abstract fun tripsDao(): TripsDao
    abstract fun bookingsDao() : BookingsDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

}