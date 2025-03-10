package com.eneko.gastospersonales.ui.theme

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.eneko.gastospersonales.data.TransactionEntity

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
