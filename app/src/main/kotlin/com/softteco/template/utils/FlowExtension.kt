package com.softteco.template.utils

import kotlinx.coroutines.flow.Flow

@Suppress("MagicNumber")
inline fun <R> combine(
    vararg flows: Flow<*>,
    crossinline transform: suspend () -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(*flows) { _ ->
        transform()
    }
}
