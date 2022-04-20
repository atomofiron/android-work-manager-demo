package app.atomofiron.workmanager.ui.weather

import androidx.lifecycle.ViewModel
import app.atomofiron.workmanager.dataFlow
import app.atomofiron.workmanager.ui.weather.state.WeatherState
import app.atomofiron.workmanager.value
import com.google.android.exoplayer2.ExoPlayer

class WeatherViewModel : ViewModel() {

    val presenter = WeatherPresenter(this)

    val state = dataFlow(WeatherState(isRefreshing = true, weatherInfo = null))
    lateinit var exoPlayer: ExoPlayer

    fun updateState(action: WeatherState.() -> WeatherState) {
        state.value = action(state.value)
    }

    fun showError() = updateState {
        copy(isRefreshing = false, weatherInfo = null)
    }
}