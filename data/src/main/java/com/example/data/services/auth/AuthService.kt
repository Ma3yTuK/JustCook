package com.example.data.services.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("/login_email/{email}")
    suspend fun loginEmail(@Path("email") email: String): LoginResponse
}