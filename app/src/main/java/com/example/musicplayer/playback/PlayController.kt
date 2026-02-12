package com.example.musicplayer.playback

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    data class PlayerState(
        val isPlaying: Boolean = false,
        val currentMediaItemIndex: Int = 0,
        val duration: Long = 0,
        val currentPosition: Long = 0,
        val bufferedPosition: Long = 0,
        val mediaItemsCount: Int = 0
    )

    fun initialize() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, MusicPlaybackService::class.java)
        )

        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener(
            {
                mediaController = controllerFuture?.get()
                setupPlayerListener()
                updatePlayerState()
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun setupPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.value = _playerState.value.copy(isPlaying = isPlaying)
            }

            override fun onMediaItemTransition(mediaItem: androidx.media3.common.MediaItem?, reason: Int) {
                updatePlayerState()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                updatePlayerState()
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                updatePlayerState()
            }
        })
    }

    private fun updatePlayerState() {
        mediaController?.let { player ->
            _playerState.value = PlayerState(
                isPlaying = player.isPlaying,
                currentMediaItemIndex = player.currentMediaItemIndex,
                duration = player.duration.coerceAtLeast(0),
                currentPosition = player.currentPosition.coerceAtLeast(0),
                bufferedPosition = player.bufferedPosition.coerceAtLeast(0),
                mediaItemsCount = player.mediaItemCount
            )
        }
    }

    fun play() {
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun stop() {
        mediaController?.stop()
    }

    fun playPause() {
        mediaController?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.play()
            }
        }
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    fun seekToNext() {
        mediaController?.seekToNext()
    }

    fun seekToPrevious() {
        mediaController?.seekToPrevious()
    }

    fun playAtIndex(index: Int) {
        mediaController?.seekToDefaultPosition(index)
        mediaController?.play()
    }

    fun release() {
        MediaController.releaseFuture(controllerFuture!!)
        mediaController = null
    }

    fun updatePosition() {
        updatePlayerState()
    }
}