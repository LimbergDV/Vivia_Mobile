package com.limbergdv.vivia_mobile.core.hardware.data

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.FileProvider
import com.limbergdv.vivia_mobile.core.hardware.domain.CameraManager

import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AndroidCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : CameraManager {

    override fun hasCamera(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    override fun createPhotoUri(): Uri? {
        return try {
            // Crea un archivo temporal en la carpeta de caché de la app
            val photoFile = File.createTempFile(
                "vivia_photo_${System.currentTimeMillis()}",
                ".jpg",
                context.cacheDir
            )

            // Convierte el File a Uri usando FileProvider (requerido desde Android 7+)
            FileProvider.getUriForFile(
                context,
                "com.limbergdv.vivia_mobile.fileprovider",  // debe coincidir con el authority del Manifest
                photoFile
            )
        } catch (e: Exception) {
            android.util.Log.e("AndroidCameraManager", "Error al crear Uri de foto: ${e.message}")
            null
        }
    }
}