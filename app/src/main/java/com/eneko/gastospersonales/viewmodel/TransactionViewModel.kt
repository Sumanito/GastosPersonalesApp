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
        viewModelScope.launch(Dispatchers.IO) { // <-- Mueve la ejecución a un hilo secundario (IO)
            val transactions = transactionDao.getAllTransactions()
            withContext(Dispatchers.Main) { // <-- Vuelve al hilo principal para actualizar la UI
                onResult(transactions)
            }
        }
    }


    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionDao.insertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionDao.deleteTransaction(transaction)
        }
    }
}

