package app.atomofiron.workmanager.ui.weather.state

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import app.atomofiron.workmanager.R

sealed class WeatherType(
    @DrawableRes
    val backgroundResId: Int,
    @RawRes
    val soundResId: Int,
) {
    companion object {
        val values = arrayOf(
            Unknown, Hot, PartlySunny, ClearDay, ClearNight, SnowDay, SnowNight, RainDay, RainNight, PartlyCloudDay,
            PartlyCloudNight, CloudyDay, CloudyNight, FogDay, FogNight, ThunderstormDay, ThunderstormNight
        )
    }

    object Unknown : WeatherType(0, 0)

    object Hot : WeatherType(R.drawable.weather_hot, R.raw.sound_hot)
    object PartlySunny : WeatherType(R.drawable.weather_partly_sunny, R.raw.sound_sunny)

    object ClearDay : WeatherType(R.drawable.weather_sunny, R.raw.sound_sunny)
    object ClearNight : WeatherType(R.drawable.weather_clear, R.raw.sound_clouds)

    object SnowDay : WeatherType(R.drawable.weather_snow_day, R.raw.sound_snow)
    object SnowNight : WeatherType(R.drawable.weather_snow_night, R.raw.sound_snow)

    object RainDay : WeatherType(R.drawable.weather_rain_day, R.raw.sound_showers)
    object RainNight : WeatherType(R.drawable.weather_rain_night, R.raw.sound_showers)

    object PartlyCloudDay : WeatherType(R.drawable.weather_partly_cloud, R.raw.sound_clouds)
    object PartlyCloudNight : WeatherType(R.drawable.weather_partly_cloud_night, R.raw.sound_clouds)

    object CloudyDay : WeatherType(R.drawable.weather_cloudy_day, R.raw.sound_clouds)
    object CloudyNight : WeatherType(R.drawable.weather_cloudy_night, R.raw.sound_clouds)

    object FogDay : WeatherType(R.drawable.weather_fog_day, R.raw.sound_fog)
    object FogNight : WeatherType(R.drawable.weather_fog_night, R.raw.sound_fog)

    object ThunderstormDay : WeatherType(R.drawable.weather_thunderstorm_day, R.raw.sound_thunder)
    object ThunderstormNight : WeatherType(R.drawable.weather_thunderstorm_night, R.raw.sound_thunder)
}