package com.example.valseapp.media

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors

class AudioPlayer(context: Context) {

    private lateinit var mediaController: MediaController

    init {
        val sessionToken = SessionToken(context, ComponentName(context, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            // MediaController is available here with controllerFuture.get()
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())

//        mediaController.addListener(object : Player.Listener {
//
//            override fun onPlaybackStateChanged(state: Int) {
//                super.onPlaybackStateChanged(state);
//
//                val currentPosition = mediaController.currentPosition;
//                Log.d("D_D_D", Integer.toString(state));
//
//                if (state == Player.STATE_ENDED) {
//                    // Плавно уменьшаем громкость и переключаем на следующий трек
//                    // fadeOutAndNextTrack()
//                }
//
//            }
//
//            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK) {
//                    // Когда трек сменился, плавно увеличиваем громкость
//                    // fadeIn()
//                }
//            }
//        })
    }

    fun senPos() {
        val position = mediaController.contentPosition;
        val duration = mediaController.contentDuration;
//        val dd = mediaController.bufferedPosition;
    }

    fun play(id: String, url: String, title: String) {

        val mediaItem = MediaItem.fromUri(url);
        mediaController.setMediaItem(mediaItem)
        mediaController.prepare()

        // Добавляем слушателя для отслеживания позиции и других событий
        mediaController.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                // Вызывается, когда изменяется состояние воспроизведения
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                // Здесь можно отслеживать изменение состояния (например, PLAYING, PAUSED)
            }

//            override fun onPositionDiscontinuity(reason: Int) {
//                super.onPositionDiscontinuity(reason)
//                // Когда происходит разрыв в позиции воспроизведения (например, перемотка)
//            }

//            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                super.onPlayerStateChanged(playWhenReady, playbackState)
//                // Получаем текущую позицию воспроизведения
//                val currentPosition = mediaController.playbackState.position
//                // Позиция в миллисекундах
//                println("Current Position: $currentPosition")
//            }
        })

        this.mediaController.play();
    }

    fun pause(id: String) {
        // controller.execute { it.pause() }
    }

    fun seek(id: String, pos: String) {
        //  controller.execute { it.seekTo(15 * 1000) }
    }

    fun stop() {
        //   controller.execute { it.stop() }
    }

    fun next() {
        //  controller.execute { it.seekToNextMediaItem() }
    }

    fun processMessage(type: String, argv: Array<String>) {
        when (type) {
            "play" -> play(argv[0], argv[1], argv[2])
            "pause" -> pause(argv[0])
            "seek" -> seek(argv[0], argv[1])
        }
    }
}