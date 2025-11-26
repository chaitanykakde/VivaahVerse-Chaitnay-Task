package com.example.expensetracker.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.remote.dto.LoginRequest
import com.example.expensetracker.data.remote.dto.SignupRequest
import com.example.expensetracker.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val dataStore: DataStore<Preferences> // Inject DataStore
) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var isLoggedIn by mutableStateOf(false)

    fun login() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.login(LoginRequest(email, password))
            result.onSuccess { response ->
                // Store the user's name in DataStore upon successful login
                dataStore.edit { preferences ->
                    if (response.name != null) {
                        preferences[stringPreferencesKey("user_name")] = response.name
                    }
                }
                isLoggedIn = true
            }.onFailure {
                errorMessage = "Login Failed: ${it.message}"
            }
            isLoading = false
        }
    }

    fun signup() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.signup(SignupRequest(name, email, password))
            result.onSuccess { response ->
                // Store the user's name
                dataStore.edit { preferences ->
                    if (response.name != null) {
                        preferences[stringPreferencesKey("user_name")] = response.name
                    }
                }
                isLoggedIn = true
            }.onFailure {
                errorMessage = "Registration Failed: ${it.message}"
            }
            isLoading = false
        }
    }
}

