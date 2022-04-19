package app.atomofiron.workmanager.ui.weather.state

import androidx.work.Data

class ErrorInfo(
    message: String?,
) {
    companion object {
        private const val KEY_MESSAGE = "KEY_MESSAGE"

        fun fromData(data: Data): ErrorInfo? {
            return when (val message = data.getString(KEY_MESSAGE)) {
                null -> null
                else -> ErrorInfo(message)
            }
        }
    }

    val message = message ?: ""

    fun toData(): Data {
        return Data.Builder()
            .putString(KEY_MESSAGE, message)
            .build()
    }
}