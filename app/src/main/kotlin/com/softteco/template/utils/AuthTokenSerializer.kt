package com.softteco.template.utils

import androidx.datastore.core.Serializer
import com.softteco.template.data.profile.dto.AuthTokenDto
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class AuthTokenSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<AuthTokenDto> {
    override val defaultValue: AuthTokenDto
        get() = AuthTokenDto("")

    override suspend fun readFrom(input: InputStream): AuthTokenDto {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = AuthTokenDto.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e("Error reading from encrypted data store", e)
            defaultValue
        }
    }

    override suspend fun writeTo(t: AuthTokenDto, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = AuthTokenDto.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}
