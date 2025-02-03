package com.example.valseapp.media

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MediaPlayerService : MediaSessionService() {

    private var mediaSession: MediaSession? = null;

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val cacheDataSourceFactory = CacheDataSourceFactory(this, 2000 * 1024 * 1024, 100 * 1024 * 1024);
        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this).setDataSourceFactory(cacheDataSourceFactory))
            .build();
        mediaSession = MediaSession.Builder(this, player).build()
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopSelf()
        }
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession
}