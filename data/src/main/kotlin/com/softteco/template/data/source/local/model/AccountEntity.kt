package com.softteco.template.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.softteco.template.domain.model.user.Account

@Entity
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val country: String,
    val birthday: String,
    val avatar: String
)
     {
    companion object {
        internal fun fromAccount(account: Account): AccountEntity = AccountEntity(
            id = account.id,
            firstName = account.firstName,
            lastName = account.lastName,
            country = account.country,
            birthday = account.birthday,
            email = account.email,
            password = account.password,
            avatar = account.avatar
        )
    }
}

internal fun AccountEntity.toDomainModel() = Account(
    id = id,
    firstName = firstName,
    lastName = lastName,
    country = country,
    birthday = birthday,
    email = email,
    password = password,
    avatar = avatar
)
