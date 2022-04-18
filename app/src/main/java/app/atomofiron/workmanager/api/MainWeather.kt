package app.atomofiron.workmanager.api

import app.atomofiron.workmanager.ui.weather.state.WeatherType
import app.atomofiron.workmanager.ui.weather.state.WeatherType.*

enum class MainWeather(val id: Int) {

    Unknown(-1), Thunderstorm(2), Drizzle(3), Rain(5), Snow(6), Mist(7), Smoke(7), Haze(7), Dust(7), Fog(7), Sand(7),
    Ash(7), Squall(7), Tornado(7), Clear(800), FewClouds(801), ScatteredClouds(802), BrokenClouds(803), OvercastClouds(804);

    companion object {

        private fun resolve(id: Int): MainWeather {
            return values().find { it.id == id } ?: values().find { it.id == id / 100 } ?: Unknown
        }

        fun getType(id: Int, forDay: Boolean): WeatherType = when (resolve(id)) {
            Unknown -> WeatherType.Unknown
            Drizzle -> if (forDay) RainDay else RainNight
            Rain -> if (forDay) RainDay else RainNight
            Thunderstorm -> if (forDay) ThunderstormDay else ThunderstormNight
            Snow -> if (forDay) SnowDay else SnowNight

            Mist -> if (forDay) FogDay else FogNight
            Smoke -> if (forDay) FogDay else FogNight
            Haze -> if (forDay) FogDay else FogNight
            Dust -> if (forDay) FogDay else FogNight
            Fog -> if (forDay) FogDay else FogNight
            Sand -> if (forDay) FogDay else FogNight
            Ash -> if (forDay) FogDay else FogNight
            Squall -> if (forDay) FogDay else FogNight
            Tornado -> if (forDay) FogDay else FogNight

            Clear -> if (forDay) ClearDay else ClearNight
            FewClouds -> if (forDay) ClearDay else ClearNight
            ScatteredClouds -> if (forDay) PartlySunny else PartlyCloudNight
            BrokenClouds -> if (forDay) PartlyCloudDay else PartlyCloudNight
            OvercastClouds -> if (forDay) CloudyDay else CloudyNight
        }
    }
}