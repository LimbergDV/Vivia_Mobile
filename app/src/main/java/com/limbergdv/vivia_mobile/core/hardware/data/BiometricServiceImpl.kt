package com.limbergdv.vivia_mobile.core.hardware.data

import android.content.Context
import android.util.Log
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
            val rootObject = org.json.JSONObject(challengeJson)

            // 1. Extraemos el objeto interior si viene envuelto en "publicKey"
            val optionsObject = if (rootObject.has("publicKey")) {
                rootObject.getJSONObject("publicKey")
            } else {
                rootObject
            }

            // 2. Parche de Dominio (rpId)
            if (optionsObject.has("rp")) {
                val rpObject = optionsObject.getJSONObject("rp")
                if (rpObject.optString("id") == "localhost" || rpObject.optString("id").isEmpty()) {
                    rpObject.put("id", "vivia.aleosh.online")
                }
            }

            // 3. Parche de Usuario (name)
            if (optionsObject.has("user")) {
                val userObject = optionsObject.getJSONObject("user")
                if (!userObject.has("name")) {
                    userObject.put("name", "Arrendador Vivia")
                }
            }

            // --- NUEVO PARCHE: Forzar creación de Passkey ---
            // Le decimos a Android que ES OBLIGATORIO guardar la huella en el dispositivo (Resident Key)
            val authSelection = optionsObject.optJSONObject("authenticatorSelection") ?: org.json.JSONObject()
            authSelection.put("residentKey", "required")
            authSelection.put("requireResidentKey", true)
            authSelection.put("authenticatorAttachment", "platform") // Exige que sea la huella/rostro del celular
            optionsObject.put("authenticatorSelection", authSelection)
            // ------------------------------------------------

            // 4. Convertimos a String
            val fixedChallengeJson = optionsObject.toString()
            Log.d("VIVIA_AUTH_DEBUG", "JSON de Registro enviado a Android: $fixedChallengeJson")

            // 5. Invocamos el lector de Android
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
            val rootObject = org.json.JSONObject(challengeJson)

            // 1. Extraemos el objeto interior
            val optionsObject = if (rootObject.has("publicKey")) {
                rootObject.getJSONObject("publicKey")
            } else {
                rootObject
            }

            // 2. PARCHE PARA LOGIN: Inyectamos a la fuerza el rpId.
            // Como el backend solo manda el 'challenge', Android necesita
            // obligatoriamente el rpId para buscar la huella en su bóveda.
            optionsObject.put("rpId", "vivia.aleosh.online")

            // 3. Convertimos a texto
            val fixedChallengeJson = optionsObject.toString()

            // ---- AGREGA ESTA LÍNEA ----
            Log.d("VIVIA_AUTH_DEBUG", "JSON de Login enviado a Android: $fixedChallengeJson")
            // ---------------------------

            // 4. Invocamos el lector de Android
            val option = GetPublicKeyCredentialOption(requestJson = fixedChallengeJson)
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