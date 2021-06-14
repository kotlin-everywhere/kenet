import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

actual fun logToResultFile(messages: Array<out String>) {
    FileOutputStream(File("result.txt"), true).use { o ->
        PrintWriter(o).use { writer ->
            messages.forEach {
                writer.println(it)
            }
            writer.println()
        }
    }
}