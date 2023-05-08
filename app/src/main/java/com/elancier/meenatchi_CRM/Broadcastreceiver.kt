package com.elancier.meenatchi_CRM

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer


class MyBroadcastReceiver : BroadcastReceiver() {
    var mp: MediaPlayer? = null
    var gpslocation: Location? = null
    var locationmanager: LocationManager? = null
    var isGPS = false;

    private val GPS_TIME_INTERVAL = 60000 // get gps location every 1 min
    private val GPS_DISTANCE = 1000 // set the distance value in meter

    override fun onReceive(context: Context?, intent: Intent?) {

        val service=Intent(context,MyLocationService::class.java)
        context!!.startService(service);


    }

}