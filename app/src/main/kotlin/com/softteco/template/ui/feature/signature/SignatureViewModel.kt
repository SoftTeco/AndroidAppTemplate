package com.softteco.template.ui.feature.signature

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.utils.SIGNATURE_NAME
import com.softteco.template.utils.SignatureStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val JPG = ".jpg"
private const val SVG = ".svg"

@HiltViewModel
class SignatureViewModel @Inject constructor(
    private val storeManager: SignatureStorageManager,
) : ViewModel() {

    private val _signatureFilePath = MutableStateFlow("")
    val signatureFilePath = _signatureFilePath.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _signatureFilePath.value = storeManager.getSignatureFilePath()
        }
    }

    fun saveSignatureAsJPG(bitmap: Bitmap) = viewModelScope.launch(Dispatchers.IO) {
        cleanCurrentSignature()
        val newFileName = buildSignatureName(extension = JPG)
        val filePath = storeManager.saveSignatureAsJPG(bitmap, newFileName)
        _signatureFilePath.value = filePath
    }

    private fun buildSignatureName(extension: String): String =
        SIGNATURE_NAME + System.currentTimeMillis() + extension

    fun saveSignatureAsSvg(svg: String) = viewModelScope.launch(Dispatchers.IO) {
        cleanCurrentSignature()
        val newFileName = buildSignatureName(extension = SVG)
        val filePath = storeManager.saveSignatureAsSvg(svg, newFileName)
        _signatureFilePath.value = filePath
    }

    private suspend fun cleanCurrentSignature() {
        val signaturePath = signatureFilePath.first()
        if (signaturePath.isNotEmpty()) deleteFile(signaturePath)
    }

    fun deleteSignature(filePath: String) = viewModelScope.launch {
        deleteFile(filePath)
    }

    private suspend fun deleteFile(filePath: String) {
        withContext(Dispatchers.IO) {
            if (filePath.isNotEmpty()) {
                storeManager.deleteFile(filePath)
                _signatureFilePath.value = ""
            }
        }
    }
}
