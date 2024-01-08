package com.github.core.data.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.github.core.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences(private val context: Context) {
    private val themeKey = booleanPreferencesKey("themeSetting")
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
    produceNewData = { emptyPreferences() }
    ), produceFile = { context.preferencesDataStoreFile(BuildConfig.USER_PREFERENCE) })

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }
}