package com.cuh.autodraw.parser

import android.util.Log
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


class JsoupHtmlParser {


    suspend fun initHtml(url: String): String {
       val mJsoup = Jsoup.connect(url)
        Log.e("TEST", mJsoup.get().toString())
        return mJsoup.get().toString()
    }


    fun parseHtml(url:String):String{
        val mJsoup = Jsoup.connect(url)
        val data = mJsoup.get()
        Log.e("TEST", data.toString())
        val meta = data.getElementsByAttributeValueContaining("content","Google Docs")

        if(meta.size > 0){
            val checkBoxes = data.getElementsByAttributeValueContaining("aria-checked","false")
            for(checkBox:Element in checkBoxes){
                data.getElementById(checkBox.id()).
            }
        }

       return data.toString()
    }
    private fun deletePopupFunction(doc : Document){
        doc.head().allElements
    }
}