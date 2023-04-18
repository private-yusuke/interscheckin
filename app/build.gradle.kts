plugins {
    kotlin("android")
    id("com.android.application")
    id("kotlinx-serialization")
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
        kotlinCompilerExtensionVersion = "1.4.5"
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
    val composeBom = platform("androidx.compose:compose-bom:2023.04.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.fragment:fragment-ktx:1.5.6")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material")
    implementation(DependencyNames.navigationCompose)
    implementation(DependencyNames.datastorePreferences)
    implementation(DependencyNames.playServicesLocation)
    implementation(DependencyNames.hiltAndroid)
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Paging
    implementation(DependencyNames.pagingCommon)
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")

    // For loading remote images with Jetpack Compose
    implementation("io.coil-kt:coil-compose:2.3.0")

    implementation(project(path = ":library:foursquare-client"))
    kapt(DependencyNames.hiltCompiler)
    implementation(project(path = ":library:fusedlocationktx"))
    testImplementation(DependencyNames.junit)
    testImplementation(DependencyNames.mockk)
    androidTestImplementation(DependencyNames.mockkAndroid)
    androidTestImplementation(DependencyNames.mockkAgent)
    androidTestImplementation(DependencyNames.androidxJunit)
    androidTestImplementation(DependencyNames.espressoCore)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    androidTestImplementation(DependencyNames.hiltAndroidTesting)
    kaptAndroidTest(DependencyNames.hiltAndroidCompiler)

    // For encrypting and decrypting credentials
    implementation("com.google.crypto.tink:tink-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.5.0")

    // Room
    implementation(DependencyNames.roomRuntime)
    kapt(DependencyNames.roomCompiler)
    implementation(DependencyNames.roomPaging)
    implementation(DependencyNames.roomKtx)
    implementation("com.github.anboralabs:spatia-room:0.2.6")

    // Immutable collections for Composable functions
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

    // Requesting the permission for location access within screens written with Jetpack Compose
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
}
