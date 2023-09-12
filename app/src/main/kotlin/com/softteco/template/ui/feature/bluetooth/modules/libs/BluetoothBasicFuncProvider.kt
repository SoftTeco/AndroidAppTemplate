package com.softteco.template.ui.feature.bluetooth.modules.libs

import android.os.Build
import com.softteco.template.data.utils.ALL_LETTERS
import com.softteco.template.data.utils.BLUETOOTH_DEVICE_NAME
import com.softteco.template.data.utils.EMPTY_STRING
import com.softteco.template.data.utils.MAX_SIZE_OF_RANDOM_STRING
import com.softteco.template.data.utils.MIN_SIZE_OF_RANDOM_STRING
import com.softteco.template.data.utils.SPACE_STRING
import java.nio.charset.StandardCharsets
import kotlin.streams.asSequence

fun byteArrayToStringWithUTF(byteArray: ByteArray) = String(byteArray, StandardCharsets.UTF_8)

fun stringToByteArrayWithUTF(string: String) = string.toByteArray(StandardCharsets.UTF_8)

fun getBtDeviceName() = BLUETOOTH_DEVICE_NAME.plus(SPACE_STRING).plus(Build.MODEL)

fun generateRandomString(): String {
    val source = ALL_LETTERS
    return java.util.Random().ints(
        (MIN_SIZE_OF_RANDOM_STRING..MAX_SIZE_OF_RANDOM_STRING).random().toLong(),
        0,
        source.length
    )
        .asSequence()
        .map(source::get)
        .joinToString(EMPTY_STRING)
}
