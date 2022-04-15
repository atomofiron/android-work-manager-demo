package app.atomofiron.workmanager.ui.weather.state

class WeatherInfo(
    val cityName: String,
    val weatherType: WeatherType,
    val temperature: Float,
    val feelsLike: Float,
    val windSpeed: Float,
    val windDeg: Int,
)