package com.softteco.template.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.softteco.template.data.source.local.model.ApiEntryEntity

/**
 * Database to CACHE data from a remote service.
 */
@Database(
    entities = [ApiEntryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class EntryDatabase : RoomDatabase() {

    abstract fun apiEntryDao(): ApiEntryDao

    companion object {
        fun create(applicationContext: Context): EntryDatabase {
            return Room.databaseBuilder(
                applicationContext,
                EntryDatabase::class.java,
                "db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
