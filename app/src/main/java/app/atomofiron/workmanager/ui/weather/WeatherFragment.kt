package app.atomofiron.workmanager.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.atomofiron.workmanager.R
import app.atomofiron.workmanager.collectOnView
import app.atomofiron.workmanager.databinding.WeatherFragmentBinding
import app.atomofiron.workmanager.ui.weather.state.WeatherState
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var binding: WeatherFragmentBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var presenter: WeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        presenter = viewModel.presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherFragmentBinding.inflate(layoutInflater)
        binding.srl.setOnRefreshListener {
            presenter.onSwipeToRefresh()
        }
        val defaultStartOffset = binding.srl.progressViewStartOffset
        val defaultEndOffset = binding.srl.progressViewEndOffset
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val end = defaultEndOffset + insets.top
            binding.srl.setProgressViewOffset(false, defaultStartOffset, end)
            binding.includeHeader.root.updatePaddingRelative(top = insets.top)
            windowInsets
        }
        binding.spvMovie.player = viewModel.exoPlayer
        binding.spvMovie.useController = false
        binding.spvMovie.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnView(viewModel.state) { state ->
            render(state)
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onStopped()
    }

    private fun render(state: WeatherState) {
        val weatherInfo = state.weatherInfo
        binding.run {
            srl.isRefreshing = state.isRefreshing
            imNoInfo.isVisible = state.isError || weatherInfo == null
            imWeather.isVisible = weatherInfo != null
            includeHeader.run {
                root.isGone = weatherInfo == null
                if (weatherInfo != null) {
                    tvWeatherTemperature.text = getString(R.string.header_temperature, weatherInfo.temperature)
                    tvWeatherDescription.text = weatherInfo.weatherDescription
                    tvWeatherSubline.text = getString(R.string.header_subline, weatherInfo.cityName, weatherInfo.windSpeed)
                }
            }
            state.weatherInfo?.takeIf { !state.isRefreshing }?.let { weather ->
                imWeather.setImageResource(weather.weatherType.backgroundResId)

                val topLine = AnimationUtils.loadAnimation(context, R.anim.weather_info_appearance)
                includeHeader.tvWeatherTemperature.animation = topLine
                includeHeader.tvWeatherDescription.animation = topLine
                val subLine = AnimationUtils.loadAnimation(context, R.anim.weather_info_appearance)
                subLine.startOffset = topLine.duration / 2
                includeHeader.tvWeatherSubline.animation = subLine

                if (viewModel.weatherTypeWasChanged) {
                    val anim = AnimationUtils.loadAnimation(context, R.anim.weather_picture_appearance)
                    spvMovie.startAnimation(anim)
                }
            }
        }
    }
}