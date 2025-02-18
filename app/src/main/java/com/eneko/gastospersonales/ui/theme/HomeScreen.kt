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
    onAddTransactionClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Gastos Personales", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddTransactionClick() },  // 🔹 Llamamos la función cuando se presiona el botón
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir Transacción")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}


@Composable
fun TransactionItem(transaction: TransactionEntity) {
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
        }
    }
}
