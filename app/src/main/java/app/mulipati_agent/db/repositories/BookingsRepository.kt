package app.mulipati_agent.db.repositories

import app.mulipati_agent.db.daos.BookingsDao
import app.mulipati_agent.db.remote.BookingsRemoteDataSource
import app.mulipati_agent.util.performGetOperation
import javax.inject.Inject

class BookingsRepository @Inject constructor(
    private val remoteDataSource: BookingsRemoteDataSource,
    private val localDataSource: BookingsDao
) {

    fun getBookings() = performGetOperation(
        databaseQuery = { localDataSource.getBookings() },
        networkCall = { remoteDataSource.getBookings() },
        saveCallResult = {

            localDataSource.deleteBookings()
            localDataSource.insertBookings(it.bookings)

        }
    )

}