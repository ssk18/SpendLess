package com.ssk.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ssk.auth.presentation.screens.user_preference.OnboardingPreferenceScreenRoot
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SpendLessAppTheme {
                OnboardingPreferenceScreenRoot()
            }
        }
    }
}