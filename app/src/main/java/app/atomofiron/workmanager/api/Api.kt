package app.atomofiron.workmanager.api

import app.atomofiron.workmanager.BuildConfig
import app.atomofiron.workmanager.api.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    companion object {
        const val DEFAULT_LATITUDE = 44.799600
        const val DEFAULT_LONGITUDE = 20.452377
        const val DEFAULT_LANG = "ru"
        const val QUERY_LATITUDE = "lat"
        const val QUERY_LONGITUDE = "lon"
        const val QUERY_API_KEY = "appid"
        const val QUERY_UNITS = "units"
        const val QUERY_LANG = "lang"
    }
    @GET("data/2.5/weather")
    fun weather(
        @Query(QUERY_LATITUDE) latitude: Double = DEFAULT_LATITUDE,
        @Query(QUERY_LONGITUDE) longitude: Double = DEFAULT_LONGITUDE,
        @Query(QUERY_API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(QUERY_UNITS) units: String = WeatherUnits.Celsius.value,
        @Query(QUERY_LANG) lang: String = DEFAULT_LANG,
    ): Call<WeatherResponse?>
}