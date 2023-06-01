package com.softteco.template.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.softteco.template.data.source.local.model.AccountEntity
import com.softteco.template.domain.model.user.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item: AccountEntity)

    @Query("SELECT * from AccountEntity")
    fun getAccount(): Flow<AccountEntity>
}