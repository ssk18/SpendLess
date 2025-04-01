package com.ssk.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.run {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "implementation"(libs.findLibrary("androidx.material.icons.extended").get())
            "implementation"(libs.findLibrary("androidx.activity.compose").get())
            "debugImplementation"(libs.findLibrary("androidx.ui.tooling").get())
        }
    }
}