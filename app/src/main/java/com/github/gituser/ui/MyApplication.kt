package com.github.gituser.ui

import android.app.Application
import com.github.core.data.user.databaseModule
import com.github.core.di.networkModule
import com.github.core.di.useCaseModule
import com.github.gituser.ui.common.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(listOf(networkModule, databaseModule, viewModelModule, useCaseModule))
        }
    }
}