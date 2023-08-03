package com.softteco.template.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountriesItem
    (
    val error: Boolean,
    val msg: String,
    val data: List<DataObject>
) : Parcelable

@Parcelize
data class DataObject(
    val country: String,
) : Parcelable