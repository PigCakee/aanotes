package com.arton.aanotes.domain.room

import android.content.Context
import com.arton.aanotes.common.utils.CryptoManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

class DatabasePasswordManager @Inject constructor(private val cryptoManager: CryptoManager) {

    companion object {
        private const val PASSWORD_LENGTH = 10
    }

    fun getOrCreatePassword(context: Context): CharArray {
        val file = File(context.filesDir, "dbPassword.txt")
        if (!file.exists()) {
            file.createNewFile()
            val fos = FileOutputStream(file)
            cryptoManager.encrypt(bytes = getRandomString().encodeToByteArray(), fos)
        }
        val fis = FileInputStream(file)
        return cryptoManager.decrypt(fis)
            .decodeToString()
            .toCharArray()
    }

    private fun getRandomString() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..PASSWORD_LENGTH)
            .map { allowedChars.random() }
            .joinToString("")
    }
}