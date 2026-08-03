package com.therxmv.churros.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.therxmv.churros.core.database.AppDatabase
import com.therxmv.churros.core.database.getDatabaseBuilder
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val DATASTORE_FILENAME = "churros_session.preferences_pb"

@OptIn(ExperimentalForeignApi::class)
actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val dir = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )!!.path!!
                "$dir/$DATASTORE_FILENAME".toPath()
            },
        )
    }
    single<AppDatabase> {
        getDatabaseBuilder(Unit)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
