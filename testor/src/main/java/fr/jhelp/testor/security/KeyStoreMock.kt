/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.testor.security

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.cert.Certificate

private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val KEY_SIZE = 512
private const val RSA = "RSA"

private val keyStoreAlias = HashMap<String, KeyStore.PrivateKeyEntry>()

/**
 * Start mocking key store to have an emulated Android Key Store
 */
fun mockKeyStore()
{
    mockkStatic(KeyStore::class)
    val keystoreMock = mockk<KeyStore>()
    every { KeyStore.getInstance(ANDROID_KEY_STORE) } returns keystoreMock
    every { keystoreMock.load(any()) } returns Unit
    every { keystoreMock.containsAlias(any()) } returns true

    every { keystoreMock.getEntry(any(), any()) } answers { call ->
        val alias = call.invocation.args[0] as String

        keyStoreAlias.getOrPut(alias) {
            val generator = KeyPairGenerator.getInstance(RSA)
            generator.initialize(KEY_SIZE)
            val keyPair = generator.generateKeyPair()
            val certificateMock = mockk<Certificate>()
            every { certificateMock.publicKey } returns keyPair.public
            val privateKeyEntryMock = mockk<KeyStore.PrivateKeyEntry>()
            every { privateKeyEntryMock.certificate } returns certificateMock
            every { privateKeyEntryMock.privateKey } returns keyPair.private
            privateKeyEntryMock
        }
    }
}

