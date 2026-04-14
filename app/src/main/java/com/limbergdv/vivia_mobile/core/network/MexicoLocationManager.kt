package com.limbergdv.vivia_mobile.core.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.core.network.dtos.MexicoState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MexicoLocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()

    fun getMexicoLocations(): List<MexicoState> {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.mexico_locations)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<MexicoState>>() {}.type
            gson.fromJson(jsonString, listType)
        } catch (e: Exception) {
            emptyList()
        }
    }
}