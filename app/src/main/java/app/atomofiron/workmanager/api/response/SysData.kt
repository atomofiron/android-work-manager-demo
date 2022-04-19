package app.atomofiron.workmanager.api.response

import kotlinx.serialization.Serializable

@Serializable
class SysData(
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)