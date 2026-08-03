package com.therxmv.churros.core.database

import androidx.room.RoomDatabase

/**
 * Returns a platform-specific [RoomDatabase.Builder] for [AppDatabase].
 *
 * @param ctx On Android, pass an [android.content.Context] (typically the application context).
 *            On iOS, the parameter is unused — pass [Unit] or any value.
 */
expect fun getDatabaseBuilder(ctx: Any): RoomDatabase.Builder<AppDatabase>
