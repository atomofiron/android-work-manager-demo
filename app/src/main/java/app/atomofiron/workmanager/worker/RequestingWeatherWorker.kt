package app.atomofiron.workmanager.worker

import android.content.Context
import androidx.core.os.LocaleListCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import app.atomofiron.workmanager.api.MainWeather
import app.atomofiron.workmanager.api.WeatherService
import app.atomofiron.workmanager.api.response.WeatherResponse
import app.atomofiron.workmanager.ui.weather.state.ErrorInfo
import app.atomofiron.workmanager.ui.weather.state.WeatherInfo
import app.atomofiron.workmanager.ui.weather.state.WeatherType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit

@Suppress("JSON_FORMAT_REDUNDANT", "OPT_IN_USAGE")
class RequestingWeatherWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    companion object {
        private val factory = Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType())

        private const val INPUT_FAKE = "INPUT_FAKE"

        fun getInputData(fakeInfo: Boolean): Data {
           return Data.Builder().putBoolean(INPUT_FAKE, fakeInfo).build()
        }

        fun getInputFake(data: Data): Boolean = data.getBoolean(INPUT_FAKE, false)
    }

    private val locale get() = LocaleListCompat.getDefault().get(0)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(factory)
        .client(OkHttpClient.Builder().run {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(interceptor)
            build()
        }).build()

    private val service: WeatherService = retrofit.create(WeatherService::class.java)
    private var call: Call<WeatherResponse?>? = null

    override suspend fun doWork(): Result {
        if (getInputFake(inputData)) {
            delay(1000)
            return when {
                isStopped -> Result.failure()
                else -> Result.success(randomWeatherInfo().toData())
            }
        }
        val call = service.weather()
        this.call = call
        // todo отменять запрос, если был отменён воркер
        val response = call.execute()
        val result = if (isStopped) {
            Result.failure()
        } else {
            val responseBody = response.body()
            val info = parseResponseBody(responseBody)
            when {
                info != null && response.isSuccessful -> Result.success(info.toData())
                else -> Result.failure(ErrorInfo(responseBody?.message).toData())
            }
        }
        return result
    }

    private fun parseResponseBody(responseBody: WeatherResponse?): WeatherInfo? {
        val weatherData = responseBody?.weather?.firstOrNull()
        return if (responseBody != null && weatherData != null) {
            val sys = responseBody.sys
            val isItDay = responseBody.dt in sys.sunrise..sys.sunset
            WeatherInfo(
                weatherType = MainWeather.getType(weatherData.id, isItDay),
                weatherDescription = weatherData.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() },
                cityName = responseBody.name,
                temperature = responseBody.main.temp.toInt(),
                feelsLike = responseBody.main.feels_like.toInt(),
                windSpeed = responseBody.wind.speed.toInt(),
                windDeg = responseBody.wind.deg,
            )
        } else {
            null
        }
    }

    private fun randomWeatherInfo(): WeatherInfo {
        val index = (Math.random() * WeatherType.values.size).toInt()
        return WeatherInfo(
            weatherType = WeatherType.getOrNull(index)!!,
            weatherDescription = "Фиг пойми",
            cityName = "Ростов-на-Волге",
            temperature = 30,
            feelsLike = 99,
            windSpeed = (Math.random() * 30).toInt(),
            windDeg = (Math.random() * 360).toInt(),
        )
    }
}