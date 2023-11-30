package com.softteco.template.utils

import android.graphics.Color
import com.softteco.template.Constants
import java.util.Random

@OptIn(ExperimentalUnsignedTypes::class)
fun characteristicByteConversation(bytes: ByteArray, startIndex: Int, endIndex: Int): Double {
    val array = bytes.copyOfRange(startIndex, endIndex).toUByteArray()
    var result = 0
    for (i in array.indices) {
        result = result or (array[i].toInt() shl 8 * i)
    }
    return result.toDouble()
}

fun generateRandomColor(): Int {
    val rnd = Random()
    return Color.argb(
        Constants.ALPHA_COLOR_VALUE,
        rnd.nextInt(Constants.BOUND_COLOR_VALUE),
        rnd.nextInt(Constants.BOUND_COLOR_VALUE),
        rnd.nextInt(Constants.BOUND_COLOR_VALUE)
    )
}
