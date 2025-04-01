plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ssk.spendless"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ssk.spendless"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room components
    implementation(libs.room.runtime)
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    // Sqlcipher
    implementation(libs.sqlcipher)
    implementation(libs.sqlite)

    // google fonts
    implementation(libs.google.fonts)

    // Accompanist
    implementation(libs.accompanist.insets)

    // Dagger Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.compose)

    // Coil
    implementation(libs.coil)

    //Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Splashscreen
    implementation(libs.androidx.splashscreen)

    // Extended icons
    implementation(libs.androidx.material.icons.extended)

    // Gson
    implementation(libs.gson)

    // Serialization
    implementation(libs.serialization)

    // Glance
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.glance.appwidget)

    // Groq
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.core)
    implementation(libs.groq)

    implementation(libs.timber)

    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)

    implementation(projects.auth.presentation)
    implementation(projects.auth.domain)
    implementation(projects.auth.data)

}