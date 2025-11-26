package com.example.expensetracker.presentation.expenses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var expenses by mutableStateOf<List<Expense>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Filter State
    var selectedCategory by mutableStateOf<String?>(null)

    // Add/Edit State
    var currentExpenseId by mutableStateOf<String?>(null)
    var amountInput by mutableStateOf("")
    var descriptionInput by mutableStateOf("")
    var categoryInput by mutableStateOf("")
    var dateInput by mutableStateOf("") 
    var typeInput by mutableStateOf("Expense") // Default to Expense

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.getExpenses(category = selectedCategory)
            result.onSuccess {
                expenses = it
            }.onFailure {
                errorMessage = it.message
            }
            isLoading = false
        }
    }

    fun getExpenseById(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.getExpenseById(id)
            result.onSuccess { expense ->
                currentExpenseId = expense.id
                amountInput = expense.amount.toString()
                descriptionInput = expense.description
                categoryInput = expense.category
                dateInput = expense.date
                typeInput = expense.type
            }.onFailure {
                errorMessage = "Failed to load expense: ${it.message}"
            }
            isLoading = false
        }
    }

    fun clearInput() {
        currentExpenseId = null
        amountInput = ""
        descriptionInput = ""
        categoryInput = ""
        dateInput = ""
        typeInput = "Expense"
    }

    fun saveExpense(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val amount = amountInput.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                errorMessage = "Invalid amount"
                return@launch
            }
            if (descriptionInput.isBlank() || categoryInput.isBlank()) {
                errorMessage = "Please fill all fields"
                return@launch
            }
            
            isLoading = true
            val expense = Expense(
                id = currentExpenseId,
                amount = amount,
                description = descriptionInput,
                category = categoryInput,
                date = dateInput.ifEmpty { java.time.LocalDate.now().toString() },
                type = typeInput
            )
            
            val result = if (currentExpenseId != null) {
                 repository.updateExpense(currentExpenseId!!, expense)
            } else {
                 repository.addExpense(expense)
            }

            result.onSuccess {
                loadExpenses()
                onSuccess()
                clearInput()
            }.onFailure {
                errorMessage = it.message
            }
            isLoading = false
        }
    }

    fun deleteExpense(id: String) {
         viewModelScope.launch {
            repository.deleteExpense(id)
            loadExpenses()
        }
    }

    fun filterByCategory(category: String?) {
        selectedCategory = category
        loadExpenses()
    }
}
