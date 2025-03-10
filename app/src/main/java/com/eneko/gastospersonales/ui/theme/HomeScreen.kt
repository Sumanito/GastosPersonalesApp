package com.eneko.gastospersonales.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eneko.gastospersonales.data.TransactionEntity
import com.eneko.gastospersonales.viewmodel.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import com.eneko.gastospersonales.R
import androidx.compose.material.icons.filled.Download

@Composable
fun HomeScreen(
    transactions: List<TransactionEntity>,
    onAddTransactionClick: () -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onEditTransaction: (TransactionEntity) -> Unit
) {
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }
    var balance by remember { mutableStateOf(0.0) }

    val transactionViewModel: TransactionViewModel = viewModel()

    // Obtener saldo total
    LaunchedEffect(Unit) {
        transactionViewModel.getBalance { newBalance ->
            balance = newBalance
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 🔹 Encabezado con saldo total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(id = R.string.balance)}: ${balance}€",
                style = MaterialTheme.typography.headlineMedium
            )

            // 🔹 Botón para exportar transacciones
            Button(
                onClick = { transactionViewModel.exportTransactionsToFile { success ->
                    if (success) {
                        // Puedes mostrar un mensaje de éxito
                        println("Exportación completada")
                    } else {
                        println("Error al exportar")
                    }
                }},
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Filled.Download, contentDescription = stringResource(id = R.string.export_csv))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.export_csv))
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddTransactionClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.add_transaction))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionItem(
                    transaction,
                    onDeleteClick = { transactionToDelete = transaction }, // ⚠️ Solo marcamos para eliminar
                    onEditClick = { onEditTransaction(transaction) }
                )
            }
        }
    }

    transactionToDelete?.let { transaction ->
        DeleteTransactionDialog(
            transaction = transaction,
            onConfirm = {
                onDeleteTransaction(transaction)  // ✅ Eliminar tras confirmación
                transactionToDelete = null
            },
            onDismiss = { transactionToDelete = null }
        )
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity, onDeleteClick: () -> Unit, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.category, style = MaterialTheme.typography.titleMedium)
            Text(text = "${stringResource(id = R.string.amount)}: ${transaction.amount}€", style = MaterialTheme.typography.bodyMedium)
            Text(text = "${stringResource(id = R.string.date)}: ${transaction.date}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = onEditClick, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(id = R.string.edit))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(id = R.string.delete))
                }
            }
        }
    }
}
