package app.atomofiron.workmanager.ui.weather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import androidx.core.view.*
import app.atomofiron.workmanager.collectOnView
import app.atomofiron.workmanager.databinding.WeatherFragmentBinding
import app.atomofiron.workmanager.ui.weather.state.WeatherState

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
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val end = defaultEndOffset + insets.top
            binding.srl.setProgressViewOffset(false, defaultStartOffset, end)
            windowInsets
        }
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
        binding.run {
            srl.isRefreshing = state.isRefreshing
            imNoInfo.isVisible = state.isError || state.weatherInfo == null
            imWeather.isVisible = state.weatherInfo != null
            state.weatherInfo?.let { weather ->
                imWeather.setImageResource(weather.weatherType.backgroundResId)
                if (viewModel.weatherTypeWasChanged) {
                    val anim = AlphaAnimation(0f, 1f)
                    anim.duration = 300
                    anim.interpolator = AccelerateInterpolator()
                    anim.fillBefore = true
                    anim.fillAfter = true
                    imWeather.startAnimation(anim)
                }
            }
        }
    }
}