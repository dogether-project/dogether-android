package site.dogether.data.utils

import android.content.Context
import android.net.Uri
import java.io.InputStream

/**
 * Uri를 ByteArray로 변환하는 확장 함수
 */
fun Uri.toByteArray(context: Context): ByteArray {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            inputStream.readBytes()
        } ?: throw IllegalArgumentException("Cannot open input stream for URI: $this")
    } catch (e: Exception) {
        throw IllegalArgumentException(
            "Failed to read bytes from URI: $this. Error: ${e.message}",
            e
        )
    }
}
