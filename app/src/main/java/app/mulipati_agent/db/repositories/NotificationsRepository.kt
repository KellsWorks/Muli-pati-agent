package app.mulipati_agent.db.repositories

import app.mulipati_agent.db.daos.NotificationsDao
import app.mulipati_agent.db.remote.NotificationsRemoteDataSource
import app.mulipati_agent.util.performGetOperation
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val remoteDataSource: NotificationsRemoteDataSource,
    private val localDataSource: NotificationsDao
) {

    fun getNotifications() = performGetOperation(
        databaseQuery = { localDataSource.getNotifications() },
        networkCall = { remoteDataSource.getNotifications() },
        saveCallResult = { localDataSource.insertNotifications(it.notifications) }
    )

}