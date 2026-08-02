package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.AppDatabase
import com.example.data.RovioRepository
import com.example.data.UserPreferencesManager
import com.example.ui.RovioViewModel
import com.example.ui.RovioViewModelFactory
import com.example.ui.screens.MainScreen
import com.example.ui.theme.RovioDailyTheme

class MainActivity : ComponentActivity() {

    private val viewModel: RovioViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val userPrefs = UserPreferencesManager(applicationContext)
        val repository = RovioRepository(database.shortDao(), userPrefs)
        RovioViewModelFactory(repository, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RovioDailyTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
