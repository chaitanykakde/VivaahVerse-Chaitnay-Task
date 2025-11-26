package com.example.expensetracker.data.remote.dto

data class AuthResponse(
    val token: String,
    val userId: String,
    val name: String?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)
