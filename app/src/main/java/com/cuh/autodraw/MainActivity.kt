package com.cuh.autodraw

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.cuh.autodraw.databinding.ActivityMainBinding
import com.cuh.autodraw.parser.ADJavaScriptInterface
import com.cuh.autodraw.parser.JCInterfaceCallback
import com.cuh.autodraw.parser.JsoupHtmlParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity(), JCInterfaceCallback {

    private lateinit var binding:ActivityMainBinding

    private val BASE_URL = "https://www.luck-d.com/release-raffle/"
    private val jsoupHtmlParser = JsoupHtmlParser()

    private var prevPageUrl = ""
    private var currentPageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        initWebView()
    }


    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(){
        binding.mWebView.webViewClient = object:WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                //view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if(url != prevPageUrl) {
                    CoroutineScope(Dispatchers.IO).async {
                        val deferred = async {
                            url?.let {
                                jsoupHtmlParser.parseHtml(url)
                            }}
                        val doc = deferred.await()
                        doc?.let {
                            view?.post {
                               view.loadDataWithBaseURL(url, doc, "text/html", "UTF-8", null)
                            }
                        }

                        prevPageUrl = currentPageUrl
                        url?.let {
                            currentPageUrl = it
                        }
                    }
                }
            }
        }

        binding.mWebView.settings.javaScriptEnabled = true
        val adJavaScriptInterface = ADJavaScriptInterface(this)
        binding.mWebView.addJavascriptInterface(adJavaScriptInterface, "Android")

         CoroutineScope(Dispatchers.IO).async {
             val deferred = async {jsoupHtmlParser.initHtml(BASE_URL)}
             val doc = deferred.await()
             binding.mWebView.post {
                 currentPageUrl = BASE_URL
                 binding.mWebView.loadDataWithBaseURL(BASE_URL,doc,"text/html","UTF-8",null)
             }
        }
    }

    override fun htmlDataMade(html:String) {
       binding.mWebView.loadUrl(html)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(keyCode == KeyEvent.KEYCODE_BACK && binding.mWebView.canGoBack()){
            binding.mWebView.goBack()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
}