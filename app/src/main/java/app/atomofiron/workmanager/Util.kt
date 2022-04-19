package app.atomofiron.workmanager

import android.util.Log
import app.atomofiron.workmanager.ui.weather.state.WeatherInfo
import app.atomofiron.workmanager.ui.weather.state.WeatherType

fun Any.say(s: String) {
    Log.e("atomofiron", "[${javaClass.simpleName}] $s")
}
