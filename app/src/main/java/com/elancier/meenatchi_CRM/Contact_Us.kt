package com.elancier.meenatchi_CRM

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class Contact_Us : AppCompatActivity() {
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    lateinit var help_webview: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact__us)

        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        var religion=pref!!.getString("username","")
        var url=""

        if(religion=="Hindu") {
            url = "http://arsmatrimony.com/contact.php?religion=$religion&app=1"
        }
        else if((religion=="Christian")){
            url = "http://arsmatrimony.com/christian_contact.php?religion=$religion&app=1"

        }
        else if((religion=="Muslim")){
            url = "http://arsmatrimony.com/muslim_contact.php?religion=$religion&app=1"

        }

        help_webview=findViewById(R.id.webview) as WebView

        help_webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        })
        help_webview.getSettings().setJavaScriptEnabled(true)



        help_webview.loadUrl(url)
    }
}
