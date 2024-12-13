package com.example.valseapp.webview

import android.webkit.JavascriptInterface

class WebViewBridge(val webViewModule: WebViewModule) {
    @JavascriptInterface
    fun vaBrowserToAndroid(json: String) {
        webViewModule.receiveMessage(json);
    }
}