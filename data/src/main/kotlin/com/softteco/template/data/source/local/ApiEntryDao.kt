package com.softteco.template.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.softteco.template.data.source.local.model.ApiEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiEntryDao {

    @Insert
    suspend fun addAll(items: List<ApiEntryEntity>)

    @Query("DELETE from ApiEntryEntity")
    suspend fun deleteAll()

    @Query("SELECT * from ApiEntryEntity")
    fun allApiEntries(): Flow<List<ApiEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(entry: ApiEntryEntity)

    @Query("SELECT * from ApiEntryEntity WHERE name = :name")
    fun apiEntry(name: String): Flow<ApiEntryEntity?>

    @Query("SELECT * from ApiEntryEntity WHERE favorite = 1")
    fun favourites(): Flow<List<ApiEntryEntity>>

    @Transaction
    suspend fun replaceDataset(newEntries: List<ApiEntryEntity>) {
        deleteAll()
        addAll(newEntries)
    }
}
