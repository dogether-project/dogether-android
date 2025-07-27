package site.dogether.presentation.utils

import android.content.Context
import android.content.pm.PackageManager

fun Context.isPermissionGranted(permission: String): Boolean = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED