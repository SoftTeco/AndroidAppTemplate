package com.softteco.template.utils

import android.graphics.Color
import com.softteco.template.Constants
import com.softteco.template.Constants.BIT_SHIFT_VALUE
import java.util.Random

@OptIn(ExperimentalUnsignedTypes::class)
fun characteristicByteConversation(bytes: ByteArray, startIndex: Int, endIndex: Int): Double {
    val array = bytes.copyOfRange(startIndex, endIndex).toUByteArray()
    var result = 0
    for (i in array.indices) {
        result = result or (array[i].toInt() shl BIT_SHIFT_VALUE * i)
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
