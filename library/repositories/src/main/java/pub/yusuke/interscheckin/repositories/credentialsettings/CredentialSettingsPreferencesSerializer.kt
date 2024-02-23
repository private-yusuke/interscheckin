package pub.yusuke.interscheckin.repositories.credentialsettings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

/**
 * [CredentialSettingsPreferences] を暗号化して保存するための Serializer
 */
class CredentialSettingsPreferencesSerializer(
    private val aead: Aead,
) : Serializer<CredentialSettingsPreferences> {
    @ExperimentalSerializationApi
    override suspend fun readFrom(input: InputStream): CredentialSettingsPreferences {
        return try {
            val encryptedInput = input.readBytes()

            val decryptedInput = if (encryptedInput.isNotEmpty()) {
                aead.decrypt(encryptedInput, null)
            } else {
                encryptedInput
            }

            ProtoBuf.decodeFromByteArray(CredentialSettingsPreferences.serializer(), decryptedInput)
        } catch (e: SerializationException) {
            throw CorruptionException("", e)
        }
    }

    @ExperimentalSerializationApi
    override suspend fun writeTo(t: CredentialSettingsPreferences, output: OutputStream) {
        val byteArray = ProtoBuf.encodeToByteArray(CredentialSettingsPreferences.serializer(), t)
        val encryptedBytes = aead.encrypt(byteArray, null)

        withContext(Dispatchers.IO) {
            output.write(encryptedBytes)
        }
    }

    override val defaultValue: CredentialSettingsPreferences =
        CredentialSettingsPreferences()
}
