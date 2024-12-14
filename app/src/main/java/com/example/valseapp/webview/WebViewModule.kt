package com.example.valseapp.webview

import android.content.Context
import android.os.Handler
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.valseapp.media.MediaPlayer
import org.json.JSONArray
import org.json.JSONObject

class WebViewModule(val context: Context, val webView: WebView) {

    val webViewBridge: WebViewBridge;
    val mediaPlayer = MediaPlayer(context);

    init {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                val shouldBlock = false // filterNetworkRequests(webView, request);
                Log.d("WebView", request.url.toString())
                if (shouldBlock) {
                    return WebResourceResponse("text/javascript", "UTF-8", null)
                }
                return null
            }
//            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//                return filterNetworkRequests(view, request)
//            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("WebView", consoleMessage.message())
                return true
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                val resources = request?.resources
                resources?.forEach { resource ->
                    if (PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID == resource) {
                        request.grant(resources)
                        return
                    }
                }
                super.onPermissionRequest(request)
            }
        }

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.loadUrl("https://valse.me/")

        webViewBridge = WebViewBridge(this);
        webView.addJavascriptInterface(webViewBridge, "vaBridge");

    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun sendMessage(type: String, args: Array<String> = arrayOf()) {
        val arr = JSONArray(args);
        val obj = JSONObject();
        obj.put("type", type);
        obj.put("args", arr);
        webView.post {
            webView.evaluateJavascript("window.vaAndroidToBrowser($obj);", null)
        }
    }

    fun receiveMessage(json: String) {
        try {
            val obj = JSONObject(json);
            val type = obj.getString("type");
            when (type) {
                "toast" -> {
                    val text = obj.getString("text");
                    showToast(text)
                }
            }
            Handler(context.mainLooper).post {
                mediaPlayer.processMessage(type, obj);
            }
        } catch (e: Exception) {
            sendMessage("error", arrayOf(e.toString()))
        }
    }
}