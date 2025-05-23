package com.example.data.services.image

import com.example.data.models.Image
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageService {

    @Multipart
    @POST("/file-system/image")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Image

    @GET("/file-system/image/{imageId}")
    suspend fun downloadImage(@Path("imageId") imageId: Long): ResponseBody
}