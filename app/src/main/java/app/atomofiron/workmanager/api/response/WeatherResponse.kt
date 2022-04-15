package app.atomofiron.workmanager.api.response

import kotlinx.serialization.*

@Serializable
class WeatherResponse(
    val id: Long,
    val name: String,
    val weather: Array<WeatherData>,
    val main: MainData,
    val wind: WindData,
    val clouds: CloudsData,
    val sys: SysData,
    val visibility: Int,
)