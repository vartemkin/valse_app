package com.example.valseapp

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppInterface(private val mContext: Context) {
    @JavascriptInterface
    fun methodToBeCalledFromJavascript(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show()
    }
}