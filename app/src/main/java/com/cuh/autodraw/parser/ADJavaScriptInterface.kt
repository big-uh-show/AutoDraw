package com.cuh.autodraw.parser

import android.webkit.JavascriptInterface

class ADJavaScriptInterface(private val callback: JCInterfaceCallback) {

    @JavascriptInterface
    fun getHtml(html:String){
        callback.htmlDataMade(html)
    }
}