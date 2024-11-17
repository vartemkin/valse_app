package com.example.valseapp.webview

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.example.valseapp.media.Player
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import androidx.media3.common.MediaItem
import com.example.valseapp.R

@ActivityScoped
class WebViewBridge @Inject constructor(
    @ActivityContext private val context: Context,
    private val jsExecutor: JsExecutor,
    private val player: Player
) {

    @JavascriptInterface
    fun helloKatya() {
        Toast.makeText(context, "11 22 33", Toast.LENGTH_LONG).show()
        player.play()
    }

    @JavascriptInterface
    fun helloMasha() {
        Toast.makeText(context, "44 55 66", Toast.LENGTH_LONG).show()
        player.stop()
    }

    @JavascriptInterface
    fun reloadList() {
        jsExecutor.execJsCode("      window.reloadListCallback([" +
                "        { label: 'label1', value: 'value1', selected: true }," +
                "        { label: 'label2', value: 'value2', selected: false }," +
                "        { label: 'label3', value: 'value3', selected: false }" +
                "      ]);")
    }
}