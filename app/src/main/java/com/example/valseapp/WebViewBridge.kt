package com.example.valseapp

import android.webkit.JavascriptInterface
import android.widget.Toast

class WebViewBridge(private val mContext: MainActivity) {
    @JavascriptInterface
    fun helloKatya() {
        Toast.makeText(mContext, "11 22 33", Toast.LENGTH_LONG).show()
    }

    @JavascriptInterface
    fun helloMasha() {
        Toast.makeText(mContext, "44 55 66", Toast.LENGTH_LONG).show()
    }

    @JavascriptInterface
    fun reloadList() {
        mContext.execJsCode("      window.reloadListCallback([" +
                "        { label: 'label1', value: 'value1', selected: true }," +
                "        { label: 'label2', value: 'value2', selected: false }," +
                "        { label: 'label3', value: 'value3', selected: false }" +
                "      ]);");
    }
}