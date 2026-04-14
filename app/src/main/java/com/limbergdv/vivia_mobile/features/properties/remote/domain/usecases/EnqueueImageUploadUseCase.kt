package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import android.content.Context
import android.net.Uri
import androidx.work.*
import com.limbergdv.vivia_mobile.core.database.dao.PendingImageDao
import com.limbergdv.vivia_mobile.core.database.entities.PendingImageEntity
import com.limbergdv.vivia_mobile.core.workers.ImageUploadWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EnqueueImageUploadUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pendingImageDao: PendingImageDao
) {
    suspend operator fun invoke(propertyId: String, imageUris: List<Uri>) {
        // 1. Guardamos en Room
        imageUris.forEach { uri ->
            pendingImageDao.insert(
                PendingImageEntity(
                    propertyId = propertyId,
                    imageUri = uri.toString()
                )
            )
        }

        // 2. Configuramos restricciones (Solo con Internet)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // 3. Encolamos el trabajo
        val uploadRequest = OneTimeWorkRequestBuilder<ImageUploadWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("upload_images_$propertyId")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "upload_images_work",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            uploadRequest
        )
    }
}