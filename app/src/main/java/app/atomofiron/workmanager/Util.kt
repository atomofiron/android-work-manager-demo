package app.atomofiron.workmanager

import android.util.Log

fun Any.say(s: String) {
    Log.e("atomofiron", "[${javaClass.simpleName}] $s")
}

fun Int?.isValidResId(): Boolean = this != null && this != 0