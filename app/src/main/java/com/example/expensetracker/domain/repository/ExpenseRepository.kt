package com.example.expensetracker.domain.repository

import com.example.expensetracker.data.remote.dto.AuthResponse
import com.example.expensetracker.data.remote.dto.LoginRequest
import com.example.expensetracker.data.remote.dto.SignupRequest
import com.example.expensetracker.domain.model.Expense

interface ExpenseRepository {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun signup(request: SignupRequest): Result<AuthResponse>
    suspend fun getExpenses(category: String? = null, date: String? = null): Result<List<Expense>>
    suspend fun getExpenseById(id: String): Result<Expense>
    suspend fun addExpense(expense: Expense): Result<Expense>
    suspend fun updateExpense(id: String, expense: Expense): Result<Expense>
    suspend fun deleteExpense(id: String): Result<Unit>
    suspend fun saveToken(token: String)
}
