package com.elancier.meenatchi_CRM

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web__report.*
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import android.widget.Toast
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Login_Resp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Web_Report : AppCompatActivity() {
    private var downloadID: Long = 0
    internal lateinit var progbar: Dialog
    internal lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web__report)


        var inte=intent.extras
        var id=inte!!.getString("id")
        var url="http://elancier.com/bija/Bija_Pdf.php?id="+id

        imageButton5.setOnClickListener {
            finish()
        }
        imageButton6.setOnClickListener {
            progbar = Dialog(this@Web_Report)
            progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progbar.setContentView(R.layout.load)
            progbar.setCancelable(false)
            progbar.show()
            val call = ApproveUtils.Get.getMem2rep(Appconstants.Domin+"/"+"family_pdf/"+id)
            call.enqueue(object : Callback<Login_Resp> {
                override fun onResponse(call: Call<Login_Resp>, response: Response<Login_Resp>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Login_Resp
                        println(example)
                        if (example.status == "Success") {
                            progbar!!.dismiss()

                            var url=example.response!!.data!![0]!!.pdf

                            var urlrem=url!!.removePrefix("../")
                            println("urlrem"+urlrem)
                            downloadManager = this@Web_Report.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                            val uri = Uri.parse("http://elancier.com/bija/$urlrem")
                            val request = DownloadManager.Request(uri)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            val reference = downloadManager.enqueue(request)

                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                beginDownload("http://elancier.com/bija/"+urlrem)
                            }*/

                        } else {
                            progbar!!.dismiss()

                            Toast.makeText(this@Web_Report, example.status, Toast.LENGTH_SHORT)
                                .show()


                        }
                    }
                    else{

                        progbar!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<Login_Resp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    progbar!!.dismiss()

                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Web_Report,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()



                    }
                }
            })
        }

        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        println("url  p"+url)
        webvw.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        })
        webvw.getSettings().setJavaScriptEnabled(true)

        webvw.loadUrl("https://docs.google.com/gview?embedded=true&url=" +url)

    }
    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID === id) {
                Toast.makeText(this@Web_Report, "Download Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun beginDownload(url:String) {
        val file = File(getExternalFilesDir(null), "Bija_pdf")
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


}
