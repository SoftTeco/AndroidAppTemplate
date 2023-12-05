package com.softteco.template.ui.feature.openSourceLicenses

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.util.Locale
import javax.inject.Inject

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
            val licensesData = context.resources
                .openRawResource(R.raw.third_party_licenses)
                .readBytes()
            val licensesMetadataReader = context.resources
                .openRawResource(R.raw.third_party_license_metadata)
                .bufferedReader()

            _licensesList.value =
                licensesMetadataReader.use { reader -> reader.readLines() }.map { line ->
                    val (section, name) = line.split(" ", limit = 2)
                    val (startOffset, length) = section.split(":", limit = 2).map(String::toInt)
                    val licenseData =
                        licensesData.sliceArray(startOffset until startOffset + length)
                    val licenseText = licenseData.toString(Charset.forName("UTF-8"))
                    LibraryItem(name, licenseText)
                }.sortedBy { item -> item.name.lowercase(Locale.ROOT) }
        }
    }
}
