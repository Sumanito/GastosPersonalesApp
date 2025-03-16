package com.eneko.gastospersonales.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.eneko.gastospersonales.data.TransactionEntity
import com.eneko.gastospersonales.viewmodel.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import com.eneko.gastospersonales.R

@Composable
fun HomeScreen(
    transactions: List<TransactionEntity>,
    onAddTransactionClick: () -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onEditTransaction: (TransactionEntity) -> Unit
) {
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }
    var transactionToEdit by remember { mutableStateOf<TransactionEntity?>(null) }
    var balance by remember { mutableDoubleStateOf(0.0) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val transactionViewModel: TransactionViewModel = viewModel()

    LaunchedEffect(Unit) {
        transactionViewModel.getBalance { newBalance ->
            balance = newBalance
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (showSnackbar) {
            Snackbar(
                action = {
                    Button(onClick = { showSnackbar = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(id = R.string.balance)}: ${balance}€",
                style = MaterialTheme.typography.headlineMedium,
                color = if (balance >= 0) IncomeColor else ExpenseColor
            )

            Button(
                onClick = {
                    transactionViewModel.exportTransactionsToFile { success ->
                        snackbarMessage = if (success) {
                            "✅ Exportación exitosa: Archivo guardado en Documentos 📂"
                        } else {
                            "❌ Error al exportar"
                        }
                        showSnackbar = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                modifier = Modifier
                    .padding(8.dp)
                    .height(45.dp)
                    .widthIn(min = 120.dp, max = 180.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Filled.Download,
                    contentDescription = "Exportar CSV",
                    modifier = Modifier.size(18.dp),
                    tint = Color.White // Asegura que el icono sea blanco
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Exportar CSV",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White // 🔹 Cambia el color del texto a blanco
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Botón para añadir transacción
        Button(
            onClick = { onAddTransactionClick() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(id = R.string.add_transaction))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onDeleteClick = { transactionToDelete = transaction },
                    onEditClick = { transactionToEdit = transaction }
                )
            }
        }
    }

    // 🔹 Diálogo de confirmación para eliminar
    transactionToDelete?.let { transaction ->
        DeleteTransactionDialog(
            transaction = transaction,
            onConfirm = {
                onDeleteTransaction(transaction)
                transactionViewModel.getBalance { newBalance -> balance = newBalance }  // ✅ Se actualiza el saldo
                transactionToDelete = null
            },
            onDismiss = { transactionToDelete = null }
        )
    }

    // 🔹 Diálogo de edición de transacción
    transactionToEdit?.let { transaction ->
        EditTransactionDialog(
            transaction = transaction,
            onConfirm = { updatedTransaction ->
                onEditTransaction(updatedTransaction)
                transactionViewModel.getBalance { newBalance -> balance = newBalance }  // ✅ Se actualiza el saldo
                transactionToEdit = null
            },
            onDismiss = { transactionToEdit = null }
        )
    }

    LaunchedEffect(transactions) {
        transactionViewModel.getBalance { newBalance -> balance = newBalance }
    }
}


@Composable
fun TransactionItem(transaction: TransactionEntity, onDeleteClick: () -> Unit, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.category, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${stringResource(id = R.string.amount)}: ${transaction.amount}€",
                style = MaterialTheme.typography.bodyMedium,
                color = if (transaction.type == "Ingreso") IncomeColor else ExpenseColor
            )
            Text(
                text = "${stringResource(id = R.string.date)}: ${transaction.date}",
                style = MaterialTheme.typography.bodySmall,
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                // 🔹 Botón Editar (NEGRO)
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(id = R.string.edit), color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(id = R.string.delete), color = Color.White)
                }
            }
        }
    }
}
