package com.softteco.template.ui.feature.openSourceLicenses

import android.app.Application
import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

private const val LIMIT = 2

@HiltViewModel
class OpenSourceLicensesViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    application: Application
) : AndroidViewModel(application) {

    private val _licensesList = MutableStateFlow(emptyList<LibraryItem>())
    val licensesList: StateFlow<List<LibraryItem>> = _licensesList.asStateFlow()

    init {
        loadLicenses(application)
    }

    private fun loadLicenses(context: Context) {
        viewModelScope.launch(appDispatchers.io) {
            try {
                val licensesData = context.resources
                    .openRawResource(R.raw.third_party_licenses)
                    .readBytes()
                val licensesMetadataReader = context.resources
                    .openRawResource(R.raw.third_party_license_metadata)
                    .bufferedReader()

                _licensesList.value =
                    licensesMetadataReader.use { reader -> reader.readLines() }.map { line ->
                        val (section, name) = line.split(" ", limit = LIMIT)
                        val (startOffset, length) = section.split(":", limit = LIMIT)
                            .map(String::toInt)
                        val licenseData =
                            licensesData.sliceArray(startOffset until startOffset + length)
                        val licenseText = licenseData.toString(Charsets.UTF_8)
                        LibraryItem(name, licenseText)
                    }.sortedBy { item -> item.name.lowercase(Locale.ROOT) }
            } catch (e: NotFoundException) {
                Timber.e("Error reading from resources", e)
                _licensesList.value = emptyList()
            }
        }
    }
}
