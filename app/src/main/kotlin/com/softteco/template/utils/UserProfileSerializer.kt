package com.softteco.template.utils

import androidx.datastore.core.Serializer
import com.softteco.template.data.profile.dto.ProfileDto
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class UserProfileSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<ProfileDto> {
    override val defaultValue: ProfileDto
        get() = ProfileDto(0, "", "", "", null, null, null, null)

    override suspend fun readFrom(input: InputStream): ProfileDto {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = ProfileDto.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e("Error reading from encrypted data store", e)
            defaultValue
        }
    }

    override suspend fun writeTo(t: ProfileDto, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = ProfileDto.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
