package com.limbergdv.vivia_mobile.core.hardware.domain

import android.net.Uri

interface CameraManager {
    fun hasCamera(): Boolean

    // Crea un Uri temporal en la carpeta de caché de la app para almacenar la foto
    fun createPhotoUri(): Uri?
}