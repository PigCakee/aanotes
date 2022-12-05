package com.arton.aanotes.domain.repo

import android.content.Context
import com.arton.aanotes.common.utils.CryptoManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val cryptoManager: CryptoManager,
) {

    companion object {
        private const val PIN_FILE_NAME = "pinCode.txt"
    }

    fun isPinCorrect(context: Context, pinCode: String): Boolean {
        val file = File(context.filesDir, PIN_FILE_NAME)
        if (!file.exists()) {
            return false
        }
        val fis = FileInputStream(file)
        return cryptoManager.decrypt(fis).decodeToString() == pinCode
    }

    fun createOrUpdatePin(context: Context, pinCode: String) {
        val file = File(context.filesDir, PIN_FILE_NAME)
        clearPin(context)
        file.createNewFile()
        val fos = FileOutputStream(file)
        cryptoManager.encrypt(bytes = pinCode.encodeToByteArray(), fos)
    }

    fun doesPinExist(context: Context) = File(context.filesDir, PIN_FILE_NAME).exists()

    fun clearPin(context: Context) {
        val file = File(context.filesDir, PIN_FILE_NAME)
        file.delete()
    }
}