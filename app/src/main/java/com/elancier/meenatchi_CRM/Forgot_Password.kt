package com.elancier.meenatchi_CRM

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Forgot_Password : AppCompatActivity() {
    internal  var pref: SharedPreferences?=null
    var religion=""
    internal  var editor: SharedPreferences.Editor?=null
    var sitelogo=""

    var dialog: ProgressDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot__password)

        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        religion=pref!!.getString("username","").toString()
        var url=""

    }

    @SuppressLint("StaticFieldLeak")
    inner class Areacodes : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {

            Log.i("Areacodes", "started")
        }

        override fun doInBackground(vararg param: String):String? {

            return null
        }

        override fun onPostExecute(resp: String?) {

        }
    }

    /*fun load(){

        runOnUiThread {
            dialog =ProgressDialog(this)
            dialog!!.setMessage("Processing...")
            dialog!!.show()
        }
        if (Appconstants.net_status(this@Forgot_Password)) {

            //Log.e("hj",username.text.toString().trim()+religion)
            val call = ApproveUtils.Get.forgot(username.text.toString().trim(),religion)

            call.enqueue(object : Callback<Login_Resp> {
                override fun onResponse(
                    call: Call<Login_Resp>,
                    response: Response<Login_Resp>
                ) {
                    Log.e("getLogin responce",response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Login_Resp
                        //println(example)
                        Log.e("inside",example.toString())


                        if (example.status == "Success") {
                            dialog!!.dismiss()

                            Log.e("inside","in")
                            val mem = example.response!!
                            runOnUiThread {
                                Toast.makeText(applicationContext,mem.toString(), Toast.LENGTH_SHORT).show()
                            }




                        }
                        else {
                            // Log.e("inside",example.id)
                            val mem = example.response!!

                            runOnUiThread {
                                Toast.makeText(applicationContext,mem.toString(), Toast.LENGTH_SHORT).show()
                            }
                            runOnUiThread {
                                dialog!!.dismiss()
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Login_Resp>, t: Throwable) {
                    Log.e("getLogin Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Forgot_Password,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {


                    }

                }
            })
        }
    }*/




}
