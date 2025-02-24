package com.eneko.gastospersonales.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eneko.gastospersonales.data.AppDatabase
import com.eneko.gastospersonales.data.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionDao = AppDatabase.getDatabase(application).transactionDao()

    fun getTransactions(onResult: (List<TransactionEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val transactions = transactionDao.getAllTransactions()
            withContext(Dispatchers.Main) {
                onResult(transactions)
            }
        }
    }

    fun addTransaction(transaction: TransactionEntity, onResult: (List<TransactionEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.insert(transaction)  // 🟢 Inserta la transacción en la BD

            val updatedTransactions = transactionDao.getAllTransactions()  // 🔄 Obtiene la lista actualizada
            withContext(Dispatchers.Main) {
                onResult(updatedTransactions)  // ✅ Devuelve la lista actualizada a la UI
            }
        }
    }


    fun deleteTransaction(transaction: TransactionEntity, onComplete: (List<TransactionEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.deleteTransaction(transaction)
            val updatedTransactions = transactionDao.getAllTransactions() // Obtener la lista actualizada
            withContext(Dispatchers.Main) {
                onComplete(updatedTransactions) // Pasar la lista actualizada al callback
            }
        }
    }



    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.updateTransaction(transaction)
        }
    }



}

