package com.luisleyva.gc

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class Menu : AppCompatActivity(),  BiometricManager.Authenticators {

    private var KEY_NAME = "abc"

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btn_atras: Button = findViewById(R.id.btn_atras_menu)
        val btn_gestionar: Button = findViewById(R.id.btn_gestionar)



        btn_atras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_gestionar.setOnClickListener {
            val intent = Intent(this, GenerarPassword::class.java)
            startActivity(intent)
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext,
                    "Authentication error: $errString", Toast.LENGTH_SHORT).show()

            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                val encryptedInfo: ByteArray? = result.cryptoObject?.cipher?.doFinal(
                    KEY_NAME.toByteArray(
                    Charset.defaultCharset()))
                Log.d("MY_APP_TAG", "Encrypted information: " +
                        Arrays.toString(encryptedInfo))
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext,
                    "Autenticacion exitosa!", Toast.LENGTH_SHORT)
                    .show()
            }

//            override fun onAuthenticationSucceeded(
//                result: BiometricPrompt.AuthenticationResult) {
//
//                super.onAuthenticationSucceeded(result)
//                Toast.makeText(applicationContext,
//                    "Autenticacion exitosa!", Toast.LENGTH_SHORT)
//                    .show()
//            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Autenticacion fallida",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("!Bienvenido de nuevo!")
            .setSubtitle("Toque el sensor de huella")
            .setNegativeButtonText("Use su huella o contraseña")
            .build()


        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        val biometricLoginButton = findViewById<ImageButton>(R.id.btn_huella)
        biometricLoginButton.setOnClickListener {
        //Inicia un flujo de trabajo de autenticación biométrica que incorpore un algoritmo de cifrado
            val cipher = getCipher()
            val secretKey = getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            biometricPrompt.authenticate(promptInfo,
                BiometricPrompt.CryptoObject(cipher))

        }

        // Genera una clave que use la siguiente configuración KeyGenParameterSpec
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                generateSecretKey(KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    // Invalidate the keys if the user has registered a new biometric
                    // credential, such as a new fingerprint. Can call this method only
                    // on Android 7.0 (API level 24) or higher. The variable
                    // "invalidatedByBiometricEnrollment" is true by default.
                    .setInvalidatedByBiometricEnrollment(true)
                    .build())
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private  fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(keyGenParameterSpec)
        }
        keyGenerator.generateKey()
    }

    private  fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }

}