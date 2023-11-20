package com.softteco.template.data.profile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity
data class Profile(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    val createdAt: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: String? = null,
    val country: String? = null,
    val avatar: String? = null,
) {
    companion object {
        private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        fun Profile.toJson(): String = moshi.adapter(Profile::class.java).toJson(this)

        fun fromJson(json: String): Profile {
            return moshi.adapter(Profile::class.java).fromJson(json)!!
        }
    }

    fun fullName(): String {
        return StringBuilder()
            .append(firstName ?: "")
            .append(" ")
            .append(lastName ?: "")
            .toString()
            .ifBlank { "" }
    }
}
