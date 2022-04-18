package app.atomofiron.workmanager.ui.weather

import androidx.core.os.LocaleListCompat
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
    private val locale get() = LocaleListCompat.getDefault().get(0)

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

    fun randomInfo() {
        val index = (Math.random() * WeatherType.values.size).toInt()
        val wtf = WeatherType.values[index]
        val info = WeatherInfo(
            weatherType = wtf,
            weatherDescription = "Фиг пойми",
            cityName = "Ростов-на-Волге",
            temperature = 30,
            feelsLike = 99,
            windSpeed = (Math.random() * 30).toInt(),
            windDeg = (Math.random() * 360).toInt(),
        )
        updateState {
            copy(isRefreshing = false, isError = false, weatherInfo = info)
        }
    }

    fun updateInfo(response: Response<WeatherResponse?>) {
        val weatherResponse = response.body()
        val weatherData = weatherResponse?.weather?.firstOrNull()
        if (response.isSuccessful && weatherResponse != null && weatherData != null) {
            val type = MainWeather.getType(weatherData.id, forDay = true)
            val info = WeatherInfo(
                weatherType = type,
                weatherDescription = weatherData.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() },
                cityName = weatherResponse.name,
                temperature = weatherResponse.main.temp.toInt(),
                feelsLike = weatherResponse.main.feels_like.toInt(),
                windSpeed = weatherResponse.wind.speed.toInt(),
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