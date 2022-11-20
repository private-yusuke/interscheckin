package pub.yusuke.interscheckin.repositories.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

/**
 * SettingsPreferences を暗号化して保存するための Serializer
 */
class SettingsPreferencesSerializer(
    private val aead: Aead
) : Serializer<SettingsPreferences> {
    override suspend fun readFrom(input: InputStream): SettingsPreferences {
        return try {
            val encryptedInput = input.readBytes()

            val decryptedInput = if (encryptedInput.isNotEmpty()) {
                aead.decrypt(encryptedInput, null)
            } else {
                encryptedInput
            }

            ProtoBuf.decodeFromByteArray(SettingsPreferences.serializer(), decryptedInput)
        } catch (e: SerializationException) {
            throw CorruptionException("", e)
        }
    }

    override suspend fun writeTo(t: SettingsPreferences, output: OutputStream) {
        val byteArray = ProtoBuf.encodeToByteArray(SettingsPreferences.serializer(), t)
        val encryptedBytes = aead.encrypt(byteArray, null)

        output.write(encryptedBytes)
    }

    override val defaultValue: SettingsPreferences =
        SettingsPreferences()
}
