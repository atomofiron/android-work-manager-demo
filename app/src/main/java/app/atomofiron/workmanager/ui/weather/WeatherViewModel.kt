package app.atomofiron.workmanager.ui.weather

import androidx.lifecycle.ViewModel
import app.atomofiron.workmanager.api.MainWeather
import app.atomofiron.workmanager.api.response.WeatherResponse
import app.atomofiron.workmanager.dataFlow
import app.atomofiron.workmanager.ui.weather.state.WeatherInfo
import app.atomofiron.workmanager.ui.weather.state.WeatherState
import app.atomofiron.workmanager.ui.weather.state.WeatherType
import app.atomofiron.workmanager.value
import com.google.android.exoplayer2.ExoPlayer
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    val presenter = WeatherPresenter(this)

    val state = dataFlow(WeatherState(isRefreshing = true, isError = false, weatherInfo = null))
    lateinit var exoPlayer: ExoPlayer

    var soundResId: Int? = null
        private set
    var videoResId: Int? = null
        private set
    var weatherTypeWasChanged = true
        private set

    fun updateState(action: WeatherState.() -> WeatherState) {
        val weatherType = state.value.weatherInfo?.weatherType
        val new = action(state.value)
        weatherTypeWasChanged = weatherType !== new.weatherInfo?.weatherType
        soundResId = new.weatherInfo?.weatherType?.soundResId?.takeIf { weatherTypeWasChanged }
        videoResId = new.weatherInfo?.weatherType?.videoResId?.takeIf { weatherTypeWasChanged }
        state.value = new
    }

    fun showError() = updateState {
        copy(isRefreshing = false, isError = true, weatherInfo = null)
    }

    fun randomType() {
        val info = WeatherInfo(
            cityName = "",
            weatherType = WeatherType.values[(Math.random() * WeatherType.values.size).toInt()],
            temperature = 0f,
            feelsLike = 0f,
            windSpeed = 0f,
            windDeg = 0,
        )
        updateState {
            copy(isRefreshing = false, isError = false, weatherInfo = info)
        }
    }

    fun updateInfo(response: Response<WeatherResponse?>) {
        val weatherResponse = response.body()
        val weatherData = weatherResponse?.weather?.firstOrNull()
        if (response.isSuccessful && weatherResponse != null && weatherData != null) {
            val type = MainWeather.getType(weatherData.main, weatherData.description, forDay = true)
            val info = WeatherInfo(
                cityName = weatherResponse.name,
                weatherType = type,
                temperature = weatherResponse.main.temp,
                feelsLike = weatherResponse.main.feels_like,
                windSpeed = weatherResponse.wind.speed,
                windDeg = weatherResponse.wind.deg,
            )
            updateState {
                copy(isRefreshing = false, isError = false, weatherInfo = info)
            }
        } else {
            showError()
        }
    }
}