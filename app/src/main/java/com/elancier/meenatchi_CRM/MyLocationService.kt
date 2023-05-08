package com.elancier.meenatchi_CRM

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_otp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by roberto on 9/29/16.
 */
class MyLocationService : Service() {
    private var mLocationManager: LocationManager? = null
    var locationmanager: LocationManager? = null
    var latistr=""
    var longstr=""
    internal lateinit var pref: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor

    fun getLocation() {
        try {
            println("locat")
            locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            //locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)
            locationmanager!!.requestSingleUpdate(
                criteria,
                object : android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        println("singlelocation : " + location.latitude + " , " + location.longitude)
                        latistr = location.latitude.toString()
                        longstr = location.longitude.toString()

                        if (latistr.isNotEmpty() && longstr.isNotEmpty()) {

                          Toast.makeText(this@MyLocationService,latistr,Toast.LENGTH_LONG).show()
                            val call = ApproveUtils.Get.getgeo(pref.getString("tid","").toString(),
                                pref.getString("mobile","").toString(),latistr,longstr)
                            call.enqueue(object : Callback<Resp_otp> {
                                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                                    Log.e("response", response.toString())
                                    if (response.isSuccessful()) {
                                        val example = response.body() as Resp_otp
                                        println(example)
                                        if (example.status == "Success") {
                                            Toast.makeText(
                                                this@MyLocationService,
                                                example.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@MyLocationService,
                                                example.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                   // pDialog.dismiss()
                                    //loading_show(activity).dismiss()
                                }

                                override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                                    Log.e("Fail response", t.toString())
                                    if (t.toString().contains("time")) {
                                        Toast.makeText(
                                            this@MyLocationService,
                                            "Poor network connection",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    //pDialog.dismiss()
                                    //loading_show(activity).dismiss()
                                }
                            })
                        } else {
                            // val toast=Toast(applicationContext,"Unable to get location")
                        }
                        //location_shimmer.stopShimmer()
                        // location_shimmer.visibility = View.GONE
                        // location_layout.visibility = View.VISIBLE
                        //getCompleteAddressString(location)
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
            if (locationmanager != null) {
                val location = locationmanager!!
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    println("lastknown : " + location.latitude + " , " + location.longitude)

                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            println("error : " + e.printStackTrace())
        }


    }
    inner class LocationListener(provider: String) : android.location.LocationListener {
        var mLastLocation: Location
        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: $location")
            mLastLocation.set(location)
        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: $provider")
            getLocation()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: $provider")
        }

        init {
            Log.e(TAG, "LocationListener $provider")
            mLastLocation = Location(provider)
            getLocation()
        }
    }

    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */
    var mLocationListeners = arrayOf<LocationListener>(
        LocationListener(LocationManager.PASSIVE_PROVIDER)
    )

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")

        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
        initializeLocationManager()
        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER,
                LOCATION_INTERVAL.toLong(),
                LOCATION_DISTANCE,
                mLocationListeners[0]
            )
            pref = applicationContext.getSharedPreferences("MyPref", 0)
            editor = pref.edit()
            getLocation()
        } catch (ex: SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.message)
        }

        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }*/
    }




    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: Exception) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex)
                }
            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(
            TAG,
            "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE
        )
        if (mLocationManager == null) {
            mLocationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        }
    }

    companion object {
        private const val TAG = "MyLocationService"
        private const val LOCATION_INTERVAL = 1000
        private const val LOCATION_DISTANCE = 10f
    }
}