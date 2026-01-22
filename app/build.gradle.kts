import org.gradle.api.tasks.testing.Test
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
}

val secrets = Properties()
val secretsFile = rootProject.file("secrets.properties")
if (secretsFile.exists()) {
    secretsFile.inputStream().use { secrets.load(it) }
}

android {
    namespace = "com.fritte.eveonline"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.fritte.eveonline"
        minSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "0.1"

        val appName = secrets.getProperty("appName")
        val clientId = secrets.getProperty("clientId")
        val clientSecret = secrets.getProperty("clientSecret")
        val callbackUrl = secrets.getProperty("callbackUrl")
        val email = secrets.getProperty("email")

        buildConfigField(
            "String",
            "EVE_APP_NAME",
            "\"$appName\""
        )
        buildConfigField(
            "String",
            "EVE_CLIENT_ID",
            "\"$clientId\""
        )
        buildConfigField(
            "String",
            "EVE_CLIENT_SECRET",
            "\"$clientSecret\""
        )
        buildConfigField(
            "String",
            "EVE_CALLBACK_URL",
            "\"$callbackUrl\""
        )
        buildConfigField(
            "String",
            "EVE_EMAIL",
            "\"$email\""
        )
        buildConfigField(
            "String",
            "EVE_LOGIN_URL",
            "\"https://login.eveonline.com/\""
        )
        buildConfigField(
            "String",
            "EVE_ESI_API_URL",
            "\"https://esi.evetech.net/\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.ui.tooling.preview)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Room
    implementation(libs.room.runtime)
    debugImplementation(libs.androidx.ui.tooling)
    ksp(libs.room.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Compose
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.material3)

    // Browser
    implementation(libs.browser)
    
    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.moshi.kotlin)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
