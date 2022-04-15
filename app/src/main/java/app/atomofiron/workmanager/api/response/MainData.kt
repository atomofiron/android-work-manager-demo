package app.atomofiron.workmanager.api.response

import kotlinx.serialization.Serializable

@Serializable
class MainData(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int,
)