import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.eneko.gastospersonales.data.TransactionEntity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object FileUtils {
    fun exportToCSV(context: Context, transactions: List<TransactionEntity>): Boolean {
        val csvFileName = "transactions.csv"
        val isAndroid10OrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        val outputStream: OutputStream? = if (isAndroid10OrHigher) {
            val contentResolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, csvFileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)  // ✅ CAMBIADO a Descargas
            }
            val uri = contentResolver.insert(MediaStore.Downloads.getContentUri("external"), contentValues)
            uri?.let { contentResolver.openOutputStream(it) }
        } else {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), csvFileName)  // ✅ CAMBIADO a Descargas
            FileOutputStream(file)
        }

        return try {
            outputStream?.bufferedWriter()?.use { writer ->
                writer.append("ID,Categoría,Monto,Fecha\n") // Encabezados del CSV
                for (transaction in transactions) {
                    writer.append("${transaction.id},${transaction.category},${transaction.amount},${transaction.date}\n")
                }
            }
            true  // ✅ Exportación exitosa
        } catch (e: IOException) {
            e.printStackTrace()
            false // ❌ Error en exportación
        }
    }
}
