package com.example.musicplayer.playback


import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MusicSessionCallback : MediaSession.Callback {

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
            .setAvailablePlayerCommands(
                MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS
                    .buildUpon()
                    .build()
            )
            .setAvailableSessionCommands(
                MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS
                    .buildUpon()
                    .build()
            )
            .build()
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        // Handle post connect if needed
    }

    override fun onDisconnected(session: MediaSession, controller: MediaSession.ControllerInfo) {
        // Handle disconnect if needed
    }
}
