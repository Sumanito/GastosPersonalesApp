package com.eneko.gastospersonales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eneko.gastospersonales.viewmodel.TransactionViewModel
import com.eneko.gastospersonales.data.TransactionEntity
import com.eneko.gastospersonales.ui.theme.GastosPersonalesTheme
import com.eneko.gastospersonales.ui.theme.HomeScreen
import com.eneko.gastospersonales.ui.theme.AddTransactionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GastosPersonalesTheme {
                val navController = rememberNavController()
                val transactionViewModel: TransactionViewModel = viewModel()
                val transactions = remember { mutableStateListOf<TransactionEntity>() }

                // Cargar transacciones desde la base de datos
                LaunchedEffect(Unit) {
                    transactionViewModel.getTransactions { fetchedTransactions ->
                        transactions.clear()
                        transactions.addAll(fetchedTransactions)
                    }
                }

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            transactions = transactions,
                            onAddTransactionClick = {
                                navController.navigate("addTransaction")
                            },
                            onDeleteTransaction = { transaction ->
                                transactionViewModel.deleteTransaction(transaction) { updatedTransactions ->
                                    transactions.clear()
                                    transactions.addAll(updatedTransactions)  // ✅ Actualizamos la lista después de eliminar
                                }
                            }
                        )
                    }

                    composable("addTransaction") {
                        AddTransactionScreen { transaction ->
                            transactionViewModel.addTransaction(transaction) {
                                transactions.clear()
                                transactions.addAll(it)  // 🔄 Actualiza la lista después de añadir
                            }
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

