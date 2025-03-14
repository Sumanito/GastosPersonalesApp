package com.eneko.gastospersonales.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.eneko.gastospersonales.data.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTransactionScreen(
    onTransactionAdded: (TransactionEntity) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Ingreso") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Añadir Transacción", style = MaterialTheme.typography.headlineMedium, color = TextColor)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto", color = TextColor) },
            textStyle = TextStyle(color = TextColor), // Asegura que el texto sea visible
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Categoría", color = TextColor) },
            textStyle = TextStyle(color = TextColor), // Asegura que el texto sea visible
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(text = "Tipo:", color = TextColor)
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { type = "Ingreso" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type == "Ingreso") IncomeColor else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Ingreso", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { type = "Gasto" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type == "Gasto") ExpenseColor else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Gasto", color = Color.White)
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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Añadir", color = Color.White)
        }
    }
}
