plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.ssk.settings.data"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.settings.domain)
}