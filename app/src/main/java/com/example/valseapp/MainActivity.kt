package com.example.valseapp

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.valseapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    

    private var ololo = WebAppInterface(this@MainActivity);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = binding.webView


        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                val shouldBlock = false // filterNetworkRequests(webView, request);
                Log.d("WebView", request.url.toString())
                if (shouldBlock) {
                    return WebResourceResponse("text/javascript", "UTF-8", null)
                }
                return null
            }
//
//            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//                return filterNetworkRequests(view, request)
//            }
        };

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

        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webView.loadUrl("https://web.valse.me/")

        //webView.evaluateJavascript("window.alert(1);", null);
        //ololo.con = this@MainActivity;
        //webView.addJavascriptInterface(ololo, "NativeAndroid");
    }

//    override fun onBackPressed() {
//        val webView = binding.webView
//        if (webView.canGoBack()) {
//            super.onBackPressed()
//        };
//        Toast.makeText(this@MainActivity, "There is no back action", Toast.LENGTH_LONG).show()
//        return
//    }
override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    val webView = binding.webView;
    // Check whether the key event is the Back button and if there's history.
    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
        webView.goBack()
        return true
    }
    // If it isn't the Back button or there isn't web page history, bubble up to
    // the default system behavior. Probably exit the activity.
    return super.onKeyDown(keyCode, event)
}

}