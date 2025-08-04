package site.dogether.presentation.utils

import android.content.Context
import android.content.pm.PackageManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
val today = LocalDateTime.now()
val tomorrow = today.plusDays(1)

fun Context.isPermissionGranted(permission: String): Boolean = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun LocalDateTime.toFormattedString() = format(dateFormat)

fun LocalDate.addDays(days: Long): LocalDateTime = today.plusDays(days)