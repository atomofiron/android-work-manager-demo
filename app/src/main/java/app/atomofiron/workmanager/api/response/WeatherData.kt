package app.atomofiron.workmanager.api.response

import kotlinx.serialization.Serializable

@Serializable
class WeatherData(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)