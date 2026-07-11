package com.therxmv.churros

import android.app.Application
import com.therxmv.churros.core.di.appModule
import com.therxmv.churros.core.logger.initLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChurrosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
        startKoin {
            androidContext(this@ChurrosApplication)
            modules(appModule)
        }
    }
}
