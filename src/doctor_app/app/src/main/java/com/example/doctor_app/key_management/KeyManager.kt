package com.example.doctor_app.key_management

import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

class KeyManager {
    private val STRING_LENGTH = 16;
    private val charPool = charArrayOf(
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'x',
        'y',
        'z',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        '0'
    );

    public fun generateKey() : String {
        val randomString = (1..STRING_LENGTH).map { i -> kotlin.random.Random.nextInt(
            0,
            charPool.size
        ) }.map(charPool::get).joinToString("");

        val encoded = Base64.getEncoder().encode(randomString.toByteArray())
        return String(encoded)
    }

    @Throws(Exception::class)
    private fun generateKey(secret: String): Key? {
        val decoded = Base64.getDecoder().decode(secret.toByteArray())
        return SecretKeySpec(decoded, "AES")
    }



    public fun signData(strToEncrypt: String, secret_key: String) : String {

        val key: Key = generateKey(secret_key)!!
        val c = Cipher.getInstance("AES")
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(strToEncrypt.toByteArray())
        return Base64.getEncoder().encodeToString(encVal)



//        Security.addProvider(BouncyCastleProvider())
//        var keyBytes: ByteArray
//
//        try {
//            keyBytes = secret_key.toByteArray(charset("UTF8"))
//            val skey = SecretKeySpec(keyBytes, "AES")
//            val input = strToEncrypt.toByteArray(charset("UTF8"))
//
//            synchronized(Cipher::class.java) {
//                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
//                cipher.init(Cipher.ENCRYPT_MODE, skey)
//
//                val cipherText = ByteArray(cipher.getOutputSize(input.size))
//                var ctLength = cipher.update(
//                    input, 0, input.size,
//                    cipherText, 0
//                )
//                ctLength += cipher.doFinal(cipherText, ctLength)
//
//                return Base64.getEncoder().encodeToString(cipherText)
//            }
//        } catch (uee: UnsupportedEncodingException) {
//            uee.printStackTrace()
//        } catch (ibse: IllegalBlockSizeException) {
//            ibse.printStackTrace()
//        } catch (bpe: BadPaddingException) {
//            bpe.printStackTrace()
//        } catch (ike: InvalidKeyException) {
//            ike.printStackTrace()
//        } catch (nspe: NoSuchPaddingException) {
//            nspe.printStackTrace()
//        } catch (nsae: NoSuchAlgorithmException) {
//            nsae.printStackTrace()
//        } catch (e: ShortBufferException) {
//            e.printStackTrace()
//        }
//
//        return "";
    }
}