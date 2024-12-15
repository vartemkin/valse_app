package com.example.valseapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.valseapp.databinding.ActivityMainBinding
import com.example.valseapp.webview.WebViewModule
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

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
        val obj = JSONObject();
        webViewModule.sendMessage("BackPressed", obj);
        return super.onBackPressed();
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val obj = JSONObject();
            webViewModule.sendMessage("KeyDownBack", obj);
        }
        return super.onKeyDown(keyCode, event)
    }
}