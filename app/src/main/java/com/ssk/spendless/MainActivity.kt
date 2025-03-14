package com.ssk.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.spendless.navigation.SpendLessNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SpendLessAppTheme {
                SpendLessNavigation(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController
                )
            }
        }
    }
}