package pub.yusuke.interscheckin

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Test 用のランナー
 * build.gradle (:app) で参照されているが、Kotlin ソース内では参照されていないため
 * 意図的に unused の警告を Supress している
 */
@Suppress("unused")
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
