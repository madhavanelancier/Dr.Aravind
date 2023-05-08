package com.elancier.meenatchi_CRM

import android.Manifest
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_otp
import com.elancier.meenatchi_CRM.retrofit.Utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AlarmReceiver : BroadcastReceiver() {

    lateinit var utils : Utils
    var current_lat = 0.0
    var current_lng = 0.0
    internal var TAG = "AlarmReceiver"
    lateinit var cons :Context
    internal lateinit var pref: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor

    override fun onReceive(context: Context?, intent: Intent) {
        // TODO Auto-generated method stub
        cons=context!!
        utils= Utils(context)
        pref = cons.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        if (intent.action != null && context != null) {
            if (intent.action!!.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED")
                //println("every 2 minutes ${SystemClock.currentThreadTimeMillis()/1000}")
                //val localData = LocalData(context)
                //NotificationScheduler.setReminder(context, AlarmReceiver::class.java, localData._hour, localData._min)
                return
            }
        }

        //Log.d(TAG, "onReceive: ")
        //println("every 2 minutes ${System.currentTimeMillis()/1000}")
        //Trigger the notification
        //NotificationScheduler.showNotification(context!!, LoginActivity::class.java,"You have 5 unwatched videos", "Watch them now?")
        if (net_status(context) == true){

          getLocation(context)

          upload()



        }else{
            println("No Network")
        }
    }
    fun net_status(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var connected=false

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true
        } else {
            //Toast.makeText(context,"No Network",Toast.LENGTH_LONG).show()
            connected = false
        }
        return connected
    }

    fun upload(){

        val call = ApproveUtils.Get.getgeo(pref.getString("tid","").toString(),
            pref.getString("mobile","").toString(),current_lat.toString(),current_lng.toString())
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        /*Toast.makeText(
                            cons,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()*/
                    } else {
                        /*Toast.makeText(
                            cons,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                }
                // pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        cons,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })
    }

   /* private inner class Upload : AsyncTask<String, String, String>() {

        private var resp: String? = null

        override fun doInBackground(vararg params: String): String {
            println("dbname : "+params[0])
            val lat = params[0]
            val lng = params[1]
            println("${System.currentTimeMillis()/1000} : location : "+lat +" : "+lng)

            val userid = utils.userid()
            val call = ApproveUtils.Get.updateLocation(userid,lat, lng)
            call.enqueue(object : Callback<Resp> {
                override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                    Log.e("loc response", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp
                        println(example)
                        if (example.status == "Success") {
                        }
                    }
                    utils.setSend(true)
                }

                override fun onFailure(call: Call<Resp>, t: Throwable) {
                    Log.e("loc Fail response", t.toString())
                    if (t.toString().contains("time")) {
                    }
                    utils.setSend(true)
                }
            })

            resp="com"
            return resp!!
        }


        override fun onPostExecute(result: String) {

        }


        override fun onPreExecute() {

        }


        override fun onProgressUpdate(vararg text: String) {

        }
    }*/

    fun getLocation(context: Context) {
        try {
            var  location : Location
            var locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                println("locat gps ")
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                val criteria = Criteria()
                //criteria.accuracy = Criteria.ACCURACY_HIGH
                //locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)
                locationManager!!.requestSingleUpdate(
                    criteria,
                    object : LocationListener {
                        override fun onLocationChanged(locatio: Location) {
                            println("singlelocation : " + locatio.latitude + " , " + locatio.longitude)
                            location = locatio
                            current_lat = location.latitude
                            current_lng = location.longitude
                           // Utils(context).setLocation((location.latitude).toString(),(location.longitude).toString())
                        }

                        override fun onStatusChanged(
                            provider: String,
                            status: Int,
                            extras: Bundle
                        ) {
                        }

                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    },
                    null
                )
                // Log.e(“Network”, “Network”);
                if (locationManager != null) {
                    val locatio = locationManager!!
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (locatio != null) {
                        println("lastknown : " + locatio.latitude + " , " + locatio.longitude)
                        location=locatio
                        current_lat = location.latitude
                        current_lng = location.longitude
                        //Utils(context).setLocation((location.latitude).toString(),(location.longitude).toString())
                    }
                }
            }else{
                println("locat gps ")
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                val criteria = Criteria()
                //criteria.accuracy = Criteria.ACCURACY_HIGH
                //locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)
                locationManager!!.requestSingleUpdate(
                    criteria,
                    object : LocationListener {
                        override fun onLocationChanged(locatio: Location) {
                            println("singlelocation : " + locatio.latitude + " , " + locatio.longitude)
                            location=locatio
                            current_lat = location.latitude
                            current_lng = location.longitude
                            //Utils(context).setLocation((location.latitude).toString(),(location.longitude).toString())
                        }

                        override fun onStatusChanged(
                            provider: String,
                            status: Int,
                            extras: Bundle
                        ) {
                        }

                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    },
                    null
                )
                // Log.e(“Network”, “Network”);
                if (locationManager != null) {
                    val locatio = locationManager!!
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (locatio != null) {
                        println("lastknown : " + locatio.latitude + " , " + locatio.longitude)
                        location=locatio
                        current_lat = location.latitude
                        current_lng = location.longitude
                        //Utils(context).setLocation((location.latitude).toString(),(location.longitude).toString())
                    }
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            println("error : "+e.printStackTrace())
        }

    }
}