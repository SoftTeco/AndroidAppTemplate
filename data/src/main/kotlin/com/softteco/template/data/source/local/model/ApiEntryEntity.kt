package com.softteco.template.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.softteco.template.domain.model.ApiEntry

@Entity
data class ApiEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val auth: String,
    val category: String,
    val cors: String,
    val description: String,
    val https: Boolean,
    val link: String,
    val logo: String,
    val favorite: Boolean
) {
    companion object {
        internal fun fromApiEntry(apiEntry: ApiEntry): ApiEntryEntity = ApiEntryEntity(
            id = apiEntry.id,
            name = apiEntry.name,
            auth = apiEntry.auth,
            category = apiEntry.category,
            cors = apiEntry.cors,
            description = apiEntry.description,
            https = apiEntry.https,
            link = apiEntry.link,
            logo = "https://api.dicebear.com/5.x/icons/svg?seed=${apiEntry.name}",
            favorite = apiEntry.favorite,
        )
    }
}

internal fun ApiEntryEntity.toDomainModel() = ApiEntry(
    id = id,
    name = name,
    auth = auth,
    category = category,
    cors = cors,
    description = description,
    https = https,
    link = link,
    logo = "https://api.dicebear.com/5.x/icons/svg?seed=$name",
    favorite = favorite,
)
