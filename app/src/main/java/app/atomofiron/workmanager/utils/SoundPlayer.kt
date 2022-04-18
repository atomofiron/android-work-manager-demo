package app.atomofiron.workmanager.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class SoundPlayer(
    private val context: Context,
) {
    companion object {
        private const val VOLUME = 0.5f
    }
    private var lastSoundId = 0
    private var currentSoundId = 0
    private var player: MediaPlayer? = null

    fun play(@RawRes rawId: Int?) = when (rawId) {
        currentSoundId -> Unit
        null -> Unit
        0 -> cancel()
        else -> {
            lastSoundId = rawId
            cancel()
            currentSoundId = rawId
            val player = MediaPlayer.create(context, rawId)
            player.setOnCompletionListener {
                cancel()
            }
            player.setVolume(VOLUME, VOLUME)
            player.start()
            this.player = player
        }
    }

    fun cancel() {
        currentSoundId = 0
        player?.pause()
        player?.reset()
        player?.release()
        player = null
    }
}