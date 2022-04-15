package app.atomofiron.workmanager.api

sealed class WeatherUnits(val value: String) {
    object Fahrenheit : WeatherUnits("imperial")
    object Celsius : WeatherUnits("metric")
    object Kelvin : WeatherUnits("standard")
}