package com.eneko.gastospersonales.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eneko.gastospersonales.data.AppDatabase
import com.eneko.gastospersonales.data.TransactionEntity
import com.eneko.gastospersonales.notifications.TransactionNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionDao = AppDatabase.getDatabase(application).transactionDao()
    private val appContext = application.applicationContext // ✅ Contexto de la aplicación

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
            transactionDao.insert(transaction)
            val updatedTransactions = transactionDao.getAllTransactions()
            withContext(Dispatchers.Main) {
                TransactionNotification.showTransactionNotification(appContext, transaction, "add")
                onResult(updatedTransactions)
            }
        }
    }

    fun deleteTransaction(transaction: TransactionEntity, onComplete: (List<TransactionEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.deleteTransaction(transaction)
            val updatedTransactions = transactionDao.getAllTransactions()
            withContext(Dispatchers.Main) {
                TransactionNotification.showTransactionNotification(appContext, transaction, "delete")
                onComplete(updatedTransactions)
            }
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.updateTransaction(transaction)
            withContext(Dispatchers.Main) {
                TransactionNotification.showTransactionNotification(appContext, transaction, "update")
            }
        }
    }
    fun getBalance(onResult: (Double) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val transactions = transactionDao.getAllTransactions()
            val balance = transactions.sumOf { it.amount }
            withContext(Dispatchers.Main) {
                onResult(balance)
            }
        }
    }

    fun exportTransactionsToFile(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val transactions = transactionDao.getAllTransactions()
            val success = FileUtils.exportToCSV(appContext, transactions)  // Usa FileUtils para exportar
            withContext(Dispatchers.Main) {
                onComplete(success) // Devuelve el estado de la exportación
            }
        }
    }



}
