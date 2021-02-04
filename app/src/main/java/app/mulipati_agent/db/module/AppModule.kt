package app.mulipati_agent.db.module

import android.content.Context
import app.mulipati_agent.db.AppDatabase
import app.mulipati_agent.db.daos.NotificationsDao
import app.mulipati_agent.db.daos.TripsDao
import app.mulipati_agent.db.remote.NotificationsRemoteDataSource
import app.mulipati_agent.db.remote.TripsRemoteDataSource
import app.mulipati_agent.db.repositories.NotificationsRepository
import app.mulipati_agent.db.repositories.TripsRepository
import app.mulipati_agent.network.services.RemoteServices
import app.mulipati_agent.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    private var okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    //Trips

    @Singleton
    @Provides
    fun provideTripsDao(db: AppDatabase) = db.tripsDao()

    @Singleton
    @Provides
    fun provideTripsRepository(remoteDataSource: TripsRemoteDataSource,
                                       localDataSource: TripsDao
    ) =
        TripsRepository(remoteDataSource, localDataSource)

    //Notifications

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideRemoteService(retrofit: Retrofit): RemoteServices = retrofit.create(RemoteServices::class.java)

    @Singleton
    @Provides
    fun provideTripsRemoteDataSource(remoteServices: RemoteServices) = NotificationsRemoteDataSource(remoteServices)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideNotificationsDao(db: AppDatabase) = db.notificationsDao()

    @Singleton
    @Provides
    fun provideNotificationsRepository(remoteDataSource: NotificationsRemoteDataSource,
                                       localDataSource: NotificationsDao
    ) =
        NotificationsRepository(remoteDataSource, localDataSource)
}