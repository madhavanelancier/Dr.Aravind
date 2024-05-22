package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elancier.domdox.Adapters.PersonsAdapter
import com.elancier.meenatchi_CRM.retrofit.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AdapterView

import android.widget.AdapterView.OnItemSelectedListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.meeting_add.addresstxt
import kotlinx.android.synthetic.main.meeting_add.fname
import kotlinx.android.synthetic.main.meeting_add.save
import kotlinx.android.synthetic.main.meeting_add.statespin
import kotlinx.android.synthetic.main.schedule_meeting.*
import okhttp3.ResponseBody
import java.io.File


class Meeting_Add : AppCompatActivity() {
    val activity = this
    var persons = JSONArray()
    lateinit var personadap : PersonsAdapter
    lateinit var pDialog : Dialog
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    var cid = ""
    var eid = ""
    var estate=""
    var ecity=""
    private var CalendarHour = 0
    private  var CalendarMinute:Int = 0
    private  var statearr=ArrayList<String>()
    private  var statearr_id=ArrayList<String>()
    private  var cityarr=ArrayList<String>()
    var format: String? = null
    var calendar: Calendar? = null
    var timepickerdialog: TimePickerDialog? = null
    var editID=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meeting_add)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Meeting Feedback"

        val dcname=intent.extras!!.getString("dcname")
        val date=intent.extras!!.getString("date")
        editID=intent.extras!!.getString("id").toString()

        fname.setText(dcname)
        addresstxt.setText(date)

        save.setOnClickListener {
            if (feedback.text.toString().trim().isNotEmpty()) {
                EditFeedback()
            } else {
                if (feedback.text.toString().trim().isEmpty()) {
                    feedback.error = "Required field*"
                }

            }
        }

    }
    private fun EditFeedback() {
        var progressDialog =  ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show()

        val builder = JsonObject()
        builder.addProperty("feedback", feedback.text.toString())
        val file = File("")
        Log.e("request",builder.toString())
        val call: Call<ResponseBody?>? = ApproveUtils.Get.editFeedback(editID,builder)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                Log.w(
                    "json",
                    Gson().toJson(response)
                )
                if(response.isSuccessful) {
                    Toast.makeText(activity, "Feedback Updated Successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
                else{
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT)

                }


                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("failure", "Error " + t.message)
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT)

            }
        })

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}