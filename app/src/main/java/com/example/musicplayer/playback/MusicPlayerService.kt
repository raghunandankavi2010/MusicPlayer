package com.example.musicplayer.playback

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.musicplayer.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(UnstableApi::class)
class MusicPlaybackService : MediaSessionService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var mediaSession: MediaSession? = null

    @Inject
    lateinit var musicRepository: com.example.musicplayer.data.MusicRepository

    companion object {
        const val CHANNEL_ID = "music_playback_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .build()

        // Load playlist
        val songs = musicRepository.getPlaylist()
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setMediaId(song.id)
                .setUri(song.mediaUrl)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .setArtworkUri(android.net.Uri.parse(song.artworkUrl))
                        .build()
                )
                .build()
        }

        player.setMediaItems(mediaItems)
        player.prepare()

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MusicSessionCallback())
            .setSessionActivity(getMainActivityPendingIntent())
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Music Playback"
            val descriptionText = "Shows notifications for music playback"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getMainActivityPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player
        if (player?.playWhenReady != true || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        serviceScope.cancel()
        super.onDestroy()
    }
}
