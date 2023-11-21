package com.softteco.template.utils

import androidx.datastore.core.Serializer
import com.softteco.template.data.profile.dto.CreateUserDto
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class UserSettingsSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<CreateUserDto> {

    override val defaultValue: CreateUserDto
        get() = CreateUserDto("", "", "")

    override suspend fun readFrom(input: InputStream): CreateUserDto {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = CreateUserDto.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e("Error reading from encrypted data store", e)
            defaultValue
        }
    }

    override suspend fun writeTo(t: CreateUserDto, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = CreateUserDto.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
