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
import androidx.appcompat.app.AppCompatActivity
import com.example.valseapp.databinding.ActivityMainBinding
import com.example.valseapp.webview.WebViewBridge
import com.example.valseapp.webview.WebViewModule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var webViewModule: WebViewModule;
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        webViewModule = WebViewModule(this, binding.webView)
    }

    override fun onBackPressed() {
        super.onBackPressed();
        webViewModule.sendMessage("back_pressed");
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webViewModule.sendMessage("KEYCODE_BACK");
        }
        return super.onKeyDown(keyCode, event)
    }
}