package com.limbergdv.vivia_mobile.core.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.limbergdv.vivia_mobile.core.database.dao.PendingImageDao
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api.MyPropertiesApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class ImageUploadWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val pendingImageDao: PendingImageDao,
    private val api: MyPropertiesApi
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val pendingImages = pendingImageDao.getAllPendingImages()
        if (pendingImages.isEmpty()) return Result.success()

        // Agrupamos por propiedad para subir en un solo request por propiedad si es posible
        val imagesByProperty = pendingImages.groupBy { it.propertyId }

        for ((propertyId, images) in imagesByProperty) {
            try {
                Log.d("ImageWorker", "Subiendo ${images.size} imágenes para propiedad $propertyId")
                
                val multipartParts = images.mapNotNull { pending ->
                    createMultipartFromUri(context, Uri.parse(pending.imageUri))
                }

                if (multipartParts.isEmpty()) {
                    // Si no pudimos leer las imágenes, las borramos para no ciclar
                    pendingImageDao.deleteByPropertyId(propertyId)
                    continue
                }

                val response = api.uploadImages(propertyId, multipartParts)

                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d("ImageWorker", "✅ Subida exitosa para $propertyId")
                    pendingImageDao.deleteByPropertyId(propertyId)
                } else {
                    Log.e("ImageWorker", "❌ Error en API: ${response.message()}")
                    return Result.retry()
                }
            } catch (e: Exception) {
                Log.e("ImageWorker", "❌ Excepción subiendo imágenes", e)
                return Result.retry()
            }
        }

        return Result.success()
    }

    private fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            
            // Creamos un archivo temporal para OkHttp
            val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(tempFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", tempFile.name, requestFile)
        } catch (e: Exception) {
            null
        }
    }
}