package com.limbergdv.vivia_mobile.features.addProperty.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.local.mapper.toDraftEntity
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.local.mapper.toUiState
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.api.AddPropertyApi
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.mappers.toRequest
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState
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

    private val gson = Gson()

    // ── Borrador local (Room) ─────────────────────────────────────────────────

    override fun getDraft(): Flow<AddPropertyUiState?> =
        draftDao.getDraft().map { it?.toUiState() }

    override suspend fun saveDraft(uiState: AddPropertyUiState) {
        draftDao.saveDraft(uiState.toDraftEntity())
    }

    override suspend fun clearDraft() {
        draftDao.clearDraft()
    }

    // ── Publicación remota ────────────────────────────────────────────────────

    override suspend fun createProperty(property: Property): Property {
        val propertyRequest = property.toRequest()
        val propertyJson = gson.toJson(propertyRequest)
        Log.d("VIVIA_PROPERTY_DEBUG", "Property JSON: $propertyJson")

        val propertyBody = propertyJson
            .toRequestBody("application/json".toMediaTypeOrNull())

        val imageParts: List<MultipartBody.Part> = property.imageUris.mapNotNull { uriString ->
            uriToMultipart(uriString)
        }
        Log.d("VIVIA_PROPERTY_DEBUG", "Imágenes a subir: ${imageParts.size}")

        val response = if (imageParts.isNotEmpty()) {
            api.createProperty(request = propertyBody, images = imageParts)
        } else {
            api.createPropertyWithoutImages(request = propertyBody)
        }

        Log.d("VIVIA_PROPERTY_DEBUG", "Response success: ${response.success}")
        Log.d("VIVIA_PROPERTY_DEBUG", "Response message: ${response.message}")
        Log.d("VIVIA_PROPERTY_DEBUG", "Response data id: ${response.data?.id}")

        if (!response.success || response.data == null) {
            throw Exception(response.message ?: "Error al crear la propiedad")
        }

        return response.data.toDomain()
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun uriToMultipart(uriString: String): MultipartBody.Part? {
        return try {
            val uri = Uri.parse(uriString)
            val stream = context.contentResolver.openInputStream(uri) ?: return null
            val originalBytes = stream.readBytes()
            stream.close()

            val originalSizeKb = originalBytes.size / 1024
            Log.d("VIVIA_PROPERTY_DEBUG", "Imagen original: ${originalSizeKb}KB")

            // Comprimir la imagen
            val compressedBytes = compressImage(originalBytes)
            val compressedSizeKb = compressedBytes.size / 1024
            Log.d("VIVIA_PROPERTY_DEBUG", "Imagen comprimida: ${compressedSizeKb}KB")

            val fileName = "photo_${System.currentTimeMillis()}.jpg"
            val requestBody = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

            MultipartBody.Part.createFormData("images", fileName, requestBody)
        } catch (e: Exception) {
            Log.e("VIVIA_PROPERTY_DEBUG", "Error convirtiendo URI '$uriString': ${e.message}")
            null
        }
    }

    /**
     * Comprime la imagen progresivamente hasta que pese menos de MAX_SIZE_KB.
     * Primero reduce dimensiones si es muy grande, luego baja calidad JPEG.
     */
    private fun compressImage(bytes: ByteArray): ByteArray {
        val MAX_SIZE_KB   = 500   // límite por imagen en KB
        val MAX_DIMENSION = 1280  // máximo ancho o alto en píxeles

        // 1. Decodificar el bitmap original
        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: return bytes

        // 2. Redimensionar si alguna dimensión supera MAX_DIMENSION
        val width  = bitmap.width
        val height = bitmap.height

        if (width > MAX_DIMENSION || height > MAX_DIMENSION) {
            val ratio = minOf(
                MAX_DIMENSION.toFloat() / width,
                MAX_DIMENSION.toFloat() / height
            )
            val newWidth  = (width * ratio).toInt()
            val newHeight = (height * ratio).toInt()
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            Log.d("VIVIA_PROPERTY_DEBUG", "Redimensionado a ${newWidth}x${newHeight}")
        }

        // 3. Comprimir bajando calidad progresivamente hasta cumplir MAX_SIZE_KB
        var quality = 85
        var output: ByteArrayOutputStream

        do {
            output = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            quality -= 10
            Log.d("VIVIA_PROPERTY_DEBUG", "Comprimiendo con quality=$quality, size=${output.size() / 1024}KB")
        } while (output.size() > MAX_SIZE_KB * 1024 && quality > 10)

        return output.toByteArray()
    }
}