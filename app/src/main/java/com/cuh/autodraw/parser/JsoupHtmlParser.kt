package com.cuh.autodraw.parser

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class JsoupHtmlParser {


    suspend fun initHtml(url: String): String {
       val mJsoup = Jsoup.connect(url)
        Log.e("TEST", mJsoup.get().toString())
        return mJsoup.get().toString()
    }


    fun parseHtml(url:String):String{
        val mJsoup = Jsoup.connect(url)
        val data = mJsoup.get().toString()
        Log.e("TEST", data)
       return data
    }
    private fun deletePopupFunction(doc : Document){
        doc.head().allElements
    }
}