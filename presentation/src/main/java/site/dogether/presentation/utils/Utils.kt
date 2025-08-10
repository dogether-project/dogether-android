package site.dogether.presentation.utils

import android.content.Context
import android.content.pm.PackageManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATE_FORMAT_SHORT_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
val DATE_FORMAT_FULL_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
val today: LocalDateTime = LocalDateTime.now()
val tomorrow: LocalDateTime = today.plusDays(1)
val tomorrowMidnight: LocalDateTime = today.toLocalDate().plusDays(1).atStartOfDay()

fun Context.isPermissionGranted(permission: String): Boolean = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun LocalDateTime.toFormattedString(format: DateTimeFormatter): String = format(format)

fun LocalDate.addDays(days: Long): LocalDateTime = today.plusDays(days)