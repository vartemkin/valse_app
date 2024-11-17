package com.example.valseapp.media

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.valseapp.R
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class Player @Inject constructor(@ActivityContext context: Context) {

    private lateinit var controller: MediaController

    init {
        val sessionToken = SessionToken(context, ComponentName(context, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            // MediaController is available here with controllerFuture.get()
            controller = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }

    // TODO: добавить fun release() для завершения сессии контроллера по lifecycle activity

    /**
     * Все операции с контроллером только через этот метод, иначе будет IllegalStateException
     * видимая только в логах.
     */
    private inline fun MediaController.execute(
        crossinline task: (controller: MediaController) -> Unit
    ) {
        Handler(this.applicationLooper).post { task(this) }
    }

    fun addTrack(track: MediaItem) {
        controller.execute {
            it.addMediaItem(track)
        }
    }

    fun play() {
        val trackUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .path(R.raw.mozart.toString())
            .build()
        controller.execute {
            it.setMediaItem(MediaItem.fromUri(trackUri))
            it.prepare()
            it.play()
        }
    }

    fun pause() {
        controller.execute { it.pause() }
    }

    fun stop() {
        controller.execute { it.stop() }
    }

    fun next() {
        controller.execute { it.seekToNextMediaItem() }
    }

    fun previous() {
        controller.execute { it.seekToPreviousMediaItem() }
    }
}