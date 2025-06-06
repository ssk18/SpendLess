plugins {
    alias(libs.plugins.spendless.android.library)
}

android {
    namespace = "com.ssk.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}