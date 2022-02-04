package com.cuh.autodraw

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.cuh.autodraw.databinding.ActivityMainBinding
import com.cuh.autodraw.parser.ADJavaScriptInterface
import com.cuh.autodraw.parser.JCInterfaceCallback

class MainActivity : AppCompatActivity(), JCInterfaceCallback {

    private lateinit var binding:ActivityMainBinding

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
                super.onPageFinished(view, url)
                view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);");
            }
        }
        binding.mWebView.settings.javaScriptEnabled = true
        val adJavaScriptInterface = ADJavaScriptInterface(this)
        binding.mWebView.addJavascriptInterface(adJavaScriptInterface, "Android")

        binding.mWebView.loadUrl("https://m.naver.com")

    }

    override fun htmlDataMade(html:String) {
        binding.mWebView.loadUrl()
    }
}