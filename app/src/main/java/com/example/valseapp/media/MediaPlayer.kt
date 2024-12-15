package com.example.valseapp.media

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.valseapp.webview.WebViewModule
import com.google.common.util.concurrent.MoreExecutors
import org.json.JSONObject

class MediaPlayer(context: Context) {

    private lateinit var mediaController: MediaController
    private lateinit var mediaPlayerListener: MediaPlayerListener;

    init {
        val sessionToken = SessionToken(context, ComponentName(context, MediaPlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            // MediaController is available here with controllerFuture.get()
            mediaController = controllerFuture.get();
            mediaController.addListener(mediaPlayerListener);
            mediaPlayerListener.mediaController = mediaController;

        }, MoreExecutors.directExecutor())

        mediaPlayerListener = MediaPlayerListener();
    }

    fun getMediaPlayerListener(): MediaPlayerListener {
        return mediaPlayerListener;
    }


    fun createMediaItem(obj: JSONObject): MediaItem {
        val id = obj.getString("id");
        val url = obj.getString("url");

        val artist = obj.getString("artist");
        val title = obj.getString("title");
        val metadata = MediaMetadata.Builder().setArtist(artist).setTitle(title).build();

        return MediaItem.Builder().setMediaId(id).setMediaMetadata(metadata).setUri(url).build();
    }

    fun processMessage(type: String, obj: JSONObject) {
        when (type) {
            "playlist" -> {
                val items = obj.getJSONArray("items");
                val mediaItems = List(items.length()) {
                    createMediaItem(items.getJSONObject(it))
                }
                mediaController.setMediaItems(mediaItems);
                mediaController.prepare()

                val index = obj.getInt("index");
                mediaController.seekTo(index, 0)

                mediaController.play()
            }

            "play" -> {
                mediaController.play()
            }

            "pause" -> {
                mediaController.pause();
            }

            "stop" -> {
                mediaController.stop();
            }

            "seek" -> {
                val position = obj.getLong("position");
                mediaController.seekTo(position);
            }
        }
    }
}