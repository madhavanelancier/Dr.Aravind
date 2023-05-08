package com.elancier.meenatchi_CRM

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import kotlinx.android.synthetic.main.activity_full_view.*
import kotlinx.android.synthetic.main.activity_prolist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FullView : AppCompatActivity() {
    var progressBar: ProgressBar? = null
       val activity = this
       var url=""
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_view)
           val ab = supportActionBar
           ab!!.setDisplayHomeAsUpEnabled(true)
           ab!!.setDisplayShowHomeEnabled(true)
           ab!!.title = "Price List"



        progressBar=findViewById(R.id.progressBar)



        wbview!!.setVerticalScrollBarEnabled(false)
        wbview!!.getSettings().builtInZoomControls = true
        wbview!!.getSettings().displayZoomControls = true
        wbview!!.getSettings().javaScriptEnabled=true
        wbview!!.getSettings().allowFileAccess = true
        wbview!!.getSettings().pluginState = WebSettings.PluginState.OFF
        wbview!!.setScrollbarFadingEnabled(false)
        wbview.getSettings().setUseWideViewPort(true);
        wbview.setInitialScale(10);
        wbview!!.setWebViewClient(HelloWebViewClient())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wbview!!.getSettings().setMediaPlaybackRequiresUserGesture(false)
        };
           if (Appconstands.net_status(activity)) {
               geturl()
               //getCloudinary()
           }else{
               Toast.makeText(
                   activity,
                   "Check your Network connection",
                   Toast.LENGTH_LONG
               ).show()

           }

           Handler().postDelayed(Runnable {
               progressBar!!.visibility=View.GONE
           },5000)


       }

       private inner class HelloWebViewClient : WebViewClient() {
           override fun shouldOverrideUrlLoading(
               view: WebView,
               url: String
           ): Boolean {
               view.loadUrl(url)
               println("loadurl" + url)
               runOnUiThread {
                   //  textView8.setText("Almost finish...")
                   progressBar!!.visibility=View.VISIBLE
               }
               return true
           }
           override fun onPageFinished(view: WebView?, url: String?) {
               // TODO Auto-generated method stub
               super.onPageFinished(view, url)
               // pdialog!!.dismiss()


           }
           override fun onReceivedError(
               view: WebView?, errorCode: Int,
               description: String, failingUrl: String?
           ) {


               //TODO We can show customized HTML page when page not found/ or server not found error.
               super.onReceivedError(view, errorCode, description, failingUrl)
           }


       }
       fun geturl():String{

           progressBar!!.visibility=View.VISIBLE
           val call = ApproveUtils.Get.getprice_list()
           call.enqueue(object : Callback<Resp_trip> {
               override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                   Log.e("requesttypes", response.toString())
                   if (response.isSuccessful()) {
                       val example = response.body() as Resp_trip
                       if (example.getStatus() == "Success") {
                           var res=example.getResponse()
                           url= res?.get(0)?.price.toString()
                           println("url"+url)

                           wbview.getSettings().setJavaScriptEnabled(true);
                           wbview.getSettings().setLoadWithOverviewMode(true);
                           wbview.getSettings().setUseWideViewPort(true);
                           wbview.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
                       } else {
                           Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                       }
                   }
               }

               override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                   Log.e("requesttypes Fail", t.toString())
                   if (t.toString().contains("time")) {
                       Toast.makeText(
                           activity,
                           "Poor network connection",
                           Toast.LENGTH_LONG
                       ).show()

                   }
               }
           })

           return url;
       }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {



        // Handle item selection
        when (item.itemId) {
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


   }