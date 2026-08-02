package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesManager(private val context: Context) {

    companion object {
        val LAST_ARM_TIME = longPreferencesKey("last_arm_time")
        val STREAK = intPreferencesKey("streak")
        val FREEZE_SHIELDS = intPreferencesKey("freeze_shields")
        val LAST_RESET_DATE = stringPreferencesKey("last_reset_date")
        val SELECTED_THEME = intPreferencesKey("selected_theme")
    }

    private val safeDataStore = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }

    val lastArmTime: Flow<Long> = safeDataStore.map { prefs ->
        prefs[LAST_ARM_TIME] ?: 0L
    }

    val selectedThemeIndex: Flow<Int> = safeDataStore.map { prefs ->
        prefs[SELECTED_THEME] ?: 0
    }

    val streak: Flow<Int> = safeDataStore.map { prefs ->
        prefs[STREAK] ?: 14 // Default mock initial streak 14
    }

    val freezeShields: Flow<Int> = safeDataStore.map { prefs ->
        prefs[FREEZE_SHIELDS] ?: 1 // Default 1 freeze shield
    }

    val lastResetDate: Flow<String> = safeDataStore.map { prefs ->
        prefs[LAST_RESET_DATE] ?: ""
    }

    suspend fun setLastArmTime(time: Long) {
        context.dataStore.edit { prefs ->
            prefs[LAST_ARM_TIME] = time
        }
    }

    suspend fun setStreak(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[STREAK] = value
        }
    }

    suspend fun setFreezeShields(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[FREEZE_SHIELDS] = value
        }
    }

    suspend fun setLastResetDate(dateStr: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_RESET_DATE] = dateStr
        }
    }

    suspend fun setSelectedTheme(index: Int) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_THEME] = index
        }
    }
}
