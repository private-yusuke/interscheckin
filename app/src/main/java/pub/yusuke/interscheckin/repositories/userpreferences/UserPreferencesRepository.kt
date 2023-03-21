package pub.yusuke.interscheckin.repositories.userpreferences

import kotlinx.coroutines.flow.Flow

data class UserPreferences(
    val drivingMode: Boolean,
)

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun enableDrivingMode(enable: Boolean)
    suspend fun fetchInitialPreferences(): UserPreferences
}
