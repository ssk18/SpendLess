plugins {
    alias(libs.plugins.spendless.android.library)
    alias(libs.plugins.spendless.android.room)
}

android {
    namespace = "com.ssk.core.database"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.sqlcipher)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.datastore.preference)
    implementation(libs.androidx.datastore.core)
    implementation(projects.core.domain)
}