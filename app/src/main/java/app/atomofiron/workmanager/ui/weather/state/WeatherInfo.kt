package app.atomofiron.workmanager.ui.weather.state

import androidx.work.Data

class WeatherInfo(
    val weatherType: WeatherType,
    val weatherDescription: String,
    val cityName: String,
    val temperature: Int,
    val feelsLike: Int,
    val windSpeed: Int,
    val windDeg: Int,
) {
    companion object {
        private const val KEY_TYPE = "KEY_TYPE"
        private const val KEY_WEATHER_DESCRIPTION = "KEY_WEATHER_DESCRIPTION"
        private const val KEY_CITY_NAME = "KEY_CITY_NAME"
        private const val KEY_TEMPERATURE = "KEY_TEMPERATURE"
        private const val KEY_FEELS_LIKE = "KEY_FEELS_LIKE"
        private const val KEY_WIND_SPEED = "KEY_WIND_SPEED"
        private const val KEY_WIND_DEG = "KEY_WIND_DEG"

        fun fromData(data: Data): WeatherInfo {
            return data.run {
                WeatherInfo(
                    weatherType = WeatherType.getOrNull(getInt(KEY_TYPE, -1))!!,
                    weatherDescription = getString(KEY_WEATHER_DESCRIPTION)!!,
                    cityName = getString(KEY_CITY_NAME)!!,
                    temperature = getInt(KEY_TEMPERATURE, 0),
                    feelsLike = getInt(KEY_FEELS_LIKE, 0),
                    windSpeed = getInt(KEY_WIND_SPEED, 0),
                    windDeg = getInt(KEY_WIND_DEG, 0),
                )
            }
        }
    }

    fun toData(): Data {
        return Data.Builder()
            .putInt(KEY_TYPE, weatherType.index)
            .putString(KEY_WEATHER_DESCRIPTION, weatherDescription)
            .putString(KEY_CITY_NAME, cityName)
            .putInt(KEY_TEMPERATURE, temperature)
            .putInt(KEY_FEELS_LIKE, feelsLike)
            .putInt(KEY_WIND_SPEED, windSpeed)
            .putInt(KEY_WIND_DEG, windDeg)
            .build()
    }
}