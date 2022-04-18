package app.atomofiron.workmanager.ui.weather

import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.viewModelScope
import app.atomofiron.workmanager.App
import app.atomofiron.workmanager.api.WeatherService
import app.atomofiron.workmanager.api.response.WeatherResponse
import app.atomofiron.workmanager.utils.PoolPlayer
import app.atomofiron.workmanager.utils.SoundPlayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Retrofit

@Suppress("JSON_FORMAT_REDUNDANT", "OPT_IN_USAGE")
class WeatherPresenter(
    private val viewModel: WeatherViewModel,
) {
    private val factory = Json {
        ignoreUnknownKeys = true
    }.asConverterFactory(MediaType.get("application/json"))

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(factory)
        .build()

    private val service: WeatherService = retrofit.create(WeatherService::class.java)
    private var call: Call<WeatherResponse?>? = null
    private val soundPlayer = SoundPlayer(App.context)
    private val poolPlayer = PoolPlayer(App.context)
    private val exoPlayer = ExoPlayer.Builder(App.context)
        .setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        .build()

    init {
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        viewModel.exoPlayer = exoPlayer
        refresh(withFeedback = false)
    }

    fun onSwipeToRefresh() {
        poolPlayer.playPullDown()
        viewModel.updateState {
            copy(isRefreshing = true)
        }
        refresh(withFeedback = true)
    }

    fun onStopped() = soundPlayer.cancel()

    private fun refresh(withFeedback: Boolean) {
        call?.cancel()
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            if (true) {
                delay(1000)
                viewModel.randomType()
                soundPlayer.play(viewModel.soundResId)
                viewModel.videoResId?.takeIf { it != 0 }?.let { videoResId ->
                    playVideo(videoResId)
                }
                if (withFeedback) poolPlayer.playUpdating()
            } else {
                val call = service.weather()
                this@WeatherPresenter.call = call
                val response = call.execute()
                if (!call.isCanceled) {
                    viewModel.updateInfo(response)
                    soundPlayer.play(viewModel.soundResId)
                    if (withFeedback) poolPlayer.playUpdating()
                }
            }
        }
    }

    private fun playVideo(rawId: Int) {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            val uri = Uri.Builder()
                .scheme("android.resource")
                .authority(App.context.packageName)
                .path(rawId.toString())
                .build()
            val mediaItem: MediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }
}