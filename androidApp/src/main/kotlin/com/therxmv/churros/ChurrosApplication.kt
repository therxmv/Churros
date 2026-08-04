package com.therxmv.churros

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import com.therxmv.churros.core.di.appModule
import com.therxmv.churros.core.logger.initLogger
import com.therxmv.churros.di.credentialsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChurrosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        startKoin {
            androidContext(this@ChurrosApplication)
            modules(credentialsModule, appModule)
        }
    }

    /**
     * Reapplies the stored per-app locale on every cold start.
     *
     * On API 33+ the system [android.app.LocaleManager] handles persistence
     * automatically. On API 24–32 AppCompat reads its SharedPreferences storage
     * and we need to build the overridden [Configuration] context manually so
     * that string resources resolve to the correct locale from the very first
     * frame.
     */
    override fun attachBaseContext(base: Context) {
        val storedLocales = AppCompatDelegate.getApplicationLocales()
        if (storedLocales.isEmpty) {
            super.attachBaseContext(base)
        } else {
            val config = Configuration(base.resources.configuration)
            ConfigurationCompat.setLocales(config, storedLocales)
            super.attachBaseContext(base.createConfigurationContext(config))
        }
    }
}
