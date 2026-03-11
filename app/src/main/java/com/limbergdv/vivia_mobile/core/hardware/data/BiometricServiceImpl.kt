package com.limbergdv.vivia_mobile.core.hardware.data

import android.content.Context
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.limbergdv.vivia_mobile.core.hardware.domain.BiometricService
import org.json.JSONObject
import javax.inject.Inject

class BiometricServiceImpl @Inject constructor(
    private val credentialManager: CredentialManager
) : BiometricService {

    override suspend fun registerBiometric(context: Context, challengeJson: String): Result<String> {
        return try {
            val rootObject = JSONObject(challengeJson)

            // 1. Extraemos el objeto interior si viene envuelto en "publicKey"
            val optionsObject = if (rootObject.has("publicKey")) {
                rootObject.getJSONObject("publicKey")
            } else {
                rootObject
            }

            // 2. Verificamos que el bloque user tenga el atributo "name"
            if (optionsObject.has("rp")) {
                val rpObject = optionsObject.getJSONObject("rp")
                if (rpObject.optString("id") == "localhost" || rpObject.optString("id").isEmpty()) {
                    // Cambia esto por el dominio real donde estará alojado tu backend
                    rpObject.put("id", "vivia.aleosh.online")
                }
            }

            if (optionsObject.has("user")) {
                val userObject = optionsObject.getJSONObject("user")
                if (!userObject.has("name")) {
                    userObject.put("name", "Arrendador Vivia")
                }
            }

            // 3. Convertimos el objeto desenvuelto y corregido a String
            val fixedChallengeJson = optionsObject.toString()

            // 4. Pasamos el JSON limpio a Android
            val request = CreatePublicKeyCredentialRequest(requestJson = fixedChallengeJson)
            val response = credentialManager.createCredential(context, request)

            if (response is CreatePublicKeyCredentialResponse) {
                Result.success(response.registrationResponseJson)
            } else {
                Result.failure(Exception("Formato de credencial biométrica no soportado."))
            }
        } catch (e: CreateCredentialException) {
            Result.failure(Exception("Registro cancelado o fallido: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun authenticateBiometric(context: Context, challengeJson: String): Result<String> {
        return try {
            val option = GetPublicKeyCredentialOption(requestJson = challengeJson)
            val request = GetCredentialRequest(listOf(option))

            val response = credentialManager.getCredential(context, request)
            val credential = response.credential

            if (credential is PublicKeyCredential) {
                Result.success(credential.authenticationResponseJson)
            } else {
                Result.failure(Exception("Credencial devuelta no es válida."))
            }
        } catch (e: GetCredentialException) {
            Result.failure(Exception("Autenticación biométrica cancelada o fallida: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}