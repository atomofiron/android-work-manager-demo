package app.atomofiron.workmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import app.atomofiron.workmanager.R
import app.atomofiron.workmanager.ui.weather.WeatherFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherFragment.newInstance())
                .commitNow()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}