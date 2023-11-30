import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kotlin.serialization)
    jacoco
}

android {
    namespace = "com.softteco.template"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.softteco.template.ble"
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
        val envBluetoothCharacteristicUUID =
            "\"${System.getenv("BLUETOOTH_CHARACTERISTIC_UUID_VALUE")}\""
        val envBluetoothDescriptorUUID =
            "\"${System.getenv("BLUETOOTH_DESCRIPTOR_UUID_VALUE")}\""
        val baseUrl = gradleLocalProperties(rootDir).getProperty("BASE_URL", envBaseUrl)
        val bluetoothServiceUUID = gradleLocalProperties(rootDir).getProperty(
            "BLUETOOTH_SERVICE_UUID_VALUE",
            envBluetoothServiceUUID
        )
        val bluetoothCharacteristicUUID = gradleLocalProperties(rootDir).getProperty(
            "BLUETOOTH_CHARACTERISTIC_UUID_VALUE",
            envBluetoothCharacteristicUUID
        )
        val bluetoothDescriptorUUID = gradleLocalProperties(rootDir).getProperty(
            "BLUETOOTH_DESCRIPTOR_UUID_VALUE",
            envBluetoothDescriptorUUID
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
                "BLUETOOTH_CHARACTERISTIC_UUID_VALUE",
                bluetoothCharacteristicUUID
            )
            buildConfigField(
                "String",
                "BLUETOOTH_DESCRIPTOR_UUID_VALUE",
                bluetoothDescriptorUUID
            )
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL", baseUrl)
            buildConfigField("String", "BLUETOOTH_SERVICE_UUID_VALUE", bluetoothServiceUUID)
            buildConfigField(
                "String",
                "BLUETOOTH_CHARACTERISTIC_UUID_VALUE",
                bluetoothCharacteristicUUID
            )
            buildConfigField(
                "String",
                "BLUETOOTH_DESCRIPTOR_UUID_VALUE",
                bluetoothDescriptorUUID
            )
            // disabled because of unit tests errors,
            // could be restored after running instrumentation tests on CI, and fixing unit tests errors
            // enableAndroidTestCoverage = true
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
    testOptions {
        packaging {
            jniLibs { useLegacyPackaging = true }
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
    implementation(libs.androidx.browser)
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
    implementation(libs.androidx.compose.icons)

    implementation(libs.retrofit)
    implementation(libs.retrofit.coroutines.adapter)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp)
    implementation(libs.okhttpLoggingInterceptor)
    implementation(libs.nordicSemiScanner)
    implementation(libs.hellocharts)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.signature.pad.compose)
    implementation(libs.io.coil.kt.coil.svg)
    implementation(libs.io.coil.kt.coil.compose)

    implementation(libs.preferences.data.store)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.kotlin.reflect)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.io.mockk.mockk.android)
    androidTestImplementation(libs.io.mockk.mockk.agent)
    androidTestImplementation(libs.org.junit.jupiter.jupiter)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.org.junit.jupiter.jupiter)
    testImplementation(libs.org.junit.vintage.engine)
    testImplementation(libs.io.mockk.mockk)
    testImplementation(libs.app.cash.turbine)
    testImplementation(libs.io.kotest.kotest.assertions)
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events("started", "skipped", "passed", "failed")
        showStandardStreams = true
    }
    configure<JacocoTaskExtension> {
        isEnabled = true
        isIncludeNoLocationClasses = false
    }
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
