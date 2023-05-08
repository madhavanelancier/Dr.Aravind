package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp
import com.elancier.meenatchi_CRM.retrofit.Utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object Appconstands {
    var GPS_REQUEST = 1001;
    val RequestPermissionCode = 7
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    lateinit var alert : AlertDialog

    //https://elancier1.co/offline/register.php
    val Domin ="http://teamdev.co.in/vanitha/api/"
    val rupees ="₹ "
    const val RADIUS_1000 = "1000"
    const val TYPE_BAR = "bar"
    const val GOOGLE_API_KEY = "AIzaSyD4Liy8N1PPV-BPYooPpjQGE5Nxl9LAgG8"

    fun isValidEmail(editText: EditText): Boolean {

        return Patterns.EMAIL_ADDRESS.matcher(editText.text.toString().trim { it <= ' ' }).matches()
    }
    fun changeStatusBarColor(context: Activity,color : Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = context.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = context.resources.getColor(color)
        }
    }
    fun net_status(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var connected=false
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true
        } else {
            //Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show()
            connected = false
        }
        return connected
    }


    fun maintenance(activity: Activity){
        val call2 = ApproveUtils.Get.getMaintenance()
        call2.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("maintenance responce", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.getStatus() == "Success"){

                    }else {
                        Utils(activity).setMaintenance("")
                        if (::alert.isInitialized){
                            if (alert.isShowing) {
                                alert.dismiss()
                            }
                        }
                        //Toast.makeText(this@Dashboard, example.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call:Call<Resp>,t:Throwable) {
                Log.e("maince Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                    ).show()
                }
                if (t.toString().contains("Unable to resolve host")) {
                    if (Utils(activity).Maintenance()!!.isNotEmpty()) {
                        ///maintenancePopup(activity, Utils(activity).Maintenance())
                    } else {
                        if ((::alert.isInitialized)) {
                            if (alert.isShowing) {
                                alert.dismiss()
                            }
                        }
                    }
                }
            }
        })
    }

    fun maintenancePopup(activity: Activity,message:String){
        if (!(::alert.isInitialized)){
            val alert11 = AlertDialog.Builder(activity)
            alert11.setCancelable(false)
            //alert11.setTitle("Maintenance")
            alert11.setMessage(message)
            alert11.setPositiveButton(
                    "OK",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()
                            //activity.finish()
                            //activity.finishAffinity();
                            /*val intent = Intent(activity, Login::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            activity.startActivity(intent)*/
                        }
                    })
            alert = alert11.create()
            alert.show()
        }else{
            if (!alert.isShowing) {
                val alert11 = AlertDialog.Builder(activity)
                alert11.setCancelable(false)
                //alert11.setTitle("Maintenance")
                alert11.setMessage(message)
                alert11.setPositiveButton(
                        "OK",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                                //activity.finish()
                            }
                        })
                alert = alert11.create()
                alert.show()
            }
        }
    }

    fun CheckingPermissionIsEnabledOrNot(context: Activity): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET)
        val WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val CAMERA = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        //val CALL_PHONE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)

        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED && //&&
                CAMERA == PackageManager.PERMISSION_GRANTED //&&
        //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }

    fun RequestMultiplePermission(context: Activity) {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(context, arrayOf<String>
            (
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CAMERA
        ), Appconstands.RequestPermissionCode)


    }

    fun loading_show(mActivity:Activity,dialog: Dialog) : Dialog {
        val openwith = dialog
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        openwith.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        val popUpView = mActivity.layoutInflater.inflate(R.layout.loading_layout, null)

        //val pronm = popUpView.findViewById(R.id.pronm) as TextView

        openwith.setContentView(popUpView);
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val displaymetrics = DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width =  (displaymetrics.widthPixels * 1);
        val height =  (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //openwith.show()
        return openwith
    }
}