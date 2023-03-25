package pub.yusuke.interscheckin.ui.main

import android.os.VibratorManager
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import pub.yusuke.fusedlocationktx.currentLocation
import pub.yusuke.interscheckin.ui.main.util.MainScreenTestData

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MainViewModelModule::class],
)
interface FakeMainViewModelModule {
    companion object {
        @Provides
        fun provideInteractor(
            mainInteractor: MainInteractor
        ): MainContract.Interactor {
            val interactor = spyk(mainInteractor)

            // Assume that the access to precise locations is available
            every { interactor.locationProvidersAvailable() } returns true
            every { interactor.preciseLocationAccessAvailable() } returns true

            return interactor
        }

        @Provides
        fun provideViewModel(
            interactor: MainContract.Interactor,
        ): MainContract.ViewModel {
            val vm = spyk(MainViewModel(interactor))

            // Defaults to both the location and the venues loaded
            every { vm.locationState } returns mutableStateOf(MainScreenTestData.locationStateLoaded)
            every { vm.venuesState } returns mutableStateOf(MainScreenTestData.venuesStateIdle)

            return vm
        }

        @Provides
        fun provideVibratorManager(): VibratorManager {
            val vibratorManager: VibratorManager = mockk()
            every { vibratorManager.defaultVibrator } returns mockk(relaxUnitFun = true)

            return vibratorManager
        }

        @Provides
        fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
            val fusedLocationProviderClient: FusedLocationProviderClient = mockk()

            every {
                fusedLocationProviderClient.requestLocationUpdates(
                    any(),
                    any<LocationCallback>(),
                    any(),
                )
            } returns mockk()
            every {
                fusedLocationProviderClient.removeLocationUpdates(any<LocationCallback>())
            } returns mockk()
            coEvery {
                fusedLocationProviderClient.currentLocation()
            } returns MainScreenTestData.location

            return fusedLocationProviderClient
        }
    }
}
