package com.eneko.gastospersonales.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eneko.gastospersonales.data.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTransactionScreen(
    onTransactionAdded: (TransactionEntity) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Ingreso") } // Puede ser "Ingreso" o "Gasto"

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Añadir Transacción", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(text = "Tipo:")
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { type = "Ingreso" }, enabled = type != "Ingreso") {
                Text("Ingreso")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { type = "Gasto" }, enabled = type != "Gasto") {
                Text("Gasto")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (amount.isNotEmpty() && category.isNotEmpty()) {
                    val transaction = TransactionEntity(
                        amount = amount.toDouble(),
                        category = category,
                        date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                        type = type
                    )
                    onTransactionAdded(transaction)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir")
        }
    }
}

