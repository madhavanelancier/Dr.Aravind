
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
import android.widget.Toast
import com.elancier.meenatchi_CRM.Appconstands.loading_show
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_verify.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class Verify_Activity : AppCompatActivity() {

    internal  var pref: SharedPreferences?=null
    var religion=""
    internal  var editor: SharedPreferences.Editor?=null
    var sitelogo=""
    var token=""
    lateinit var pDialog: Dialog
    val activity = this

    var otp=""
    var mobileval=""
    var devid=""
    var dialog: ProgressDialog?=null

    var mVerificationId = ""
    var firebaseAuth: FirebaseAuth? = null
    var changedCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance();

        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()

        var intent=intent.extras
        otp=intent!!.getString("otp").toString()
        mobileval=intent!!.getString("mobile").toString()

        //mobile.setText(otp)
        sendVerificationCode(mobileval);

        login.setOnClickListener {

            //if(mobile.text.toString().trim()==otp) {
            if(mobile.text.toString().trim().isNotEmpty()) {
                //CheckSigninOtp(mobileval, otp, mobile.text.toString().trim())
                verifyVerificationCode(mobile.text.toString().trim())
            }
            else{
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }


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

    fun CheckSigninOtp(mob:String,otp:String,eotp:String){

        /*pDialog= Dialog(activity)
        loading_show(activity,pDialog).show()
        FirebaseApp.initializeApp(this);*/

        Log.e("otplogin",mob+otp+eotp)

        //token=FirebaseInstanceId.getInstance().token.toString()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("tok", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                //utils.setToken(token!!)
                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                Log.d("token", token!!)
                val device_id = Settings.Secure.getString(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                val ob= JSONObject()
                ob.put("mobile",mob)
                ob.put("otp",otp)
                ob.put("user_otp",eotp)
                ob.put("device_id",device_id)
                ob.put("token",token)
                Log.d("verify",ob.toString())
                val call = ApproveUtils.Get.getotp(mob,otp,eotp,device_id,token.toString())
                call.enqueue(object : Callback<Resp_otps> {
                    override fun onResponse(call: Call<Resp_otps>, response: Response<Resp_otps>) {
                        Log.e("responce", response.toString())

                        if (response.isSuccessful()) {
                            val example = response.body() as Resp_otps
                            println(example)
                            if (example.status == "Success"){
                                var name=""
                                var userid=example.response!!

                                //var name=example.response!![0].name
                                editor!!.putString("van_login", "true")
                                editor!!.putString("mobile", userid.mobile)
                                editor!!.putString("fname", userid.first_name)
                                editor!!.putString("lname", userid.last_name)
                                editor!!.putString("email", userid.email)
                                editor!!.commit()

                                Toast.makeText(
                                    activity,
                                    example.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                startActivity(Intent(activity,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                finish()

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

                    override fun onFailure(call: Call<Resp_otps>, t: Throwable) {
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
            })

    }


    override fun onBackPressed() {
        editor!!.putString("reg","")
        editor!!.commit()
        val intent = Intent()
        setResult(1, intent)
        finish()
    }

    private fun sendVerificationCode(mobile: String) {
        pDialog=Dialog(activity)
        loading_show(activity, pDialog).show()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobile",
            60,
            TimeUnit.SECONDS,
            activity,
            mCallbacks
        )
    }

    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    otp = code
                    println("otp : " + code)
                    mobile.setText(code)
                    //verifying the code
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(activity.javaClass.name,e.message!!)
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                if (pDialog.isShowing){
                    pDialog.dismiss()
                }
            }

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                mVerificationId = s

                //mResendToken = forceResendingToken
                //mobile.isEnabled = false
                //submit.setText("Login To continue")
                //enotp.visibility = View.VISIBLE
                Toast.makeText(
                    activity,
                    "OTP has been sent",
                    Toast.LENGTH_LONG
                ).show()
                if (pDialog.isShowing){
                    pDialog.dismiss()
                }
            }
        }

    private fun verifyVerificationCode(otp: String) {
        //creating the credential

        Log.e("value",otp)
        val credential = PhoneAuthProvider.getCredential(mVerificationId, otp)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        pDialog=Dialog(activity)
        loading_show(activity, pDialog).show()
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener(this,
            OnCompleteListener { task: Task<AuthResult?> ->
                try {
                    if (task.isSuccessful) {
                        //updateStatusLogin("success", mobileNO)
                        CheckSigninOtp(
                            mobileval,
                            mobile.text.toString().trim(),
                            mobile.text.toString().trim()
                        )
                    } else {
                        //updateStatusLogin("failed", mobileNO)
                    }
                } catch (e: Exception) {
                    FirebaseAuth.getInstance().signOut()
                    e.printStackTrace()
                }
            })
    }

}
