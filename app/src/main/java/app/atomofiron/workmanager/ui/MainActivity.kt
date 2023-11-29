package app.atomofiron.workmanager.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import app.atomofiron.workmanager.R
import app.atomofiron.workmanager.ui.weather.WeatherFragment

private const val LOCATION_REQUEST_CODE = 1337

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            when {
                hasPermission() -> showFragment()
                else -> requestPermission()
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            requestCode != LOCATION_REQUEST_CODE -> Unit
            grantResults.first() != PackageManager.PERMISSION_GRANTED -> Unit
            else -> showFragment()
        }
    }

    private fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("NewApi")
    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, WeatherFragment.newInstance())
            .commitNow()
    }
}