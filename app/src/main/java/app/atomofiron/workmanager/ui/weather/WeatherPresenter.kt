package app.atomofiron.workmanager.ui.weather

import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.*
import app.atomofiron.workmanager.App
import app.atomofiron.workmanager.ui.weather.state.WeatherInfo
import app.atomofiron.workmanager.utils.PoolPlayer
import app.atomofiron.workmanager.utils.SoundPlayer
import app.atomofiron.workmanager.worker.CachingWeatherWorker
import app.atomofiron.workmanager.worker.RequestingWeatherWorker
import app.atomofiron.workmanager.worker.WorkerApi
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherPresenter(
    private val viewModel: WeatherViewModel,
) {

    private val workManager = WorkManager.getInstance(App.context)
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
        val requesting = OneTimeWorkRequestBuilder<RequestingWeatherWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setInputData(RequestingWeatherWorker.getInputData(fakeInfo = withFeedback))
            .build()
        workManager
            .beginUniqueWork(WorkerApi.REQUESTING_NAME, ExistingWorkPolicy.REPLACE, requesting)
            .then(OneTimeWorkRequest.from(CachingWeatherWorker::class.java))
            .enqueue()

        observeRequest(requesting, withFeedback)
    }

    private fun observeRequest(request: OneTimeWorkRequest, withFeedback: Boolean) {
        viewModel.viewModelScope.launch {
            workManager.getWorkInfoByIdLiveData(request.id)
                .asFlow()
                .collect {
                    when (it?.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            viewModel.updateState {
                                copy(isRefreshing = false, isError = false, weatherInfo = WeatherInfo.fromData(it.outputData))
                            }
                            playWeather(withFeedback)
                        }
                        WorkInfo.State.FAILED -> viewModel.showError()
                    }
                }
        }
    }

    private fun playWeather(withFeedback: Boolean) {
        soundPlayer.play(viewModel.soundResId)
        viewModel.videoResId?.takeIf { it != 0 }?.let { videoResId ->
            playVideo(videoResId)
        }
        if (withFeedback) poolPlayer.playUpdating()
    }

    private fun playVideo(rawId: Int) {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            val uri = Uri.Builder()
                .scheme("android.resource")
                .authority(App.context.packageName)
                .path(rawId.toString())
                .build()
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }
}