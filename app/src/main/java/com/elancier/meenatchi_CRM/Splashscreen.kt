package com.elancier.meenatchi_CRM

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elancier.meenatchi_CRM.retrofit.Utils
import kotlinx.android.synthetic.main.activity_splashscreen.*

class Splashscreen : AppCompatActivity() {
    val tag = "Welcome"
    val activity = this
    lateinit var utils: Utils
    lateinit var pDialog: Dialog
    private var dots: Array<TextView?> = emptyArray()
    private var layouts: IntArray? = null
    internal  var pref: SharedPreferences?=null

    internal  var editor: SharedPreferences.Editor?=null

    //private PrefManager prefManager;
    //internal var utils: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splashscreen)
        utils = Utils(activity)

        pref = this.getSharedPreferences("MyPref", 0)

        editor = pref!!.edit()
        logo.visibility = View.VISIBLE
        //loading our custom made animations
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        //starting the animation
        logo.startAnimation(animation)
        Handler().postDelayed(Runnable {
            if (CheckingPermissionIsEnabledOrNot()) {
                if(pref!!.getString("van_login","")!!.isNotEmpty()){
                    startActivity(Intent(this,MainActivity::class.java)
                        .putExtra("from","Employee").
                        putExtra("name",pref!!.getString("fname",""))
                        .putExtra("id",pref!!.getString("empid","")))
                    finish()


                }
                else{
                    startActivity(Intent(activity, LoginActivity::class.java))
                    finish()
                }



            }else{
                RequestMultiplePermission()
            }
        }, 3000)

    }
    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
        val WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val CAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
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
    fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this, arrayOf<String>
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            Appconstands.RequestPermissionCode ->

                if (grantResults.size > 0) {
                    val INTERNET = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    val READ_EXTERNAL_STORAGE = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_NETWORK_STATE = grantResults[3] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_FINE_LOCATION = grantResults[4] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_COARSE_LOCATION = grantResults[5] == PackageManager.PERMISSION_GRANTED
                    val CAMERA = grantResults[6] == PackageManager.PERMISSION_GRANTED

                    if (INTERNET &&
                        WRITE_EXTERNAL_STORAGE &&
                        READ_EXTERNAL_STORAGE &&
                        ACCESS_NETWORK_STATE &&
                        ACCESS_FINE_LOCATION &&
                        ACCESS_COARSE_LOCATION&&CAMERA) {
                        if(pref!!.getString("van_login","")!!.isNotEmpty()){
                            startActivity(Intent(activity, MainActivity::class.java)
                                .putExtra("from","Employee")
                            )
                            finish()
                        }
                        else{
                            startActivity(Intent(activity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        //Toast.makeText(this@MainFirstActivity, "Permission Denied", Toast.LENGTH_LONG).show()
                        RequestMultiplePermission()
                    }
                }

        }
    }

}