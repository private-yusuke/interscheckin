package pub.yusuke.interscheckin.repositories.userpreferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FakeUserPreferencesRepository @Inject constructor() : UserPreferencesRepository {
    private var _userPreferencesFlow = MutableStateFlow(
        UserPreferences(
            drivingMode = false,
        ),
    )

    override val userPreferencesFlow: Flow<UserPreferences>
        get() = _userPreferencesFlow

    override suspend fun enableDrivingMode(enable: Boolean) {
        val pref = _userPreferencesFlow.first()
        _userPreferencesFlow.tryEmit(pref.copy(drivingMode = enable))
    }

    override suspend fun fetchInitialPreferences(): UserPreferences =
        _userPreferencesFlow.first()
}
