plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.ssk.sync.work"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.koin.android.workmanager)
    implementation(libs.androidx.work)
    implementation(libs.serialization)

    implementation(projects.core.domain)

    implementation(libs.timber)
}