package app.atomofiron.workmanager.ui.weather.state

data class WeatherState(
    val isRefreshing: Boolean,
    val isError: Boolean,
    val weatherInfo: WeatherInfo?,
)