package app.atomofiron.workmanager

import android.util.Log

fun Any.say(s: String) {
    Log.e("atomofiron", "[${javaClass.simpleName}] $s")
}