package app.atomofiron.workmanager.ui.weather.state

class WeatherTypeDif(
    val weatherTypeWasChanged: Boolean,
    val newSoundResId: Int,
    val newVideoResId: Int,
) {
    companion object {
        fun calc(before: WeatherType?, after: WeatherType?): WeatherTypeDif {
            val weatherTypeWasChanged = before !== after
            return WeatherTypeDif(
                weatherTypeWasChanged,
                newSoundResId = if (weatherTypeWasChanged) after?.soundResId ?: 0 else 0,
                newVideoResId = if (weatherTypeWasChanged) after?.videoResId ?: 0 else 0,
            )
        }
    }

    inline fun ifChanged(action: (newSoundResId: Int, newVideoResId: Int) -> Unit) {
        if (weatherTypeWasChanged) action(newSoundResId, newVideoResId)
    }
}