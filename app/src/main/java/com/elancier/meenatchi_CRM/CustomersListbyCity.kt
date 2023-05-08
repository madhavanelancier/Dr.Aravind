package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import com.elancier.meenatchi_CRM.Adapers.CustomersAdapter
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_customers_listby_city.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomersListbyCity : AppCompatActivity() {
    val activity = this
    var customers = ArrayList<Customers>()
    var city =""
    var state =""
    internal lateinit var progbar: Dialog

    var daytxt =""
    var date_txt =""
    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    internal lateinit var fcode: EditText
    var pop: AlertDialog? = null

    lateinit var customersadp : CustomersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers_listby_city)
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        getRequesttypes()
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Feedback List"


        add_family.setOnClickListener {
            editarea()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun getRequesttypes(){
        val obj = JSONObject()
        obj.put("cus_id",pref.getString("cusid",""))
        println("customersbycity : "+obj)
        val call = ApproveUtils.Get.getCustomersByCity(pref.getString("cusid","").toString(),pref.getString("empid","").toString())
        call.enqueue(object : Callback<CustomersListByCity> {
            override fun onResponse(
                call: Call<CustomersListByCity>,
                response: Response<CustomersListByCity>
            ) {
                Log.e("customers", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as CustomersListByCity
                    if (example.status == "Success") {
                        customers = example.response as ArrayList<Customers>
                        customersadp = CustomersAdapter(activity,customers)
                        customerslist.adapter = customersadp
                    } else {
                        Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CustomersListByCity>, t: Throwable) {
                Log.e("customers Fail", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

        })
    }

    fun editarea() {


        val dialogBuilder = AlertDialog.Builder(this)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.addchannels_popup, null)
        fcode = dialogView.findViewById<EditText>(R.id.fcode);
        val tit = dialogView.findViewById<TextView>(R.id.tit);
        val cancel = dialogView.findViewById<ImageButton>(R.id.cancel);
        val add = dialogView.findViewById<Button>(R.id.button)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        pop = dialogBuilder.create()
        pop!!.show()

        cancel.setOnClickListener {
            pop!!.dismiss();
            //doLcatload(type.toString(), "1")

        }
        add.setOnClickListener {

            if (fcode.text.toString().trim().isNotEmpty()
            ) {
                val type = pref.getString("id", "")
                progbar = Dialog(this)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.save)
                progbar.setCancelable(false)
                progbar.show()

                if (Appconstants.net_status(this)) {

                    dosavecatupdate(
                        fcode.text.toString())
                } else {
                    Toast.makeText(applicationContext, "You're offline", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Please fill feedback", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }

    private fun dosavecatupdate(nm: String) {

        val json=JsonObject()
        json.addProperty("cus_id",pref.getString("cusid",""))
        json.addProperty("comment",nm)
        json.addProperty("enter_by",pref!!.getString("empid",""))
        val call = ApproveUtils.Get.feedback_save(json)

        Log.e("inside",nm)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {


                Log.v("responce", response.toString())
                if (response.isSuccessful) {
                    val login = response.body()
                    if (login!!.status == "Success") {
                        progbar.dismiss()
                        pop!!.dismiss()
                        getRequesttypes()

                        toast(login.message!!.toString())
                    } else {
                        progbar.dismiss()

                        Toast.makeText(
                            applicationContext,
                            login.message!!,
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


            override fun onFailure(call: Call<Resp_otp>, throwable: Throwable) {
                Log.e("error", throwable.toString())
                //progbar.dismiss();
                progbar.dismiss()

                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()

                //login.setEnabled(true);
                // Toast.makeText(MainActivity.this,"Failed"+t.toString(),Toast.LENGTH_SHORT).show();

            }
        })
    }
    fun toast(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    override fun onBackPressed() {
        finish()
    }
}