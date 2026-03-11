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
import javax.inject.Inject

class BiometricServiceImpl @Inject constructor(
    private val credentialManager: CredentialManager
) : BiometricService {

    override suspend fun registerBiometric(context: Context, challengeJson: String): Result<String> {
        return try {
            // Preparamos la petición WebAuthn con el desafío del servidor
            val request = CreatePublicKeyCredentialRequest(requestJson = challengeJson)

            // Invocamos el hardware. Esto pausa la corrutina y muestra el UI nativo de huella.
            val response = credentialManager.createCredential(context, request)

            // Extraemos la respuesta para el servidor
            if (response is CreatePublicKeyCredentialResponse) {
                Result.success(response.registrationResponseJson)
            } else {
                Result.failure(Exception("Formato de credencial biométrica no soportado."))
            }
        } catch (e: CreateCredentialException) {
            Result.failure(Exception("Registro biométrico cancelado o fallido: ${e.message}"))
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