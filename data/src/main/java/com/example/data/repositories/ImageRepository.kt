package com.example.data.repositories

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf
import com.example.data.models.Image
import com.example.data.services.image.ImageService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import java.io.File


class ImageRepository(
    private val retrofit: Retrofit
) {
    private val imageService: ImageService = retrofit.create(ImageService::class.java)

    suspend fun uploadImage(image: Uri): Image {
        val file = File(image.path!!)

        return imageService.uploadImage(MultipartBody.Part.createFormData("image", file.name, file.asRequestBody()))
    }

    suspend fun downloadImage(imageId: Long) = imageService.downloadImage(imageId)

    fun getUrl(imageId: Long): String = retrofit.baseUrl().toUri().resolve("./file-system/image/${imageId}").toString()
}

val LocalImageRepository = compositionLocalOf<ImageRepository?> { null }