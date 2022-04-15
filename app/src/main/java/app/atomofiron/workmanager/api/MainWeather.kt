package app.atomofiron.workmanager.api

import app.atomofiron.workmanager.ui.weather.state.WeatherType
import app.atomofiron.workmanager.ui.weather.state.WeatherType.*

enum class MainWeather {

    Unknown, Thunderstorm, Drizzle, Rain, Snow, Mist, Smoke, Haze, Dust, Fog, Sand, Ash, Squall, Tornado, Clear, Clouds;

    companion object {

        private fun resolve(main: String): MainWeather = values().find { it.name == main } ?: Unknown

        fun getType(main: String, description: String, forDay: Boolean): WeatherType = when (resolve(main)) {
            Unknown -> WeatherType.Unknown
            Drizzle -> if (forDay) RainDay else RainNight
            Rain -> if (forDay) RainDay else RainNight
            Thunderstorm -> if (forDay) ThunderstormDay else ThunderstormNight
            Snow -> if (forDay) SnowDay else SnowNight
            Clear -> if (forDay) ClearDay else ClearNight
            Clouds -> when (description) {
                "few clouds" -> if (forDay) ClearDay else ClearNight
                "scattered clouds" -> if (forDay) PartlySunny else PartlyCloudNight
                "broken clouds" -> if (forDay) PartlyCloudDay else PartlyCloudNight
                "overcast clouds" -> if (forDay) CloudyDay else CloudyNight
                else -> if (forDay) PartlyCloudDay else PartlyCloudNight
            }
            Mist -> if (forDay) FogDay else FogNight
            Smoke -> if (forDay) FogDay else FogNight
            Haze -> if (forDay) FogDay else FogNight
            Dust -> if (forDay) FogDay else FogNight
            Fog -> if (forDay) FogDay else FogNight
            Sand -> if (forDay) FogDay else FogNight
            Ash -> if (forDay) FogDay else FogNight
            Squall -> if (forDay) FogDay else FogNight
            Tornado -> if (forDay) FogDay else FogNight
        }
    }
}