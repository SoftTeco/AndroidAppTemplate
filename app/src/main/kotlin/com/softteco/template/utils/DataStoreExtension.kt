package com.softteco.template.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T> DataStore<Preferences>.getFromDataStore(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> {
    return data.map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun <T> DataStore<Preferences>.saveToDataStore(
    key: Preferences.Key<T>,
    value: T
) {
    edit { preferences ->
        preferences[key] = value
    }
}
