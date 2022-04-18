package app.atomofiron.workmanager.ui.weather.state

class WeatherInfo(
    val weatherType: WeatherType,
    val weatherDescription: String,
    val cityName: String,
    val temperature: Int,
    val feelsLike: Int,
    val windSpeed: Int,
    val windDeg: Int,
)