package com.eneko.gastospersonales.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): List<TransactionEntity>

    @Insert
    fun insert(transaction: TransactionEntity)

    @Update
    fun updateTransaction(transaction: TransactionEntity) // 👈 Nuevo método

    @Delete
    fun deleteTransaction(transaction: TransactionEntity)
}
