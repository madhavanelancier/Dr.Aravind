package com.elancier.meenatchi_CRM

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_otp
import com.elancier.meenatchi_CRM.retrofit.Resp_otps
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var pDialog: Dialog
    internal  var pref: SharedPreferences?=null
    var religion=""
    internal  var editor: SharedPreferences.Editor?=null
    var sitelogo=""
    val activity = this
    var dialog: ProgressDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_login)

        pref = this.getSharedPreferences("MyPref", 0)

        editor = pref!!.edit()
        //mobile.requestFocus()

        if(pref!!.getString("van_login","").toString().isEmpty()){

        }
        else if(pref!!.getString("van_login","").toString().isNotEmpty()){
            startActivity(Intent(activity,MainActivity::class.java)
                .putExtra("from","Employee"))
            finish()
        }

        login.setOnClickListener {
           /* startActivity(Intent(activity,MainActivity::class.java)
                .putExtra("from","Employee"))
            finish()*/
            if(mobile.text.toString().trim().isNotEmpty()&&password.text.toString().trim().isNotEmpty()){
                    if (Appconstands.net_status(this)) {
                        SendLogin(mobile.text.toString().trim(),password.text.toString())
                    }
            }
            else{

                if(mobile.text.isNullOrBlank()){
                    mobile.setError("Enter Username.")

                }

                if(password.text.isNullOrBlank()){
                    password.setError("Enter password.")

                }
            }
        }

    }
    fun SendLogin(mobile:String,pass:String){

        pDialog= Dialog(activity)
        Appconstands.loading_show(activity, pDialog).show()
        var token=""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("tok", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                 token = task.result?.token.toString()
                //utils.setToken(token!!)
                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                Log.d("token", token!!)
            })
                val device_id = Settings.Secure.getString(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )

        var jsonobj=JsonObject()
        jsonobj.addProperty("username",mobile)
        jsonobj.addProperty("password",pass)
        jsonobj.addProperty("device_id",device_id)
        jsonobj.addProperty("token",token)
        Log.e("json",jsonobj.toString())
        val call = ApproveUtils.Get.getlogin(jsonobj)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr=example.message
                        println("otp"+arr)
                        toast(arr.toString())
                        editor!!.putString("van_login", "true")
                        editor!!.putString("mobile", example.response?.get(0)!!.mobile)
                        editor!!.putString("fname", example.response!![0]!!.first_name)
                        editor!!.putString("empid", example.response!![0]!!.id)
                        editor!!.putString("emptype", "Employee")

                        editor!!.commit()
                        startActivity(Intent(activity,MainActivity::class.java)
                            .putExtra("from","Employee"))
                        finish()
                        //finish()
                    } else {
                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }


    fun toast(msg:String){
        val toast=Toast.makeText(activity,msg,Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }


    @SuppressLint("StaticFieldLeak")
    inner class Areacodes : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);


            Log.i("Areacodes", "started")
        }

        override fun doInBackground(vararg param: String): String? {


            //load()

            return null
        }

        override fun onPostExecute(resp: String?) {

        }
    }



    override fun onBackPressed() {
        editor!!.putString("reg","")
        editor!!.commit()
        val intent = Intent()
        setResult(1, intent)
        finish()
    }

}
