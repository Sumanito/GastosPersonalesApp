import android.content.Context
import android.os.Environment
import com.eneko.gastospersonales.data.TransactionEntity
import java.io.File
import java.io.FileWriter
import java.io.IOException

object FileUtils {
    fun exportToCSV(context: Context, transactions: List<TransactionEntity>): Boolean {
        val csvFileName = "transactions.csv"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), csvFileName)

        return try {
            val fileWriter = FileWriter(file)
            fileWriter.append("ID,Categoría,Monto,Fecha\n") // Encabezados

            for (transaction in transactions) {
                fileWriter.append("${transaction.id},${transaction.category},${transaction.amount},${transaction.date}\n")
            }

            fileWriter.flush()
            fileWriter.close()
            true  // Exportación exitosa
        } catch (e: IOException) {
            e.printStackTrace()
            false // Error en exportación
        }
    }
}
