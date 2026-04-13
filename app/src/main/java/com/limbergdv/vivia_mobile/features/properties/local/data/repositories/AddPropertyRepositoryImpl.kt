package com.limbergdv.vivia_mobile.features.properties.local.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.local.mapper.toDraftEntity
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.local.mapper.toUiState
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.api.AddPropertyApi
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.mappers.toRequest
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.local.domain.repositories.AddPropertyRepository
import com.limbergdv.vivia_mobile.features.properties.local.presentation.screens.AddPropertyUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class AddPropertyRepositoryImpl @Inject constructor(
    private val draftDao: PropertyDraftDao,
    private val api: AddPropertyApi,
    @ApplicationContext private val context: Context
) : AddPropertyRepository {


    override fun getDraft(): Flow<AddPropertyUiState?> =
        draftDao.getDraft().map { it?.toUiState() }

    override suspend fun saveDraft(uiState: AddPropertyUiState) {
        draftDao.saveDraft(uiState.toDraftEntity())
    }

    override suspend fun clearDraft() {
        draftDao.clearDraft()
    }


    override suspend fun createProperty(property: Property): Property {

        Log.d("VIVIA_PROPERTY_DEBUG", "=== Paso 1: Enviando JSON a POST /properties ===")

        val request = property.toRequest()
        Log.d("VIVIA_PROPERTY_DEBUG", "Request: $request")

        val createResponse = api.createProperty(request)
        Log.d("VIVIA_PROPERTY_DEBUG", "Response success: ${createResponse.success}")
        Log.d("VIVIA_PROPERTY_DEBUG", "Response message: ${createResponse.message}")

        if (!createResponse.success || createResponse.data == null) {
            throw Exception(createResponse.message ?: "Error al crear la propiedad")
        }

        val createdProperty = createResponse.data.toDomain()
        val propertyId = createdProperty.id

        Log.d("VIVIA_PROPERTY_DEBUG", "Propiedad creada con id: '$propertyId'")
        Log.d("VIVIA_PROPERTY_DEBUG", "¿propertyId está en blanco? ${propertyId.isBlank()}")

        if (property.imageUris.isNotEmpty() && propertyId.isNotBlank()) {
            Log.d("VIVIA_PROPERTY_DEBUG", "=== Paso 2: Subiendo ${property.imageUris.size} imágenes a /properties/$propertyId/images ===")

            val imageParts = property.imageUris.mapNotNull { uriString ->
                uriToMultipart(uriString)
            }

            Log.d("VIVIA_PROPERTY_DEBUG", "Parts generados: ${imageParts.size}")

            if (imageParts.isNotEmpty()) {
                val imageResponse = api.uploadImages(
                    propertyId = propertyId,
                    images     = imageParts
                )

                Log.d("VIVIA_PROPERTY_DEBUG", "HTTP code: ${imageResponse.code()}")

                if (imageResponse.isSuccessful) {
                    val body = imageResponse.body()
                    Log.d("VIVIA_PROPERTY_DEBUG", "Imágenes subidas exitosamente: ${body?.success}")
                    Log.d("VIVIA_PROPERTY_DEBUG", "Message: ${body?.message}")

                    if (body?.success == true && body.data != null) {
                        return body.data.toDomain()
                    }
                } else {
                    val errorBody = imageResponse.errorBody()?.string()
                    Log.e("VIVIA_PROPERTY_DEBUG", "Error HTTP ${imageResponse.code()} subiendo imágenes")
                    Log.e("VIVIA_PROPERTY_DEBUG", "Error body: $errorBody")
                }
            }
        }

        return createdProperty
    }


    private fun uriToMultipart(uriString: String): MultipartBody.Part? {
        return try {
            val uri = Uri.parse(uriString)
            val stream = context.contentResolver.openInputStream(uri) ?: return null
            val originalBytes = stream.readBytes()
            stream.close()

            Log.d("VIVIA_PROPERTY_DEBUG", "Imagen original: ${originalBytes.size / 1024}KB")

            val compressedBytes = compressImage(originalBytes)
            Log.d("VIVIA_PROPERTY_DEBUG", "Imagen comprimida: ${compressedBytes.size / 1024}KB")

            val fileName = "photo_${System.currentTimeMillis()}.jpg"
            val requestBody = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

            MultipartBody.Part.createFormData("images", fileName, requestBody)
        } catch (e: Exception) {
            Log.e("VIVIA_PROPERTY_DEBUG", "Error convirtiendo URI '$uriString': ${e.message}")
            null
        }
    }

    private fun compressImage(bytes: ByteArray): ByteArray {
        val MAX_SIZE_KB   = 500
        val MAX_DIMENSION = 1280

        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: return bytes

        val width  = bitmap.width
        val height = bitmap.height

        if (width > MAX_DIMENSION || height > MAX_DIMENSION) {
            val ratio     = minOf(MAX_DIMENSION.toFloat() / width, MAX_DIMENSION.toFloat() / height)
            val newWidth  = (width * ratio).toInt()
            val newHeight = (height * ratio).toInt()
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        }

        var quality = 85
        var output: ByteArrayOutputStream
        do {
            output = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            quality -= 10
        } while (output.size() > MAX_SIZE_KB * 1024 && quality > 10)

        return output.toByteArray()
    }
}