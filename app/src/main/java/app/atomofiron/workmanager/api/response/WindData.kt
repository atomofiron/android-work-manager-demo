package app.atomofiron.workmanager.api.response

import kotlinx.serialization.Serializable

@Serializable
class WindData(
    val speed: Float,
    val deg: Int,
)