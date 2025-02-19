package com.eneko.gastospersonales.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eneko.gastospersonales.data.TransactionEntity

@Composable
fun HomeScreen(
    transactions: List<TransactionEntity>,
    onAddTransactionClick: () -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit
) {
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Gastos Personales", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddTransactionClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir Transacción")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionItem(transaction, onDeleteClick = { transactionToDelete = transaction })
            }
        }
    }

    transactionToDelete?.let { transaction ->
        DeleteTransactionDialog(
            transaction = transaction,
            onConfirm = {
                onDeleteTransaction(transaction)
                transactionToDelete = null
            },
            onDismiss = { transactionToDelete = null }
        )
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.category, style = MaterialTheme.typography.titleMedium)
            Text(text = "Monto: ${transaction.amount} €", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${transaction.date}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                Text("Eliminar")
            }
        }
    }
}

@Composable
fun DeleteTransactionDialog(transaction: TransactionEntity, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar transacción") },
        text = { Text("¿Estás seguro de que deseas eliminar esta transacción de ${transaction.amount} € en ${transaction.category}?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
