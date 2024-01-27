package hr.algebra.rickandmortyapp.logger

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

class FileLogger(private val context: Context) {

    private val logFileName = "app_logs.txt"
    private val directoryPath = "C:\\Users\\antev\\Desktop\\androidlogs"

    fun log(message: String) {
        try {
            val logFile = getLogFile()

            val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val logMessage = "$timeStamp: $message\n"
            FileOutputStream(logFile, true).bufferedWriter().use { writer ->
                writer.append(logMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLogFile(): File {
        val logDirectory = File(directoryPath)

        // Ensure that the directory exists; create it if it doesn't
        if (!logDirectory.exists()) {
            logDirectory.mkdirs()
        }

        return File(logDirectory, logFileName)
    }
}
