package app.mulipati_agent.db.remote

import app.mulipati.db.BaseDataSource
import app.mulipati_agent.network.services.RemoteServices
import javax.inject.Inject


class TripsRemoteDataSource @Inject constructor(
   private val remoteServices: RemoteServices
): BaseDataSource() {

    suspend fun getTrips() = getResult { remoteServices.getTrips() }

}