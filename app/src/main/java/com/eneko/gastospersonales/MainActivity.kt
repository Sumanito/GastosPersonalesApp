package com.eneko.gastospersonales

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.eneko.gastospersonales.notifications.TransactionNotification

class MainActivity : ComponentActivity() {

    companion object {
        private const val CHANNEL_ID = "transactions_channel"
        private const val REQUEST_CODE_NOTIFICATIONS = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Crear canal de notificaciones
        createNotificationChannel()

        // ✅ Solicitar permiso de notificaciones si es necesario (Android 13+)
        requestNotificationPermission()

        setContent {
            GastosPersonalesTheme {
                val navController = rememberNavController()
                val transactionViewModel: TransactionViewModel = viewModel()
                val transactions = remember { mutableStateListOf<TransactionEntity>() }

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
                                    transactions.addAll(updatedTransactions)
                                }
                            },
                            onEditTransaction = { updatedTransaction ->
                                transactionViewModel.updateTransaction(updatedTransaction) {
                                    transactionViewModel.getTransactions { updatedTransactions ->
                                        transactions.clear()
                                        transactions.addAll(updatedTransactions)
                                    }
                                }
                            }




                        )
                    }

                    composable("addTransaction") {
                        AddTransactionScreen { transaction ->
                            transactionViewModel.addTransaction(transaction) { updatedTransactions ->
                                transactions.clear()
                                transactions.addAll(updatedTransactions)

                                // ✅ Disparar notificación de nueva transacción
                                TransactionNotification.showTransactionNotification(
                                    context = this@MainActivity,
                                    transaction = transaction,
                                    action = "add"
                                )
                            }
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Transacciones",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de transacciones"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_NOTIFICATIONS
                )
            }
        }
    }
}
