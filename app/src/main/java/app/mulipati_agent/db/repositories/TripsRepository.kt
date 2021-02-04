package app.mulipati_agent.db.repositories

import app.mulipati_agent.db.daos.TripsDao
import app.mulipati_agent.db.remote.TripsRemoteDataSource
import app.mulipati_agent.util.performGetOperation
import javax.inject.Inject

class TripsRepository @Inject constructor(
    private val remoteDataSource: TripsRemoteDataSource,
    private val localDataSource: TripsDao
) {

    fun getTrips() = performGetOperation(
        databaseQuery = { localDataSource.getTrips() },
        networkCall = { remoteDataSource.getTrips() },
        saveCallResult = {

            localDataSource.deleteTrips()
            localDataSource.insertTrips(it.trips)

        }
    )

}