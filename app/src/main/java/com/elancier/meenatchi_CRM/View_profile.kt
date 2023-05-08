package com.elancier.meenatchi_CRM

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_view_profile.*

class View_profile : AppCompatActivity() {
    internal  var pref: SharedPreferences?=null

    internal  var editor: SharedPreferences.Editor?=null

    var religion=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        var religion=pref!!.getString("username","")
        var url=""
        var loginid=pref!!.getString("logid","")

        val intent=intent.extras
        var idval=intent!!.getString("idval").toString()



        if(loginid!!.isNotEmpty()) {



            if (religion == "Hindu") {
                url =
                    "http://arsmatrimony.com/profile_view.php?id=$idval&uid=$loginid&religion=$religion&app=1"

            } else if ((religion == "Christian")) {
                url = "http://arsmatrimony.com/profile_view.php?id=$idval&uid=$loginid&religion=$religion&app=1"

            } else if ((religion == "Muslim")) {
                url = "http://arsmatrimony.com/profile_view.php?id=$idval&uid=$loginid&religion=$religion&app=1"

            }

            wbvw.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return false
                }
            })
            wbvw.getSettings().setJavaScriptEnabled(true)







            wbvw.loadUrl(url)
        }
        else{

        }
    }
}
