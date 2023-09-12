import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.softteco.template"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.softteco.template"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        configurations.all {
            resolutionStrategy {
                force("androidx.emoji2:emoji2-views-helper:1.3.0")
                force("androidx.emoji2:emoji2:1.3.0")
            }
        }
        android.buildFeatures.buildConfig = true
    }

    buildTypes {

        val envBaseUrl = "\"${System.getenv("BASE_URL")}\""
        val envBluetoothServiceUUID = "\"${System.getenv("BLUETOOTH_SERVICE_UUID_VALUE")}\""
        val envBluetoothCharacteristicForWriteUUID =
            "\"${System.getenv("BLUETOOTH_CHARACTERISTIC_FOR_WRITE_UUID_VALUE")}\""
        val baseUrl = gradleLocalProperties(rootDir).getProperty("BASE_URL", envBaseUrl)
        val bluetoothServiceUUID = gradleLocalProperties(rootDir).getProperty(
            "BLUETOOTH_SERVICE_UUID_VALUE",
            envBluetoothServiceUUID
        )
        val bluetoothCharacteristicForWriteUUID = gradleLocalProperties(rootDir).getProperty(
            "BLUETOOTH_CHARACTERISTIC_FOR_WRITE_UUID_VALUE",
            envBluetoothCharacteristicForWriteUUID
        )

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            buildConfigField("String", "BASE_URL", baseUrl)
            buildConfigField("String", "BLUETOOTH_SERVICE_UUID_VALUE", bluetoothServiceUUID)
            buildConfigField(
                "String",
                "BLUETOOTH_CHARACTERISTIC_FOR_WRITE_UUID_VALUE",
                bluetoothCharacteristicForWriteUUID
            )
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL", baseUrl)
            buildConfigField("String", "BLUETOOTH_SERVICE_UUID_VALUE", bluetoothServiceUUID)
            buildConfigField(
                "String",
                "BLUETOOTH_CHARACTERISTIC_FOR_WRITE_UUID_VALUE",
                bluetoothCharacteristicForWriteUUID
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi.onverter)
    implementation(libs.retrofit.coroutines.adapter)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.okhttp)
    implementation(libs.okhttpLoggingInterceptor)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.rxAndroidBle)
    implementation(libs.nordicSemiBLE)
    implementation(libs.nordicSemiScanner)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
