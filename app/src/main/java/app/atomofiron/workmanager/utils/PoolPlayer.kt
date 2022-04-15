package app.atomofiron.workmanager.utils

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import app.atomofiron.workmanager.R

class PoolPlayer(context: Context) {
    private val pool = SoundPool(3, AudioManager.STREAM_MUSIC, 100)
    private var pulldownId = pool.load(context, R.raw.pulldown, 1)
    private var updatingId = pool.load(context, R.raw.updating, 1)

    fun playPullDown() = pool.play(pulldownId, 1f, 1f, 1, 0, 1f)

    fun playUpdating() = pool.play(updatingId, 1f, 1f, 1, 0, 1f)
}