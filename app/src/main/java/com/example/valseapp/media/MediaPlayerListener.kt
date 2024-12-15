package com.example.valseapp.media

import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.COMMAND_GET_CURRENT_MEDIA_ITEM
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.example.valseapp.webview.WebViewModule
import org.json.JSONObject

class MediaPlayerListener : Player.Listener {

    lateinit var webViewModule: WebViewModule; // for sendMessagev
    lateinit var mediaController: MediaController

    fun sendMessage(type: String, obj: JSONObject) {
        webViewModule.sendMessage(type, obj);
    }

    fun addPosInfo(obj: JSONObject) {
        obj.put("positionMs", mediaController.currentPosition)
        obj.put("bufferedMs", mediaController.bufferedPosition)
        obj.put("durationMs", mediaController.duration)
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        val obj = JSONObject();
        obj.put("periodCount", timeline.periodCount)
        obj.put("reason", reason)
        sendMessage("TimelineChanged", obj)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        val obj = JSONObject();
        obj.put("id", mediaItem?.mediaId)
        obj.put("reason", reason)
        sendMessage("MediaItemTransition", obj)
    }

    override fun onTracksChanged(tracks: Tracks) {
        val obj = JSONObject();
        obj.put("groups", tracks.groups.size)
        sendMessage("TracksChanged", obj);
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        val obj = JSONObject();
        obj.put("isLoading", isLoading)
        sendMessage("LoadingChanged", obj)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        val obj = JSONObject();
        var playbackStateName = "Unknown";
        when (playbackState) {
            Player.STATE_IDLE -> playbackStateName = "idle";
            Player.STATE_BUFFERING -> playbackStateName = "buffering";
            Player.STATE_READY -> playbackStateName = "ready";
            Player.STATE_ENDED -> playbackStateName = "ended";
        }
        obj.put("playbackState", playbackStateName)
        sendMessage("PlaybackStateChanged", obj)
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        val obj = JSONObject();
        obj.put("playWhenReady", playWhenReady)
        obj.put("reason", reason)
        sendMessage("PlayWhenReadyChanged", obj)
    }

    override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
        //
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val obj = JSONObject();
        obj.put("isPlaying", isPlaying)
        addPosInfo(obj);
        sendMessage("PlayingChanged", obj)
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        //
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        //
    }

    override fun onPlayerError(error: PlaybackException) {
        val obj = JSONObject();
        obj.put("errorCode", error.errorCode)
        obj.put("errorCodeName", error.errorCodeName)
        sendMessage("PlayerError", obj)
    }

    // ===============================================================================

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        val obj = JSONObject();
        addPosInfo(obj);
        obj.put("positionMs", newPosition.positionMs)
        obj.put("reason", reason)
        sendMessage("PositionDiscontinuity", obj);
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        val obj = JSONObject();
        obj.put("pitch", playbackParameters.pitch)
        obj.put("speed", playbackParameters.speed)
        sendMessage("PlaybackParametersChanged", obj);
    }

    override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
        if (availableCommands.contains(COMMAND_GET_CURRENT_MEDIA_ITEM)) {
            val obj = JSONObject();
            addPosInfo(obj);
            sendMessage("AvailableCommandsChanged", obj);

        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        val obj = JSONObject();
        obj.put("mediaType", mediaMetadata.mediaType)
        sendMessage("MediaMetadataChanged", obj);
    }

    override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
        val obj = JSONObject();
        sendMessage("PlaylistMetadataChanged", obj);
    }

    override fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {
        val obj = JSONObject();
        obj.put("flags", audioAttributes.flags)
        obj.put("usage", audioAttributes.usage)
        obj.put("usage", audioAttributes.contentType)
        sendMessage("AudioAttributesChanged", obj)
    }

    override fun onVolumeChanged(volume: Float) {
        val obj = JSONObject();
        obj.put("volume", volume)
        sendMessage("VolumeChanged", obj)
    }

    // ==========================================================================

    override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
        val obj = JSONObject();
        obj.put("minVolume", deviceInfo.minVolume)
        obj.put("maxVolume", deviceInfo.maxVolume)
        sendMessage("DeviceInfoChanged", obj)
    }

    override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
        val obj = JSONObject();
        obj.put("volume", volume)
        obj.put("muted", muted)
        sendMessage("DeviceVolumeChanged", obj)
    }
}