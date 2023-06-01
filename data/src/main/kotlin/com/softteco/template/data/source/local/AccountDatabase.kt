package com.softteco.template.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.softteco.template.data.source.local.model.AccountEntity


@Database(
    entities = [AccountEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    companion object {
        fun create(applicationContext: Context): AccountDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AccountDatabase::class.java,
                "db_user"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}