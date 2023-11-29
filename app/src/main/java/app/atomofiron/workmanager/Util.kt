package app.atomofiron.workmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat

fun Any.say(s: String) {
    Log.e("atomofiron", "[${javaClass.simpleName}] $s")
}

fun Int?.isValidResId(): Boolean = this != null && this != 0

fun Context.hasLocationPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
