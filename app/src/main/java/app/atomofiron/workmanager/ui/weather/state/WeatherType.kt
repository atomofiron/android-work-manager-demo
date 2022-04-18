package app.atomofiron.workmanager.ui.weather.state

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import app.atomofiron.workmanager.R

sealed class WeatherType(
    @DrawableRes
    val backgroundResId: Int,
    @RawRes
    val soundResId: Int,
    @RawRes
    val videoResId: Int,
) {
    companion object {
        // в котлине баг, 3й элемент просто null
        val values get() = arrayOf(
            Unknown, Hot, PartlySunny, ClearDay, ClearNight, SnowDay, SnowNight, RainDay, RainNight, PartlyCloudDay,
            PartlyCloudNight, CloudyDay, CloudyNight, FogDay, FogNight, ThunderstormDay, ThunderstormNight
        )
    }

    object Unknown : WeatherType(0, 0, 0)

    object Hot : WeatherType(R.drawable.weather_hot, R.raw.sound_hot, R.raw.weather_hot)
    object PartlySunny : WeatherType(R.drawable.weather_partly_sunny, R.raw.sound_sunny, R.raw.weather_sunny)

    object ClearDay : WeatherType(R.drawable.weather_sunny, R.raw.sound_sunny, R.raw.weather_sunny)
    object ClearNight : WeatherType(R.drawable.weather_clear, R.raw.sound_clouds, R.raw.weather_clear)

    object SnowDay : WeatherType(R.drawable.weather_snow_day, R.raw.sound_snow, R.raw.weather_snow_day)
    object SnowNight : WeatherType(R.drawable.weather_snow_night, R.raw.sound_snow, R.raw.weather_snow_night)

    object RainDay : WeatherType(R.drawable.weather_rain_day, R.raw.sound_showers, R.raw.weather_rain)
    object RainNight : WeatherType(R.drawable.weather_rain_night, R.raw.sound_showers, R.raw.weather_rain_night)

    object PartlyCloudDay : WeatherType(R.drawable.weather_partly_cloud, R.raw.sound_clouds, R.raw.weather_partly_cloud)
    object PartlyCloudNight : WeatherType(R.drawable.weather_partly_cloud_night, R.raw.sound_clouds, R.raw.weather_partly_cloud_night)

    object CloudyDay : WeatherType(R.drawable.weather_cloudy_day, R.raw.sound_clouds, R.raw.weather_cloudy_day)
    object CloudyNight : WeatherType(R.drawable.weather_cloudy_night, R.raw.sound_clouds, R.raw.weather_cloudy_night)

    object FogDay : WeatherType(R.drawable.weather_fog_day, R.raw.sound_fog, R.raw.weather_fog_day)
    object FogNight : WeatherType(R.drawable.weather_fog_night, R.raw.sound_fog, R.raw.weather_fog_night)

    object ThunderstormDay : WeatherType(R.drawable.weather_thunderstorm_day, R.raw.sound_thunder, R.raw.weather_thunderstorm_day)
    object ThunderstormNight : WeatherType(R.drawable.weather_thunderstorm_night, R.raw.sound_thunder, R.raw.weather_thunderstorm_night)
}