package com.eneko.gastospersonales.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eneko.gastospersonales.data.TransactionEntity

@Composable
fun EditTransactionDialog(
    transaction: TransactionEntity,
    onConfirm: (TransactionEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var updatedCategory by remember { mutableStateOf(transaction.category) }
    var updatedAmount by remember { mutableStateOf(transaction.amount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar transacción") },
        text = {
            Column {
                TextField(
                    value = updatedCategory,
                    onValueChange = { updatedCategory = it },
                    label = { Text("Categoría") }
                )
                TextField(
                    value = updatedAmount,
                    onValueChange = { updatedAmount = it },
                    label = { Text("Monto") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedTransaction = transaction.copy(
                        category = updatedCategory,
                        amount = updatedAmount.toDoubleOrNull() ?: transaction.amount
                    )
                    onConfirm(updatedTransaction)
                },
                colors = ButtonDefaults.buttonColors(
                    if (transaction.type == "Ingreso") EditIncomeColor else EditExpenseColor
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
