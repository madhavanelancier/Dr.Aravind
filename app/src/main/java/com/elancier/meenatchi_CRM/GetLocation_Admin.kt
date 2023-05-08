package com.elancier.meenatchi_CRM

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.elancier.meenatchi_CRM.Appconstands.RequestPermissionCode
import com.elancier.meenatchi_CRM.service.AppUtils
import com.elancier.meenatchi_CRM.service.FetchAddressIntentService


import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_create_complaint_admin.*
import kotlinx.android.synthetic.main.complaint_add_lay_admin.*

import java.io.IOException
import java.util.*

class GetLocation_Admin : AppCompatActivity(), OnMapReadyCallback {
    private val RESULT_LOAD_IMAGE = 1
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private val TAG = "MAP LOCATION"
    private val REQUEST_CODE_AUTOCOMPLETE = 1
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mCenterLatLong: LatLng? = null
    private var mResultReceiver: AddressResultReceiver? = null
    var image1path = ""
    var image2path = ""
    var image3path = ""

    var image1data = ""
    var image2data = ""
    var image3data = ""

    var lattitude = ""
    var longitude = ""

    var curr_lat = ""
    var curr_long = ""
    var pincode = ""
    var area = ""
    var frm = 0
    var count=0
    internal var pref: SharedPreferences? = null

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_complaint_admin)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        pref = this.getSharedPreferences("MyPref", 0)
        checkLocationPermission()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mResultReceiver = AddressResultReceiver(Handler())



        back.setOnClickListener{
            finish()
        }



        next.setOnClickListener {
                val yu = Intent()
                yu.putExtra("location", address.text.toString())
                yu.putExtra("lattitude", lattitude)
                yu.putExtra("longitude", longitude)
                yu.putExtra("area", area)
                yu.putExtra("pincode", pincode)
                setResult(Activity.RESULT_OK,yu)
                finish()

        }
    }
    fun CheckingPermissionIsEnabledOrNot(): Boolean {
        val INTERNET = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        /*val ACCESS_NETWORK_STATEt = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)*/
        return INTERNET == PackageManager.PERMISSION_GRANTED /*&&
                ACCESS_NETWORK_STATEt == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED&&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED*/

    }

    private fun RequestMultiplePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(

                //android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA//,
                //android.Manifest.permission.WRITE_EXTERNAL_STORAGE

            ), RequestPermissionCode
        )

    }

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            // Display the address string or an error message sent from the intent service.
            val mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY)

            pincode = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA).toString()

            val state = resultData.getString("state")
            val countryName = resultData.getString("countryName")
            area = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY).toString()
            val mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET)
            val full = mStateOutput.toString().split(",")

            println("FullAdrs : " + resultData.getString(AppUtils.LocationConstants.FullAdrs))
            println("mAddressOutput : " + mAddressOutput)
            println("pincode : " + pincode)
            println("state : " + state)
            println("countryName : " + countryName)
            println("mCityOutput : " + area)
            println("mStateOutput : " + mStateOutput)

            //displayAddressOutput()
            address.setText(mStateOutput)

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                address.setText(mStateOutput)

            }
        }

    }

    protected fun startIntentService(mLocation: Location) {
        lattitude = mLocation.latitude.toString()
        longitude = mLocation.longitude.toString()
        //getCompleteAddressString(mLocation)
        /*enter_layout.visibility = View.GONE
        address_layout.visibility = View.GONE*/
        // Create an intent for passing to the intent service responsible for fetching the address.
        val intent = Intent(this, FetchAddressIntentService::class.java)

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver)

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation)

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent)

        println("Latitude:" + mLocation.getLatitude() + ", Longitude:" + mLocation.getLongitude())
        /*val geocoder = Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            val listAddresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
            println("listAddresses : "+listAddresses)
            if(null!=listAddresses&&listAddresses.size>0){
                val area = listAddresses.get(0).getAddressLine(0).toString().split(",")[1]
                val city = listAddresses.get(0).locality
                println("adminArea : "+listAddresses.get(0).adminArea)
                println("subAdminArea : "+listAddresses.get(0).subAdminArea)
                println("subLocality : "+listAddresses.get(0).subLocality)
                println("area : "+area)
                println("city : "+city)
                *//*ab!!.title=city.trimStart()
                ab!!.subtitle =area.trimStart()*//*
            }
        } catch ( e: IOException) {
            e.printStackTrace();
        }*/
    }

    fun getCompleteAddressString(locat: Location) {

        val geocoder = Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            val listAddresses = geocoder.getFromLocation(locat.latitude, locat.longitude, 1);
            println("listAddresses : " + listAddresses)
            if(null!=listAddresses&&listAddresses.size>0){
                val area = listAddresses.get(0).getAddressLine(0).toString().split(",")[1]
                val city = listAddresses.get(0).locality
                println("adminArea : " + listAddresses.get(0).adminArea)
                println("subAdminArea : " + listAddresses.get(0).subAdminArea)
                println("subLocality : " + listAddresses.get(0).subLocality)
                println("area : " + area)
                println("city : " + city)
                //location.setText(city)
                runOnUiThread({
                    address.setText(listAddresses.get(0).subAdminArea)
                })

                /*ab!!.title=city.trimStart()
                ab!!.subtitle =area.trimStart()*/
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                val selectedImage: Uri? = data.data

                /*val myBitmap = Uri.fromFile(File(picturePath))
            val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myBitmap)
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()*/;
               /* if (frm == 1) {
                    image1data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                    image1path = selectedImage.toString()
                    count += 1
                    image1.setImageURI(image1path.toUri())//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                } else if (frm == 2) {
                    image2data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                    image2path = selectedImage.toString()
                    count += 1
                    image2.setImageURI(image2path.toUri())
                } else if (frm == 3) {
                    image3data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                    image3path = selectedImage.toString()
                    count += 1
                    image3.setImageURI(image3path.toUri())
                }*/
                //println("image Data : "+Base64.encodeToString(byteArray, Base64.DEFAULT))
            }
        }
    }
    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            //checkBackgroundLocation()
        }
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                        // Now check background location
                        //checkBackgroundLocation()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {

                    }
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                        Toast.makeText(
                            this,
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }
    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

    override fun onMapReady(p0: GoogleMap) {
        Log.d(TAG, "OnMapReady")
        mMap = p0

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }else {
            mMap!!.isMyLocationEnabled = true
        }

        curr_lat=pref!!.getString("lat","").toString()
        curr_long=pref!!.getString("long","").toString()
        val set = LatLng(curr_lat.toDouble(), curr_long.toDouble());

        //"lat":"13.006529","lng":"80.2043947"
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(set, 15f));
        //mMap!!.animateCamera(CameraUpdateFactory.zoomTo(14.0f))
        mMap!!.setOnCameraChangeListener { cameraPosition ->
            var cameraPos : CameraPosition? = null

            /*if (id!=""){
                val data = db.Addressget(intent.extras!!.get("id").toString())
                val set = LatLng(data.adrs_latitude.toString().toDouble(), data.adrs_longtitude.toString().toDouble());
                //val set = LatLng(-33.867, 151.206);
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(set, 20f));
                cameraPos = CameraPosition.Builder()
                    .target(set).zoom(20f)*//*.tilt(70f)*//*.build()
                id=""
            }else {*/
            cameraPos = cameraPosition
            //}
            Log.d("Camera postion change" + "", cameraPosition.toString() + "")
            mCenterLatLong = cameraPos!!.target


            mMap!!.clear()

            try {

                val mLocation = Location("")
                mLocation.latitude = mCenterLatLong!!.latitude
                mLocation.longitude = mCenterLatLong!!.longitude

                startIntentService(mLocation)
                ///mLocationMarkerText.text = "Lat : " + mCenterLatLong!!.latitude + "," + "Long : " + mCenterLatLong!!.longitude
                //println("Lat : " + mCenterLatLong!!.latitude + "," + "Long : " + mCenterLatLong!!.longitude)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }


}