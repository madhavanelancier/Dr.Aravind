package com.elancier.meenatchi_CRM

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.os.Bundle
import android.os.StrictMode
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elancier.meenatchi_CRM.Adapers.MyFamilyAdap
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.DataClasses.SpinnerPojo
import com.elancier.meenatchi_CRM.Family.Family_Main
import com.elancier.meenatchi_CRM.retrofit.*
import com.elanciers.bringszodelivery.Common.NotificationScheduler
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var pDialog: Dialog
    var villagearr = ArrayList<SpinnerPojo>()
    var CentresArrays = java.util.ArrayList<CentresData>()
    lateinit var adp: MyFamilyAdap
    lateinit var click: MyFamilyAdap.OnItemClickListener
    internal var pref: SharedPreferences? = null
    internal lateinit var progbar: Dialog
    internal var editor: SharedPreferences.Editor? = null
    var posid = ""
    var lat = 0.0
    var longi = 0.0

    //var prefs: SharedPreferences? = null
    private var mLocationManager: LocationManager? = null
    var locationmanager: LocationManager? = null
    var locationManager: LocationManager? = null;
    var isGPS = false;
    var loaded = false
    var latistr = ""
    var longstr = ""
    var from = ""
    var district = ""
    var checkaction = "0"

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
                ) != PackageManager.PERMISSION_GRANTED
            ) {

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

                        lat = location.latitude
                        longi = location.longitude


                        getCompleteAddressString(lat, longi)

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

                } else {
                    CheckingPermissionIsEnabledOrNot()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            println("error : " + e.printStackTrace())
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pDialog = Dialog(this@MainActivity)

        //prefs = getSharedPreferences("com.elancier.vanithamarket", MODE_PRIVATE);
        pref = this@MainActivity.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        textView55.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Customer_Add::class.java)
            )
        }

        textView53.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Schedule_Meeting::class.java)
            )
        }

        cust.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Meeting_Add::class.java)
            )
            //Editorders(0)


        }


        checkout.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Checkout?")
            alert.setMessage("Are you sure want to checkout?")
            alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()

                dosavecat(
                    "Check Out",
                    pref!!.getString("cusid", "").toString()
                )


            })
            alert.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()


            })
            val pop = alert.create()
            pop.show()

        }

        checkin.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Check In?")
            alert.setMessage("Are you sure want to checkin?")
            alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()
                dosavecat(
                    "Check In",
                    pref!!.getString("cusid", "").toString()
                )
            })
            alert.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()


            })
            val pop = alert.create()
            pop.show()

        }

        getLocation()
        if (CheckingPermissionIsEnabledOrNot()) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
                locationManager =
                    this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    10F,
                    locationListenerGPS
                )
                getCompleteAddressString(lat.toDouble(), longi.toDouble())


            }


            statusCheck()
            //getLocation()
        } else {
            RequestMultiplePermission()
        }

        docCard.setOnClickListener {
            if (checkin.visibility == View.GONE) {
                startActivity(
                    Intent(this@MainActivity, Customers_List::class.java).putExtra(
                        "cus",
                        "customer"
                    )//Eod_list
                )
            } else {
                toast("Please Check In")
            }


        }
        meetCard.setOnClickListener {
            if (checkin.visibility == View.GONE) {
                startActivity(
                    Intent(this@MainActivity, Eod_list::class.java).putExtra("cus", "order")
                )
            } else {
                toast("Please Check In")
            }

        }
        patientCard.setOnClickListener {
            if (checkin.visibility == View.GONE) {
                startActivity(
                    Intent(this@MainActivity, Patient_list::class.java)
                )
            } else {
                toast("Please Check In")
            }
        }

        /*textView56stock.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Fish_report_list::class.java)
            )
        }


        textView47.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Travel_list::class.java).putExtra("cus", "customer")
            )

        }

        eod.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Request_List::class.java).putExtra(
                    "cus",
                    "customer"
                )//Eod_list
            )

        }*/






        addfamily_lay.setOnClickListener {
            startActivity(Intent(this@MainActivity, Branch_List::class.java))

        }/*
        addfamily_lay.setOnClickListener {
            startActivity(Intent(this@MainActivity, Branch_List::class.java))

        }*/
        addharvest_lay.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Request_List::class.java).putExtra(
                    "cus",
                    "order"
                )
            )
        }

        account_lay.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Customer_Add::class.java).putExtra(
                    "cus",
                    "customer"
                )
            )
        }

        wallet_lay.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Request_List::class.java).putExtra(
                    "cus",
                    "customer"
                )
            )
        }
        repoprt_lay.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Customers_List::class.java).putExtra(
                    "cus",
                    "order"
                )
            )
        }

        addfvillage_lay.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, Profile_update::class.java).putExtra(
                    "cus",
                    "customer"
                )
            )

        }

        imageButton2.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit?")
                .setMessage("Are you sure want to exit the app?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    onBackPressed()
                }
                .setNegativeButton("No", null)
                .show()
        })

        name.setText(
            pref!!.getString("fname", "").toString() + " " + pref!!.getString("lname", "")
                .toString()
        )

        logut_lay.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    editor!!.putString("name", "")
                    editor!!.putString("tid", "")
                    editor!!.putString("fname", "")
                    editor!!.putString("lname", "")
                    editor!!.putString("email", "")
                    editor!!.putString("van_login", "")
                    editor!!.commit()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }


        edit.setOnClickListener {
            val openwith = android.app.AlertDialog.Builder(this@MainActivity)
            val popUpView = layoutInflater.inflate(R.layout.editprofile_popup, null)
            val fname = popUpView.findViewById(R.id.fcode) as EditText
            val save = popUpView.findViewById(R.id.button) as Button

            fname.setText(name.text.toString())
            openwith.setView(popUpView)
            val family = openwith.create()
            fname.requestFocus()
            family.setCancelable(true)
            family.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            family.show()

            save.setOnClickListener {
                progbar = Dialog(this@MainActivity)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.save)
                progbar.setCancelable(false)
                progbar.show()
                if (fname.text.toString().trim().isNotEmpty()) {
                    val call = ApproveUtils.Get.geteditfamily2(fname.text.toString().trim())

                    call.enqueue(object : Callback<Resp_dup> {
                        override fun onResponse(
                            call: Call<Resp_dup>,
                            response: Response<Resp_dup>
                        ) {


                            Log.v("responce", response.toString())
                            if (response.isSuccessful) {


                                val login = response.body()
                                //if (!login.getCustomers().isEmpty()) {
                                Log.e("inssucc", "succ")
                                if (login!!.getStatus() == "Success") {
                                    progbar.dismiss()
                                    //pop!!.dismiss()
                                    //onResume()
                                    family.dismiss()

                                    editor!!.putString("name", fname.text.toString())
                                    editor!!.commit()
                                    name.setText(fname.text.toString())
                                    Toast.makeText(
                                        applicationContext,
                                        "Updated",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()


                                } else {
                                    progbar.dismiss()

                                    Toast.makeText(
                                        applicationContext,
                                        login.getStatus(),
                                        Toast.LENGTH_SHORT
                                    ).show();

                                }
                                /*}
                            else{
                                //progbar.dismiss();

                                //Toast.makeText(LoginActivity.this, "Bad Credentials", Toast.LENGTH_SHORT).show();

                            }*/
                            }
                        }


                        override fun onFailure(call: Call<Resp_dup>, throwable: Throwable) {
                            Log.e("error", throwable.toString())
                            //progbar.dismiss();
                            progbar.dismiss()
                            family.dismiss()

                            Toast.makeText(
                                this@MainActivity,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()

                            //login.setEnabled(true);
                            // Toast.makeText(MainActivity.this,"Failed"+t.toString(),Toast.LENGTH_SHORT).show();

                        }
                    })
                } else {

                    fname.setError("Enter name")

                }
            }
        }


    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder()
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    break
                }
                strAdd = strReturnedAddress.toString()
                district = strReturnedAddress.toString()
                Log.e("location address", district)
                //Toast.makeText(MainActivity.this, strReturnedAddress.toString(), Toast.LENGTH_SHORT).show();
                Log.e("strReturnedAddress", strReturnedAddress.toString())
            } else {
                Log.e("location address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            //finish()
            CheckingPermissionIsEnabledOrNot()
            //Toast.makeText(applicationContext, "Please turn on your GPS", Toast.LENGTH_LONG).show()
            Log.e("location address", "Cannot get Address!")
        }
        return strAdd
    }

    fun getVillage() {
        villagearr.clear()
        pDialog = Dialog(this)
        // Appconstands.loading_show(this@MainActivity, pDialog).show()

        //Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
        val call = ApproveUtils.Get.getvillage()
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {

                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.getStatus() == "Success") {
                        var otpval = example.getMessage()!!.data
                        for (i in 0 until otpval!!.size) {
                            val data = SpinnerPojo()
                            data.name = otpval[i].id.toString()
                            data.id = otpval[i].id.toString()
                            data.code = otpval[i].name
                            data.status = otpval[i].status.toString()
                            if (otpval[i].status.toString() == "on") {
                                villagearr.add(data)

                            }

                        }
                        //pDialog.dismiss()


                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            example.getStatus(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                //pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                //Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@MainActivity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }

    fun dosavecat(name: String, id: String) {
        if (lat != 0.0 && longi != 0.0 && district.isNotEmpty()) {

            if (name == "Check In") {
                progbar = Dialog(this)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.checkin)
                progbar.setCancelable(false)
                progbar.show()
            } else {
                progbar = Dialog(this)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.checkout)
                progbar.setCancelable(false)
                progbar.show()
            }
            val json = JsonObject()
            json.addProperty("user_id", "1"/*pref!!.getString("cusid", "").toString()*/)
            json.addProperty("type", name)
            json.addProperty("location", district)
            json.addProperty("lat", lat.toString())
            json.addProperty("long", longi.toString())
            val call = ApproveUtils.Get.check_in(json)

            Log.e("inside", json.toString())
            call.enqueue(object : Callback<Resp_otp> {
                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {

                    Log.v("responce", response.toString())
                    if (response.isSuccessful) {


                        val login = response.body()
                        //if (!login.getCustomers().isEmpty()) {
                        Log.e("inssucc", "succ")
                        if (login!!.status == "Success") {
                            progbar.dismiss()
                            //pop!!.dismiss()
                            toast(login!!.message.toString())
                            editor!!.putString("lat", lat.toString())
                            editor!!.putString("long", longi.toString())
                            editor!!.commit()

                            if (name == "Check In") {
                                checkout.visibility = View.VISIBLE
                                checkin.visibility = View.GONE
                            } else {
                                checkout.visibility = View.GONE
                                checkin.visibility = View.VISIBLE
                            }


                        } else {
                            progbar.dismiss()
                            toast(login!!.message.toString())
                            //Toast.makeText(applicationContext,login.getStatus(), Toast.LENGTH_SHORT).show();

                        }
                        /*}
                    else{
                        //progbar.dismiss();

                        //Toast.makeText(LoginActivity.this, "Bad Credentials", Toast.LENGTH_SHORT).show();

                    }*/
                    } else {
                        progbar.dismiss()

                    }
                }


                override fun onFailure(call: Call<Resp_otp>, throwable: Throwable) {
                    Log.e("error", throwable.toString())
                    //progbar.dismiss();
                    progbar.dismiss()

                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            toast("Turn on your location and try again.")
            getLocation()
        }
    }


    fun editarea(id: String) {

        val a = Intent(this@MainActivity, Family_Main::class.java)
        a.putExtra("id", id)
        startActivity(a)

    }

    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
        val STORAGE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val STORAGEwrite = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val CALL_PHONE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CALL_PHONE
        )

        return ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                STORAGE == PackageManager.PERMISSION_GRANTED &&
                STORAGEwrite == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED //&&
        //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }

    fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(
            this, arrayOf<String>
                (
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION//,
                //android.Manifest.permission.CALL_PHONE
            ), Appconstands.RequestPermissionCode
        )


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            Appconstands.RequestPermissionCode ->

                if (grantResults.size > 0) {

                    val ACCESS_NETWORK_STATE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    //val ACCESS_NOTIFICATION_POLICY = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_FINE_LOCATION = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_COARSE_LOCATION =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    //val CALL_PHONE = grantResults[4] == PackageManager.PERMISSION_GRANTED

                    if (ACCESS_NETWORK_STATE /*&& ACCESS_NOTIFICATION_POLICY*/ && ACCESS_FINE_LOCATION && ACCESS_COARSE_LOCATION /*&&CALL_PHONE*/) {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            /*val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);*/
                            //getLocation()                            getLocation()
                            getLocation()


                        } else {

                            finish()
                            Toast.makeText(this, "Please allow GPS Permission", Toast.LENGTH_LONG)
                                .show()

                        }
                        //Toast.makeText(this@MainFirstActivity,"Permission Granted", Toast.LENGTH_LONG).show()
                    } else {

                        finish()
                        Toast.makeText(this, "Please allow GPS Permission", Toast.LENGTH_LONG)
                            .show()

                    }
                } else {
                    finish()
                    Toast.makeText(this, "Please allow GPS Permission", Toast.LENGTH_LONG).show()

                }

        }
    }

    /*fun families(type: String) {
        if (Appconstants.net_status(this)) {

            val json = JsonObject()
            if (type == "Employee") {
                json.addProperty("cus_id", "0")

            } else {
                json.addProperty("cus_id", pref!!.getString("cusid", ""))

            }
            json.addProperty("enter_by", pref!!.getString("empid", ""))
            json.addProperty("type", type)

            val call = ApproveUtils.Get.getdashboard(json)
            call.enqueue(object : Callback<Resp_otp> {
                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otp
                        println(example)
                        if (example.status == "Success") {
                            //textView23.visibility=View.GONE
                            var arr = example.message
                            var otpval = example.response
                            //textView33.setText(arr!!.data?.get(0)?.family.toString())

                            if (type == "Employee") {
                                orderval.setText(otpval?.get(0)!!.customers)
                            } else {
                                baln.setText(otpval?.get(0)!!.balance)
                                norders.setText(otpval?.get(0)!!.orders)
                                checkaction = otpval?.get(0)!!.check_out.toString()

                                *//*if(checkaction=="2"){
                                    var AlertDialog= AlertDialog.Builder(this@MainActivity)
                                    AlertDialog.setTitle("Session Expired")
                                    AlertDialog.setCancelable(false)
                                    AlertDialog.setMessage("Your customer login session expired.\nPlease Check In Again.")
                                    AlertDialog.setPositiveButton("OK",
                                        DialogInterface.OnClickListener { dialogInterface, i ->

                                            dialogInterface.dismiss()
                                            dosavecat(
                                                pref!!.getString("cusname", "").toString(),
                                                pref!!.getString("cusid", "").toString()
                                            )

                                        })
                                    AlertDialog.setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialogInterface, i ->
                                            dialogInterface.dismiss()

                                            //onResume()
                                        })
                                    val pops=AlertDialog.create()
                                    pops.show()
                                }*//*
                            }

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                example.status,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            //shimmer_view_container.visibility=View.GONE
                            //textView23.visibility=View.VISIBLE
                            //shimmer_view_container.stopShimmer()

                        }
                    } else {
                        //textView23.visibility=View.VISIBLE

                        //shimmer_view_container.stopShimmer()

                        //shimmer_view_container.visibility=View.GONE

                    }
                }

                override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@MainActivity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        //shimmer_view_container.visibility=View.GONE
                        //shimmer_view_container.stopShimmer()


                    }
                }
            })
        } else {
            Toast.makeText(
                this@MainActivity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            //shimmer_view_container.visibility=View.GONE

        }
    }*/

    fun dosavedel(user: String) {
        progbar = Dialog(this@MainActivity)
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progbar.setContentView(R.layout.load)
        progbar.setCancelable(false)
        progbar.show()
        var AlertDialog = AlertDialog.Builder(this)
        AlertDialog.setTitle("Deactivate?")
        AlertDialog.setMessage("Are you sure?")
        AlertDialog.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->

                dodeletecat(user, "off", "De-activated")
                dialogInterface.dismiss()


            })
        AlertDialog.setNegativeButton("No",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                progbar!!.dismiss()
                //onResume()
            })
        val pops = AlertDialog.create()
        pops.show()


    }

    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        println("net : " + manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        println("gps : " + manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && !manager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        ) {
            println("perform")
            GpsUtils(this).turnGPSOn(object : GpsUtils.onGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    // turn on GPS
                    isGPS = isGPSEnable
                    println("network & gps else " + isGPSEnable)
                    getLocation()


                }
            })
            /*val request = LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            val settingsRequest = LocationSettingsRequest.Builder()
            settingsRequest.addLocationRequest(request)
            val client = LocationServices.getSettingsClient(activity)
            val responseTask = client.checkLocationSettings(settingsRequest.build())
            responseTask.addOnSuccessListener(activity, { locationSettingsResponse-> locationScheduler() })
            responseTask.addOnFailureListener(activity, { e->
                val statusCode = (e as ApiException).getStatusCode()
                when (statusCode) {
                    CommonStatusCodes.RESOLUTION_REQUIRED -> try
                    {
                        val apiException = (e as ResolvableApiException)
                        apiException.startResolutionForResult(LocationSettingsStatusCodes, permissionCode)
                        Log.d(TAG, "Dialog displayed")
                    }
                    catch (sendIntentException:IntentSender.SendIntentException) {
                        Log.d(TAG, "Error displaying dialogBox", sendIntentException)
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.d(TAG, "Unable to turn on location service", e)
                } })*/
            //buildAlertMessageNoGps()
            //if (googleApiClient == null)
            //{
            /*val googleApiClient = GoogleApiClient.Builder(getApplicationContext()).addApi(
                LocationServices.API).build()
            googleApiClient.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            locationRequest.setInterval(5 * 1000)
            //locationRequest.setFastestInterval(5 * 1000)
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************
            val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback(object: ResultCallback<LocationSettingsResult>
            {
                override fun onResult(result: LocationSettingsResult)
                {
                    val status = result.getStatus()
                    val state = result.getLocationSettingsStates()
                    when (status.getStatusCode()) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                            getLocation()
                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{}
                            *//*try
                                {
                                    status.startResolutionForResult(activity, 1000)
                                }
                                catch (e: IntentSender.SendIntentException)
                                {
                                }*//*
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                })*/
            //}
        } else {
            println("else : ")

            if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //getLocation()
                //buildAlertMessageNoGps()
                //if (googleApiClient == null)
                //{
                /*val googleApiClient = GoogleApiClient.Builder(getApplicationContext()).addApi(
                    LocationServices.API).build()
                googleApiClient.connect()
                val locationRequest = LocationRequest.create()
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                locationRequest.setInterval(5 * 1000)
                //locationRequest.setFastestInterval(5 * 1000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                // **************************
                builder.setAlwaysShow(true) // this is the key ingredient
                // **************************
                val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                result.setResultCallback(object: ResultCallback<LocationSettingsResult>
                {
                    override fun onResult(result: LocationSettingsResult)
                    {
                        val status = result.getStatus()
                        val state = result.getLocationSettingsStates()
                        when (status.getStatusCode()) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                getLocation()
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{}
                            *//*try
                            {
                                status.startResolutionForResult(activity, 1000)
                            }
                            catch (e: IntentSender.SendIntentException)
                            {
                            }*//*
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                })*/
                //}
            } else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //getLocation()
                //buildAlertMessageNoGps()
                //if (googleApiClient == null)
                //{
                /*val googleApiClient = GoogleApiClient.Builder(getApplicationContext()).addApi(
                    LocationServices.API).build()
                googleApiClient.connect()
                val locationRequest = LocationRequest.create()
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                locationRequest.setInterval(5 * 1000)
                //locationRequest.setFastestInterval(5 * 1000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                // **************************
                builder.setAlwaysShow(true) // this is the key ingredient
                // **************************
                val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                result.setResultCallback(object: ResultCallback<LocationSettingsResult>
                {
                    override fun onResult(result: LocationSettingsResult)
                    {
                        val status = result.getStatus()
                        val state = result.getLocationSettingsStates()
                        when (status.getStatusCode()) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                getLocation()
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{}
                            *//*try
                            {
                                status.startResolutionForResult(activity, 1000)
                            }
                            catch (e: IntentSender.SendIntentException)
                            {
                            }*//*
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                })*/
                //}
            } else {
                GpsUtils(this).turnGPSOn(object : GpsUtils.onGpsListener {
                    override fun gpsStatus(isGPSEnable: Boolean) {
                        // turn on GPS
                        isGPS = isGPSEnable
                        println("network else " + isGPSEnable)
                        //getLocation()
                    }
                })
            }
        }
    }

    var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
            println("msglat_lang" + msg)
            latistr = location.latitude.toString()
            longstr = location.longitude.toString()
            //Toast.makeText(this@HomeActivity, msg, Toast.LENGTH_LONG).show()
            //location_shimmer.stopShimmer()
            //location_shimmer.visibility=View.GONE
            // location_layout.visibility=View.VISIBLE
            // getCompleteAddressString(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            //getLocation()
        }

        override fun onProviderEnabled(provider: String) {
            //getLocation()

        }

        override fun onProviderDisabled(provider: String) {
            statusCheck()
        }
    }

    fun dosavedelenable(user: String) {
        progbar = Dialog(this@MainActivity)
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progbar.setContentView(R.layout.load)
        progbar.setCancelable(false)
        progbar.show()
        var AlertDialog = AlertDialog.Builder(this)
        AlertDialog.setTitle("Activate?")
        AlertDialog.setMessage("Are you sure?")
        AlertDialog.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->

                dodeletecat(user, "on", "Activated")
                dialogInterface.dismiss()


            })
        AlertDialog.setNegativeButton("No",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                progbar!!.dismiss()

                //onResume()
            })
        val pops = AlertDialog.create()
        pops.show()


    }

    private fun dodeletecat(nm: String, cd: String, rate: String) {

        val call = ApproveUtils.Get.getbrfamily2(nm, cd)

        Log.e("inside", "$nm,msh$call")
        call.enqueue(object : Callback<Resp_dup> {
            override fun onResponse(call: Call<Resp_dup>, response: Response<Resp_dup>) {


                Log.v("responce", response.toString())
                if (response.isSuccessful) {


                    val login = response.body()
                    //if (!login.getCustomers().isEmpty()) {
                    Log.e("inssucc", "succ")
                    if (login!!.getStatus() == "Success") {
                        progbar.dismiss()
                        //pop!!.dismiss()

                        Toast.makeText(applicationContext, rate, Toast.LENGTH_SHORT).show()


                    } else {
                        progbar.dismiss()

                        Toast.makeText(applicationContext, login.getStatus(), Toast.LENGTH_SHORT)
                            .show();

                    }
                    /*}
                    else{
                        //progbar.dismiss();

                        //Toast.makeText(LoginActivity.this, "Bad Credentials", Toast.LENGTH_SHORT).show();

                    }*/
                }
            }


            override fun onFailure(call: Call<Resp_dup>, throwable: Throwable) {
                Log.e("error", throwable.toString())
                //progbar.dismiss();
                progbar.dismiss()

                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()

                //login.setEnabled(true);
                // Toast.makeText(MainActivity.this,"Failed"+t.toString(),Toast.LENGTH_SHORT).show();

            }
        })
    }

    override fun onResume() {
        super.onResume()
        //getVillage()
        getDashboard(pref!!.getString("empid", "").toString())
        loaded = false
        from = intent.extras!!.getString("from").toString()
        textView51.setText("Hello " + pref!!.getString("fname", "").toString())
        firstletter.setText(
            (pref!!.getString("fname", "").toString().substring(0)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
        )

        /* if(from=="Employee"){
             front.visibility=View.VISIBLE
             textView56cust.visibility=View.GONE
             textView56stock.visibility=View.VISIBLE
             textView49.visibility=View.GONE
             textView47.visibility=View.GONE
             textView55.visibility=View.GONE
            button2.setText("LOGOUT")
            orderlay.visibility=View.GONE
            balancelay.visibility=View.GONE
            cust.visibility=View.VISIBLE
            cuslay.visibility=View.VISIBLE
            eod.visibility=View.GONE
            //textView83.visibility=View.VISIBLE

            families(from)

        }
        else{
            front.visibility=View.VISIBLE
            front.setText("Previous Collections")
            textView56cust.visibility=View.VISIBLE
            textView56stock.visibility=View.GONE
            textView49.visibility=View.VISIBLE
            textView47.visibility=View.VISIBLE
            textView55.visibility=View.VISIBLE
            editor!!.putString("cusid",intent.extras!!.getString("id").toString())
            editor!!.commit()
            textView51.setText(*//*"Hello " + *//*intent.extras!!.getString("name").toString() *//*+ "!"*//*)
            firstletter.setText((intent.extras!!.getString("name").toString().substring(0).capitalize()))
            button2.setText("CHECKOUT")
            orderlay.visibility=View.VISIBLE
            balancelay.visibility=View.VISIBLE
            cuslay.visibility=View.GONE
            eod.visibility=View.VISIBLE
            cust.visibility=View.GONE

            //textView83.visibility=View.GONE
            families(from)

        }*/
    }

    fun getDashboard(mobile: String) {
        val pDialog = ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Loading...")
        pDialog.show()
        // pDialog= Dialog(this)
        //Appconstands.loading_show(this, pDialog).show()

        val call = ApproveUtils.Get.getdash(mobile)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr = example.message
                        var resp = example.response!![0]
                        norders.setText(resp.doctors)
                        orderval.setText(resp.patients)
                        baln.setText(resp.meetings)

                        if (resp.check_out.equals("1")) {
                            checkin.visibility = View.GONE
                            checkout.visibility = View.VISIBLE
                        } else {
                            checkin.visibility = View.VISIBLE
                            checkout.visibility = View.GONE
                        }

                        pDialog.dismiss()
                        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                        val min = Calendar.getInstance().get(Calendar.SECOND)
                        println("hour : " + hour + " min : " + min)

                        /* NotificationScheduler.setReminder(
                             this@MainActivity,
                             AlarmReceiver::class.java,
                             hour,
                             min
                         )*/
/*
                        if (CheckingPermissionIsEnabledOrNot()) {
                            if (ContextCompat.checkSelfPermission(
                                    this@MainActivity,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                // val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                                // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
                                locationManager =
                                    this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                locationManager!!.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    2000,
                                    10F,
                                    locationListenerGPS
                                )

                            }


                            statusCheck()
                            //getLocation()
                        } else {
                            RequestMultiplePermission()
                        }*/

                        //finish()
                    } else {


                        /*    NotificationScheduler.cancelReminder(
                                this@MainActivity,
                                AlarmReceiver::class.java
                            )*/

                        Toast.makeText(
                            this@MainActivity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                        editor!!.putString("tid", "")
                        editor!!.commit()
                    }
                }
                // pDialog.dismiss()
                //loading_show(activity).dismiss()
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                pDialog.dismiss()
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@MainActivity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //   pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }

    fun Editorders(id: Int) {

        val openwith = Dialog(this)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = this!!.layoutInflater.inflate(R.layout.customer_add_popup, null)
        var close = popUpView.findViewById<ImageButton>(R.id.imageButton10)
        val cusname = popUpView.findViewById(R.id.ref) as TextInputEditText
        val mob = popUpView.findViewById(R.id.ordamt) as TextInputEditText
        val addressfield = popUpView.findViewById(R.id.address) as EditText
        var save = popUpView.findViewById(R.id.save) as TextView

        openwith.setContentView(popUpView)
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        );
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val displaymetrics = DisplayMetrics();
        this!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width = (displaymetrics.widthPixels * 1);
        val height = (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        openwith.show()

        close.setOnClickListener {
            openwith.dismiss()
        }

        save.setOnClickListener {
            if (cusname.text.toString().isNotEmpty() && (mob.text.toString()
                    .isNotEmpty() && mob.text.toString().length == 10) &&
                addressfield.text.toString().isNotEmpty()
            ) {
                Addcustomer(
                    cusname.text.toString(),
                    mob.text.toString(),
                    addressfield.text.toString(),
                    openwith
                )

            } else {
                if (cusname.text.toString().isEmpty()) {
                    cusname.setError("Required field")
                }
                if (mob.text.toString().isEmpty()) {
                    mob.setError("Required field")

                }
                if (mob.text.toString().length < 10 || mob.text.toString().length > 10) {
                    mob.setError("Please Enter Valid Mobile Number")

                }

                if (addressfield.text.toString().isEmpty()) {
                    addressfield.setError("Required field")

                }
            }
        }

        /* image1!!.setOnClickListener {

             if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                 selectImage()

             }
             else{
                 RequestMultiplePermission(activity!!)
             }

         }

         image2!!.setOnClickListener {

             if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                 selectImage1()


             }
             else{
                 RequestMultiplePermission(activity!!)
             }

         }


         save!!.setOnClickListener {

             if(refed!!.text.toString().isNotEmpty()&&
                 ordamt!!.text.toString().isNotEmpty()) {
                 openwith.dismiss()
                 pDialog = ProgressDialog(this!!);
                 pDialog.setMessage("Submitting...")
                 pDialog.setCancelable(false)
                 pDialog.show()
                 Handler().postDelayed({
                     SendLogin(pref.getString("mobile", "").toString())

                 }, 2000)

             }
             else{
                 if(refed!!.text.toString().isEmpty()){
                     refed!!.setError("Required field")
                 }
                 if(ordamt!!.text.toString().isEmpty()){
                     ordamt!!.setError("Required field")
                 }

             }

         }*/

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount === 0) {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit?")
                .setMessage("Are you sure want to exit the app?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    onBackPressed()
                }
                .setNegativeButton("No", null)
                .show()
            true
        } else super.onKeyDown(keyCode, event)
    }

    //Add Customer
    fun Addcustomer(
        name: String,
        qty: String,
        description: String, dialog: Dialog
    ) {


        val pDialog = ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Creating Customer...")
        pDialog.show()
        /*{"user":"9791981428","tid":"V00002","request_type":"4"
        "amount":"1000",
        "tour_plan":[{"tour_date":"","day":"","tour_city":"","customer":""}],
        "expenditure_plan":[{"expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""}]}*/

        val obj = JsonObject()

        obj.addProperty("customer_name", name)
        obj.addProperty("mobile", qty)
        obj.addProperty("address", description)
        obj.addProperty("enter_by", pref!!.getString("empid", ""))

        println("sendExpense : " + obj)

        val call =
            ApproveUtils.Get.customer_add(obj)//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("advancereq", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    Toast.makeText(applicationContext, example.getMessage(), Toast.LENGTH_SHORT)
                        .show()
                    if (example.getStatus() == "Success") {
                        dialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                Log.e("advancereq Fail", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        applicationContext,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()

                }
                pDialog.dismiss()
            }

        })
    }


    override fun onBackPressed() {
        val i = Intent()
        i.setAction(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }


    fun toast(msg: String) {
        val toast = Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
