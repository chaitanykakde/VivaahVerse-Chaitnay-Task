package com.example.expensetracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensetracker.presentation.auth.LoginScreen
import com.example.expensetracker.presentation.auth.RegisterScreen
import com.example.expensetracker.presentation.dashboard.DashboardScreen
import com.example.expensetracker.presentation.expenses.AddExpenseScreen
import com.example.expensetracker.presentation.expenses.ExpenseListScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show bottom bar only on main screens
    val showBottomBar = currentRoute in listOf("dashboard", "expenses")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "dashboard",
                        onClick = { navController.navigate("dashboard") },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                        label = { Text("Dashboard") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "expenses",
                        onClick = { navController.navigate("expenses") },
                        icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
                        label = { Text("Categories") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("dashboard") { DashboardScreen(navController) }
            composable("expenses") { ExpenseListScreen(navController) }
            composable(
                route = "add_expense?expenseId={expenseId}",
                arguments = listOf(navArgument("expenseId") { 
                    type = NavType.StringType 
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val expenseId = backStackEntry.arguments?.getString("expenseId")
                AddExpenseScreen(navController, expenseId = expenseId) 
            }
        }
    }
}
