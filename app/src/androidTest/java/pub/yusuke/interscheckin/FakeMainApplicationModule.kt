package pub.yusuke.interscheckin

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import pub.yusuke.interscheckin.repositories.UserPreferencesRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FakeFoursquareCheckinsRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FakeFoursquarePlacesRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquareCheckinsRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquarePlacesRepository
import pub.yusuke.interscheckin.repositories.settings.FakeSettingsRepository
import pub.yusuke.interscheckin.repositories.settings.SettingsRepository
import pub.yusuke.interscheckin.repositories.userpreferences.FakeUserPreferencesRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MainApplicationModule::class]
)
interface FakeMainApplicationModule {
    @Singleton
    @Binds
    fun bindUserPreferencesRepository(
        userPreferencesRepository: FakeUserPreferencesRepository
    ): UserPreferencesRepository

    @Singleton
    @Binds
    fun bindSettingsRepository(
        settingsRepository: FakeSettingsRepository
    ): SettingsRepository

    @Binds
    fun bindFoursquareCheckinsRepository(
        foursquareCheckinsRepository: FakeFoursquareCheckinsRepository
    ): FoursquareCheckinsRepository

    @Binds
    fun bindFoursquarePlacesRepository(
        foursquarePlacesRepository: FakeFoursquarePlacesRepository
    ): FoursquarePlacesRepository
}
