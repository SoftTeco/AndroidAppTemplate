import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    id("com.google.gms.google-services") version "4.4.0"
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
        val baseUrl = gradleLocalProperties(rootDir).getProperty("BASE_URL", envBaseUrl)

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            buildConfigField("String", "BASE_URL", baseUrl)
            buildConfigField("String", "GOOGLE_SERVICES_JSON", "\"{}\"")
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL", baseUrl)
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.accompanist.permissions)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.messaging)
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

tasks.register("generateGoogleServicesJson") {
    doLast {
        // Check if running in a CI environment
        val isCiBuild = System.getenv("CI")?.toBoolean() ?: false
        if (isCiBuild) {
            val googleServicesJsonContent = """
            {
              "project_info": {
                "project_number": "FCM_PROJECT_NUMBER",
                "project_id": "FCM_PROJECT_ID",
                "storage_bucket": "FCM_STORAGE_BUCKET"
              },
              "client": [
                {
                  "client_info": {
                    "mobilesdk_app_id": "1:FCM_MOBILESDK_APP_ID:android:FCM_ANDROID_CLIENT_INFO",
                    "android_client_info": {
                      "package_name": "FCM_PACKAGE_NAME"
                    }
                  },
                  "oauth_client": [],
                  "api_key": [
                    {
                      "current_key": "FCM_API_KEY"
                    }
                  ],
                  "services": {
                    "appinvite_service": {
                      "other_platform_oauth_client": []
                    }
                  }
                }
              ],
              "configuration_version": "1"
            }
            """.trimIndent()

            val outputDir = project.file("app/google-services")
            outputDir.mkdirs()
            val outputFile = outputDir.resolve("google-services.json")
            outputFile.writeText(googleServicesJsonContent)
        } else {
            println("Skipping google-services.json generation as this is not a CI build")
        }
    }
}

tasks.preBuild.dependsOn("generateGoogleServicesJson")

