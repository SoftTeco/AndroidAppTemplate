package com.softteco.template.utils

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

const val SIGNATURE_NAME = "Signature"

class SignatureStorageManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun saveSignatureAsJPG(bitmap: Bitmap, fileName: String): String {
        val outputFile = File(context.cacheDir, fileName)
        bitmapToFile(outputFile, bitmap)
        return outputFile.path
    }

    fun saveSignatureAsSvg(svg: String, fileName: String): String {
        val outputFile = File(context.cacheDir, fileName)
        svg.byteInputStream().use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }
        return outputFile.path
    }

    private fun bitmapToFile(
        file: File,
        bitmap: Bitmap,
        quality: Int = 100
    ): File {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        FileOutputStream(file).use { fileOutPutStream ->
            byteArrayOutputStream.use {
                val bitMapData = it.toByteArray()
                fileOutPutStream.write(bitMapData)
            }
        }
        return file
    }

    fun getSignatureFilePath(): String {
        val files = context.cacheDir.listFiles()
        return files?.firstOrNull { it.name.startsWith(SIGNATURE_NAME) }?.path ?: ""
    }

    fun deleteFile(path: String) {
        val file = File(path)
        if (file.exists()) file.delete()
    }
}
