plugins {
    alias(libs.plugins.spendless.android.library.compose)
}

android {
    namespace = "com.ssk.core.presentation.designsystem"
}

dependencies {
    implementation(libs.google.fonts)
    implementation(projects.core.domain)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    api(libs.androidx.material3)
    
    // System UI Controller
    implementation(libs.accompanist.systemuicontroller)
    
    // Add UI Tooling for Preview
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
}