package com.example.expensetracker.domain.model

data class Expense(
    val id: String? = null,
    val amount: Double,
    val description: String,
    val date: String,
    val category: String,
    val type: String = "Expense" // "Income" or "Expense"
)

