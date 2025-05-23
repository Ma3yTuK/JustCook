package com.example.data.services.auth

data class LoginResponse(
    val token: String,
    val isFirstTime: Boolean
)