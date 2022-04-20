package app.atomofiron.workmanager.ui.weather.state

// напрашивается sealed class
data class WeatherState(
    val isRefreshing: Boolean,
    val weatherInfo: WeatherInfo?,
)