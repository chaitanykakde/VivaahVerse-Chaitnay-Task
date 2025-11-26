package com.example.expensetracker.data.remote

import com.example.expensetracker.data.remote.dto.AuthResponse
import com.example.expensetracker.data.remote.dto.LoginRequest
import com.example.expensetracker.data.remote.dto.SignupRequest
import com.example.expensetracker.domain.model.Expense
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("expenses")
    suspend fun getExpenses(
        @Query("category") category: String? = null,
        @Query("date") date: String? = null
    ): List<Expense>

    @POST("expenses")
    suspend fun addExpense(@Body expense: Expense): Expense

    @GET("expenses/{id}")
    suspend fun getExpenseById(@Path("id") id: String): Expense

    @PUT("expenses/{id}")
    suspend fun updateExpense(@Path("id") id: String, @Body expense: Expense): Expense

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<Unit>
}

