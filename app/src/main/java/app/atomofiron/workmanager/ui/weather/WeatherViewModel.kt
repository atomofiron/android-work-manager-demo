package app.atomofiron.workmanager.ui.weather

import androidx.lifecycle.ViewModel
import app.atomofiron.workmanager.dataFlow
import app.atomofiron.workmanager.ui.weather.state.WeatherState
import app.atomofiron.workmanager.value
import com.google.android.exoplayer2.ExoPlayer

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
}