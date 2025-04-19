plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.ssk.dashboard.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.koin.android.workmanager)
    implementation(libs.serialization)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.dashboard.domain)
}