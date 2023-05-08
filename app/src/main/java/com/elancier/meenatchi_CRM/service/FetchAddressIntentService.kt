package com.elancier.meenatchi_CRM.service

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import android.util.Log

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

import java.io.IOException
import java.util.ArrayList
import java.util.Locale

/**
 * This constructor is required, and calls the super IntentService(String)
 * constructor with the name for a worker thread.
 */
class FetchAddressIntentService : IntentService(TAG) {
    /**
     * The receiver where results are forwarded from this service.
     */
    protected var mReceiver: ResultReceiver? = null

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: We define a [ResultReceiver] in * MainActivity to process content
     * sent from this service.
     *
     *
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    override fun onHandleIntent(intent: Intent?) {
        var errorMessage = ""

        mReceiver = intent!!.getParcelableExtra(AppUtils.LocationConstants.RECEIVER)

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.")
            return
        }
        // Get the location passed to this service through an extra!!.
        val location = intent!!.getParcelableExtra<Location>(AppUtils.LocationConstants.LOCATION_DATA_EXTRA)

        // Make sure that the location data was really sent over through an extra!!. If it wasn't,
        // send an error error message and return.
        if (location == null) {
            errorMessage = "no_location_data_provided"
            Log.wtf(TAG, errorMessage)
            deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, errorMessage, null)
            return
        }

        // Errors could still arise from using the Geocoder (for example, if there is no
        // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
        // simply not have an address for a location. In all these cases, we communicate with the
        // receiver using a resultCode indicating failure. If an address is found, we use a
        // resultCode indicating success.

        // The Geocoder used in this sample. The Geocoder's responses are localized for the given
        // Locale, which represents a specific geographical or linguistic region. Locales are used
        // to alter the presentation of information such as numbers or dates to suit the conventions
        // in the region they describe.
        val geocoder = Geocoder(this, Locale.getDefault())

        // Address found using the Geocoder.
        var addresses: List<Address>? = null

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    // In this sample, we get just a single address.
                    1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available"
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used"
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.latitude +
                    ", Longitude = " + location.longitude, illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no_address_found"
                Log.e(TAG, errorMessage)
            }
            deliverResultToReceiver(AppUtils.LocationConstants.FAILURE_RESULT, errorMessage, null)
        } else {
            val address = addresses[0]
            val addressFragments = ArrayList<String>()

            for (i in 0 until address.maxAddressLineIndex) {
                addressFragments.add(address.getAddressLine(i))

            }
            deliverResultToReceiver(AppUtils.LocationConstants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator")!!, addressFragments), address)
            //TextUtils.split(TextUtils.join(System.getProperty("line.separator"), addressFragments), System.getProperty("line.separator"));

        }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private fun deliverResultToReceiver(resultCode: Int, message: String, address: Address?) {
        try {
            val bundle = Bundle()
            bundle.putString(AppUtils.LocationConstants.RESULT_DATA_KEY, message)

            bundle.putString(AppUtils.LocationConstants.FullAdrs, address!!.toString())
            bundle.putString("state", address.adminArea)
            bundle.putString("countryName", address.countryName)
            bundle.putString(AppUtils.LocationConstants.LOCATION_DATA_AREA, address.postalCode)
            val area = address.getAddressLine(0).toString().split(",")[1]
            val city = address.locality
            val subAdminArea = address.subAdminArea
            println("area : "+area)
            println("city : "+city)
            println("subAdminArea : "+subAdminArea)
            bundle.putString("area", area)
            bundle.putString("city", city)
            bundle.putString(AppUtils.LocationConstants.LOCATION_DATA_CITY, address.subAdminArea)
            bundle.putString(AppUtils.LocationConstants.LOCATION_DATA_STREET, address.getAddressLine(0))

            mReceiver!!.send(resultCode, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun displayLocationSettingsRequest(context: Activity) {
        val googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> println("All location settings are satisfied.")
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    println( "Location settings are not satisfied. Show the user a dialog to upgrade location settings ")

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                        println( "PendingIntent unable to execute request.")
                    }

                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> println( "Location settings are inadequate, and cannot be fixed here. Dialog not created.")
            }
        }
    }

    companion object {
        private val TAG = "FetchAddressIS"
        protected val REQUEST_CHECK_SETTINGS = 0x1
    }

}// Use the TAG to name the worker thread.
