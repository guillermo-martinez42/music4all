package com.music4all.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.music4all.app.data.preferences.UserPreferencesRepository
import com.music4all.app.ui.theme.Music4AllTheme

/**
 * Main entry point for the app.
 * Displays the onboarding / settings screen where the user selects their preferred
 * music service.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencesRepository = UserPreferencesRepository(applicationContext)
        val viewModel = ViewModelProvider(
            this,
            MainViewModel.Factory(preferencesRepository)
        )[MainViewModel::class.java]

        setContent {
            Music4AllTheme {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
