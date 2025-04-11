plugins {
    alias(libs.plugins.spendless.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ssk.dashboard.presentation"
}

dependencies {

    implementation(libs.androidx.navigation.compose)
    implementation(libs.serialization)
    implementation(libs.koin.androidx.compose)

    // Project Dependencies
    implementation(projects.core.domain)
    implementation(projects.dashboard.domain)
}