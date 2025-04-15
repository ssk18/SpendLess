plugins {
    alias(libs.plugins.spendless.android.feature.ui)
}

android {
    namespace = "com.ssk.settings.presentation"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.settings.domain)
}