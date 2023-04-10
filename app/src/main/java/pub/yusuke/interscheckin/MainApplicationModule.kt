package pub.yusuke.interscheckin

import android.app.Application
import android.content.Context
import android.os.VibratorManager
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.anbora.labs.spatia.builder.SpatiaRoom
import com.google.android.gms.location.LocationServices
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.FoursquareClientImpl
import pub.yusuke.interscheckin.repositories.credentialsettings.CredentialCredentialSettingsRepositoryImpl
import pub.yusuke.interscheckin.repositories.credentialsettings.CredentialSettingsPreferences
import pub.yusuke.interscheckin.repositories.credentialsettings.CredentialSettingsPreferencesSerializer
import pub.yusuke.interscheckin.repositories.credentialsettings.CredentialSettingsRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquareCheckinsRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquareCheckinsRepositoryImpl
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquarePlacesRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquarePlacesRepositoryImpl
import pub.yusuke.interscheckin.repositories.locationaccessacquirementscreendisplayedonce.LocationAccessAcquirementScreenDisplayedOnceRepository
import pub.yusuke.interscheckin.repositories.locationaccessacquirementscreendisplayedonce.LocationAccessAcquirementScreenDisplayedOnceRepositoryImpl
import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalRepository
import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.SharedPreferencesPeriodicLocationRetrievalRepository
import pub.yusuke.interscheckin.repositories.userpreferences.UserPreferencesRepository
import pub.yusuke.interscheckin.repositories.userpreferences.UserPreferencesRepositoryImpl
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenue
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainApplicationModule {
    @Provides
    fun provideFoursquareClient(
        credentialSettingsRepository: CredentialSettingsRepository,
    ): FoursquareClient {
        val settings = runBlocking { credentialSettingsRepository.fetchCredentialSettings() }
        val oauthToken = settings.foursquareOAuthToken
        val apiKey = settings.foursquareApiKey

        return FoursquareClientImpl(
            oauthToken = oauthToken,
            apiKey = apiKey,
        )
    }

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context,
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(context)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(
        dataStore: DataStore<CredentialSettingsPreferences>,
    ): CredentialSettingsRepository {
        return CredentialCredentialSettingsRepositoryImpl(dataStore)
    }

    @Provides
    fun provideFoursquareCheckinsRepository(
        foursquareClient: FoursquareClient,
    ): FoursquareCheckinsRepository {
        return FoursquareCheckinsRepositoryImpl(foursquareClient)
    }

    @Provides
    fun provideFoursquarePlacesRepository(
        foursquareClient: FoursquareClient,
    ): FoursquarePlacesRepository {
        return FoursquarePlacesRepositoryImpl(foursquareClient)
    }

    @Singleton
    @Provides
    fun provideAead(
        application: Application,
    ): Aead {
        AeadConfig.register()

        return AndroidKeysetManager.Builder()
            .withSharedPref(application, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    @Singleton
    @Provides
    fun provideSettingsDataStore(
        application: Application,
        aead: Aead,
    ): DataStore<CredentialSettingsPreferences> {
        return DataStoreFactory.create(
            produceFile = { File(application.filesDir, "datastore/$DATASTORE_FILE") },
            serializer = CredentialSettingsPreferencesSerializer(aead),
        )
    }

    @Provides
    fun provideVibratorManager(
        @ApplicationContext context: Context,
    ) = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context,
    ) = LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ) = SpatiaRoom.databaseBuilder(
        context,
        AppDatabase::class.java,
        "interscheckin",
    ).addCallback(
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.query(
                    """
                select
                    RecoverGeometryColumn('visited_venues', 'location', ${VisitedVenue.SRID}, 'POINT', 'XY');
            """,
                ).moveToNext()
                db.query("select CreateSpatialIndex('visited_venues', 'location');").moveToNext()
            }
        },
    ).build()

    @Singleton
    @Provides
    fun provideVisitedVenueDao(
        appDatabase: AppDatabase,
    ) = appDatabase.visitedVenueDao()

    @Singleton
    @Provides
    fun provideLocationAccessAcquirementScreenDisplayedOnceRepository(
        locationAccessAcquirementScreenDisplayedOnceRepository: LocationAccessAcquirementScreenDisplayedOnceRepositoryImpl,
    ): LocationAccessAcquirementScreenDisplayedOnceRepository =
        locationAccessAcquirementScreenDisplayedOnceRepository

    @Singleton
    @Provides
    fun providePeriodicLocationRetrievalRepository(
        sharedPreferencesPeriodicLocationRetrievalRepository: SharedPreferencesPeriodicLocationRetrievalRepository,
    ): PeriodicLocationRetrievalRepository =
        sharedPreferencesPeriodicLocationRetrievalRepository

    companion object {
        private const val KEYSET_NAME = "master_keyset"
        private const val PREFERENCE_FILE = "master_key_preference"
        private const val MASTER_KEY_URI = "android-keystore://master_key"

        private const val DATASTORE_FILE = "settings.pb"
    }
}
