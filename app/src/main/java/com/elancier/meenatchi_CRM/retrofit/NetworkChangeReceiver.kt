package com.elancier.meenatchi_CRM.retrofit

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.os.Build
import androidx.annotation.RequiresApi


class NetworkChangeReceiver : BroadcastReceiver() {

    lateinit var view:View
    lateinit var pwindow:PopupWindow
    lateinit var inflater:LayoutInflater
    lateinit var layoutt:View
    var st=Intent()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        val status = NetworkUtils.getConnectivityStatusString(context)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var connected=false
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
            try {
               //TransparentActivity.trn.finish()
                //Dashboard.dash.setResult(Appconstants.NET)
            }catch (e:Exception){

            }

            connected = true
        } else {
            try {
                //st=Intent(context,TransparentActivity::class.java)
                //context.startActivity(st)
            }catch (e:Exception){

            }

            connected = false
        }

    }

    /*private fun showCustomPopupMenu() {
        windowManager2 = getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.xxact_copy_popupmenu, null)
        params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.CENTER or Gravity.CENTER
        params.x = 0
        params.y = 0
        windowManager2.addView(view, params)
    }*/

}