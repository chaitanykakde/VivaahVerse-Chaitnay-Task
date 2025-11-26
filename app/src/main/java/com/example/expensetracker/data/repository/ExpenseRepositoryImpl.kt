package com.example.expensetracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.expensetracker.data.remote.ApiService
import com.example.expensetracker.data.remote.dto.AuthResponse
import com.example.expensetracker.data.remote.dto.LoginRequest
import com.example.expensetracker.data.remote.dto.SignupRequest
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dataStore: DataStore<Preferences>
) : ExpenseRepository {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(request: SignupRequest): Result<AuthResponse> {
        return try {
            val response = api.signup(request)
            saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExpenses(category: String?, date: String?): Result<List<Expense>> {
        return try {
            Result.success(api.getExpenses(category, date))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExpenseById(id: String): Result<Expense> {
        return try {
            Result.success(api.getExpenseById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addExpense(expense: Expense): Result<Expense> {
        return try {
            Result.success(api.addExpense(expense))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateExpense(id: String, expense: Expense): Result<Expense> {
        return try {
            Result.success(api.updateExpense(id, expense))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteExpense(id: String): Result<Unit> {
        return try {
            api.deleteExpense(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { settings ->
            settings[TOKEN_KEY] = token
        }
    }
}
