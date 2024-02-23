package pub.yusuke.interscheckin.repositories.locationaccessacquirementscreendisplayedonce

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationAccessAcquirementScreenDisplayedOnceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationAccessAcquirementScreenDisplayedOnceRepository {
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    override var locationAccessAcquirementScreenDisplayedOnce: Boolean
        get() = preferences.getBoolean(KEY_LOCATION_ACCESS_ACQUIREMENT_SCREEN_DISPLAYED_ONCE, false)
        set(value) {
            preferences.edit {
                putBoolean(KEY_LOCATION_ACCESS_ACQUIREMENT_SCREEN_DISPLAYED_ONCE, value)
            }
        }

    companion object {
        const val FILE_NAME = "location_access_acquirement_screen_displayed_once"
        private const val KEY_LOCATION_ACCESS_ACQUIREMENT_SCREEN_DISPLAYED_ONCE =
            "locationAccessAcquirementScreenDisplayedOnce"
    }
}
