package pub.yusuke.securedatastore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import java.security.AlgorithmParameters
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureDataStoreUtil {
    private val provider = "AndroidKeyStore"
    private val cipher by lazy {
        Cipher.getInstance("AES/GCM/NoPadding")
    }
    private val charset by lazy {
        charset("UTF-8")
    }
    private val keyStore by lazy {
        KeyStore.getInstance(provider).apply {
            load(null)
        }
    }
    private val keyGenerator by lazy {
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, provider)
    }

    fun encryptData(keyAlias: String, text: String): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(keyAlias))
        return cipher.doFinal(text.toByteArray(charset))
    }

    fun decryptData(keyAlias: String, encryptedData: ByteArray): String {
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(keyAlias), GCMParameterSpec(128, cipher.iv))
        return cipher.doFinal(encryptedData).toString(charset)
    }

    private fun generateSecretKey(keyAlias: String): SecretKey {
        return keyGenerator.apply {
            init(
                KeyGenParameterSpec
                    .Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        }.generateKey()
    }

    private fun getSecretKey(keyAlias: String) =
        (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
}

suspend inline fun DataStore<Preferences>.secureEdit(
    value: String,
    keyAlias: String = "interscheckin",
    crossinline editStore: (MutablePreferences, String) -> Unit
) {
    val security = SecureDataStoreUtil()
    edit {
        val encryptedValue = security.encryptData(keyAlias, value)
        editStore.invoke(it, encryptedValue)
    }
}