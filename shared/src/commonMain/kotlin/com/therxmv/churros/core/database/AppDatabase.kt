package com.therxmv.churros.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.therxmv.churros.feature.chores.data.local.ChoreDao
import com.therxmv.churros.feature.chores.data.local.ChoreEntity
import com.therxmv.churros.feature.family.data.local.FamilyMemberDao
import com.therxmv.churros.feature.family.data.local.FamilyMemberEntity
import com.therxmv.churros.feature.family.data.local.HouseholdDao
import com.therxmv.churros.feature.family.data.local.HouseholdEntity

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [
        ChoreEntity::class,
        HouseholdEntity::class,
        FamilyMemberEntity::class,
    ],
    version = 3,
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun choreDao(): ChoreDao

    abstract fun householdDao(): HouseholdDao

    abstract fun familyMemberDao(): FamilyMemberDao
}
