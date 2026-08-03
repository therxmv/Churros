package com.therxmv.churros.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val DB_FILE_NAME = "churros.db"

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNUSED_PARAMETER")
actual fun getDatabaseBuilder(ctx: Any): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!.path!! + "/$DB_FILE_NAME"
    return Room.databaseBuilder<AppDatabase>(name = dbFilePath)
}
