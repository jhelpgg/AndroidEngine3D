package fr.jhelp.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import fr.jhelp.io.readFull
import fr.jhelp.utilities.extensions.fromBase64
import fr.jhelp.utilities.extensions.fromUtf8
import fr.jhelp.utilities.extensions.toBase64
import fr.jhelp.utilities.extensions.toUtf8
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.RSAKeyGenParameterSpec
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.concurrent.atomic.AtomicBoolean
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

private const val KEY_ALIAS = "JHelpSecureKey"
private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val RSA_MODE = "RSA/ECB/PKCS1Padding"
private const val KEY_SIZE = 512
private const val RSA = "RSA"

object Security
{
    private val initialized = AtomicBoolean(false)
    private lateinit var cipherEncrypt: Cipher
    private lateinit var cipherDecrypt: Cipher

    private fun initialize()
    {
        if (!this.initialized.getAndSet(true))
        {
            try
            {
                val keyStore = keyStore()
                val privateKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
                this.cipherEncrypt = Cipher.getInstance(RSA_MODE)
                this.cipherEncrypt.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)
                this.cipherDecrypt = Cipher.getInstance(RSA_MODE)
                this.cipherDecrypt.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
            }
            catch (exception: Exception)
            {
                throw RuntimeException(exception)
            }
        }
    }

    private fun keyStore() =
        try
        {
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)

            if (!keyStore.containsAlias(KEY_ALIAS))
            {
                val dateStart: Calendar = Calendar.getInstance()
                val dateEnd: Calendar = Calendar.getInstance()
                dateEnd.add(GregorianCalendar.YEAR, 42)

                val algorithm =
                    KeyGenParameterSpec.Builder(KEY_ALIAS,
                                                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN)
                        .setKeySize(KEY_SIZE)
                        .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(KEY_SIZE,
                                                                          RSAKeyGenParameterSpec.F4))
                        .setCertificateSubject(X500Principal("CN=$KEY_ALIAS"))
                        .setCertificateSerialNumber(BigInteger.TEN)
                        .setKeyValidityStart(dateStart.time)
                        .setKeyValidityEnd(dateEnd.time)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()

                val keyPairGenerator = KeyPairGenerator.getInstance(RSA, ANDROID_KEY_STORE)
                keyPairGenerator.initialize(algorithm)
                keyPairGenerator.genKeyPair()
            }

            keyStore
        }
        catch (exception: Exception)
        {
            throw RuntimeException("Can't get Android key store", exception)
        }

    /**
     * Encrypt a text
     */
    fun encrypt(clearText: String): String =
        try
        {
            initialize()
            val byteArrayOutputStream = ByteArrayOutputStream()
            val byteArrayInputStream = ByteArrayInputStream(clearText.toUtf8())
            val buffer = ByteArray(32)
            var read = byteArrayInputStream.readFull(buffer)

            while (read > 0)
            {
                byteArrayOutputStream.write(this.cipherEncrypt.doFinal(buffer, 0, read))
                read = byteArrayInputStream.readFull(buffer)
            }

            byteArrayOutputStream.toByteArray().toBase64()
        }
        catch (exception: Exception)
        {
            throw RuntimeException("Issue while encrypting", exception)
        }

    /**
     * Decrypt a text
     */
    fun decrypt(encryptedText: String): String =
        try
        {
            initialize()
            val byteArrayInputStream = ByteArrayInputStream(encryptedText.fromBase64())
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(64)
            var read = byteArrayInputStream.readFull(buffer)

            while (read > 0)
            {
                byteArrayOutputStream.write(this.cipherDecrypt.doFinal(buffer, 0, read))
                read = byteArrayInputStream.readFull(buffer)
            }

            byteArrayOutputStream.toByteArray().fromUtf8()
        }
        catch (exception: Exception)
        {
            throw RuntimeException("Issue while decrypting", exception)
        }

    /**
     * Encrypt a given [InputStream] and write the result on given [OutputStream]
     *
     * The streams are not closed by the method
     */
    fun encrypt(clearStream: InputStream, encryptedStream: OutputStream)
    {
        try
        {
            initialize()
            val buffer = ByteArray(32)
            var read = clearStream.readFull(buffer)

            while (read > 0)
            {
                encryptedStream.write(this.cipherEncrypt.doFinal(buffer, 0, read))
                read = clearStream.readFull(buffer)
            }

            encryptedStream.flush()
        }
        catch (exception: Exception)
        {
            throw IOException("Issue while encrypting", exception)
        }
    }

    /**
     * Decrypt a given [InputStream] and write the result on given [OutputStream]
     *
     * The streams are not closed by the method
     */
    fun decrypt(encryptedStream: InputStream, clearStream: OutputStream)
    {
        try
        {
            initialize()
            val buffer = ByteArray(64)
            var read = encryptedStream.readFull(buffer)

            while (read > 0)
            {
                clearStream.write(this.cipherDecrypt.doFinal(buffer, 0, read))
                read = encryptedStream.readFull(buffer)
            }

            clearStream.flush()
        }
        catch (exception: Exception)
        {
            throw IOException("Issue while decrypting", exception)
        }
    }
}