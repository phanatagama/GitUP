package com.github.gituser.data.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.github.gituser.BuildConfig
import com.github.gituser.ui.common.SettingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    fun provideDatastore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create (produceFile = {context.preferencesDataStoreFile(BuildConfig.USER_PREFERENCE)})
    }

    @Singleton
    @Provides
    fun provideSettingPref(dataStore: DataStore<Preferences>): SettingPreferences {
        return SettingPreferences(dataStore)
    }

}