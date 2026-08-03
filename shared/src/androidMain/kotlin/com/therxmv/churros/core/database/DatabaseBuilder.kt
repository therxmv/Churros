package com.therxmv.churros.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(ctx: Any): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx as Context
    val dbFile = appContext.getDatabasePath("churros.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
