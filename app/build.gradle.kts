plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.plugin.serialization)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "pub.yusuke.interscheckin"
    compileSdk = 33

    defaultConfig {
        applicationId = "pub.yusuke.interscheckin"
        minSdk = 32
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "pub.yusuke.interscheckin.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        resConfigs("en", "ja")


        javaCompileOptions {
            annotationProcessorOptions {
                arguments += "room.schemaLocation" to "$projectDir/schemas"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.compose.ui.core)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.gms.play.services.location)
    implementation(libs.dagger.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)

    // Paging
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.compose)

    // For loading remote images with Jetpack Compose
    implementation(libs.coil.compose)

    implementation(project(path = ":library:foursquare-client"))
    kapt(libs.dagger.hilt.compiler)
    implementation(project(path = ":library:fusedlocationktx"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.dagger.hilt.android.testing)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)

    // For encrypting and decrypting credentials
    implementation(libs.tink.android)
    implementation(libs.kotlinx.serialization.protobuf)

    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)
    implementation(libs.spatia.room)

    // Immutable collections for Composable functions
    implementation(libs.kotlinx.collections.immutable)

    // Requesting the permission for location access within screens written with Jetpack Compose
    implementation(libs.accompanist.permissions)
}
