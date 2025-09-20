package site.dogether.presentation.utils

import android.content.Context
import android.content.pm.PackageManager
import site.dogether.common.exception.NetworkFailureException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATE_FORMAT_SHORT_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
val DATE_FORMAT_FULL_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
val today: LocalDate = LocalDate.now()
val todayWithTime: LocalDateTime = LocalDateTime.now()
val tomorrow: LocalDate = today.plusDays(1)
val tomorrowMidnight: LocalDateTime = today.plusDays(1).atStartOfDay()

fun Context.isPermissionGranted(permission: String): Boolean = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun LocalDate.toFormattedString(format: DateTimeFormatter): String = format(format)
fun LocalDateTime.toFormattedString(format: DateTimeFormatter): String = format(format)

fun String.toLocalDate(format: DateTimeFormatter): LocalDate = LocalDate.parse(this, format)

fun LocalDate.addDays(days: Long): LocalDate = today.plusDays(days)

fun Throwable.handle(
    onNetworkFailureException: ((String) -> Unit)? = null,
    onElse: ((Throwable) -> Unit)? = null,
) {
    if (this is NetworkFailureException) {
        onNetworkFailureException?.let { onNetworkFailureException(this.code) }
    } else {
        onElse?.let { onElse(this) }
    }
}