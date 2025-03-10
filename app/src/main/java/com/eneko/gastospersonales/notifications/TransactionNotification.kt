package com.eneko.gastospersonales.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.eneko.gastospersonales.MainActivity
import com.eneko.gastospersonales.data.TransactionEntity

object TransactionNotification {

    private const val CHANNEL_ID = "transactions_channel"
    private const val NOTIFICATION_ID = 1

    fun showTransactionNotification(context: Context, transaction: TransactionEntity, action: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationText = when (action) {
            "add" -> "Se ha añadido una transacción de ${transaction.amount}€ en ${transaction.category}"
            "delete" -> "Se ha eliminado una transacción de ${transaction.amount}€ en ${transaction.category}"
            "update" -> "Se ha actualizado una transacción de ${transaction.amount}€ en ${transaction.category}"
            else -> "Operación desconocida"
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Gestor de Gastos Personales")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}
