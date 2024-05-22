package com.elancier.meenatchi_CRM

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.elancier.domdox.Adapters.PersonsAdapter
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Edit_Resp
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.patient_add.*
import kotlinx.android.synthetic.main.schedule_meeting.*
import kotlinx.android.synthetic.main.schedule_meeting.fname
import kotlinx.android.synthetic.main.schedule_meeting.save
import kotlinx.android.synthetic.main.schedule_meeting.statespin
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class Schedule_Meeting : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    val activity = this
    var persons = JSONArray()
    lateinit var personadap: PersonsAdapter
    lateinit var pDialog: ProgressDialog
    internal var pref: SharedPreferences? = null
    internal var editor: SharedPreferences.Editor? = null
    var cid = ""
    var eid = ""
    var estate = ""
    var ecity = ""
    private var CalendarHour = 0
    private var CalendarMinute: Int = 0
    private var statearr = ArrayList<String>()
    private var statearr_id = ArrayList<String>()
    private var cityarr = ArrayList<String>()
    var format: String? = null
    var calendar: Calendar? = null
    var timepickerdialog: TimePickerDialog? = null
    var CentresArrays = java.util.ArrayList<OrderDetail>()
    var DoctorName = java.util.ArrayList<String>()
    var userID = ""
    var namePOS = -1
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var myday = 0
    var myMonth = 0
    var myYear = 0
    var myHour = 0
    var myMinute = 0
    var editID = ""
    var from = ""
    var lat = ""
    var long = ""
    var deleteDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_meeting)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        userID = pref!!.getString("empid", "").toString()
        pDialog = ProgressDialog(this)
        pDialog!!.setMessage("Processing...")
        pDialog.show()

        var arr = ArrayList<String>()
        arr.add("Select Status")
        arr.add("Planned")
        arr.add("Rescheduled")
        arr.add("Dropped")
        arr.add("Completed")

        var adap = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arr)
        statespin.adapter = adap
        from = intent.extras!!.getString("from").toString()

        if (from == "Add") {
            ab!!.title = "Schedule Meeting"

        } else {
            ab!!.title = "Edit Meeting"

        }
        try {
            val dcname = intent.extras!!.getString("dcname")
            val date = intent.extras!!.getString("date")
            val status = intent.extras!!.getString("status")

            editID = intent.extras!!.getString("id").toString()
            fname.setText(dcname)
            meeting.setText(date)

            try {
                for (i in 0 until arr.size) {
                    if (status == arr[i]) {
                        statespin.setSelection(i)
                    }
                }
            } catch (e: java.lang.Exception) {

            }

        } catch (e: Exception) {

        }

        Doctors()

        statespin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 4) {
                    feedbacklay.visibility = View.VISIBLE
                } else {
                    feedbacklay.visibility = View.GONE

                }
                //getCity(states[position].state_name!!.toString(), selectedcity)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        fname.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                rowId: Long
            ) {
                val selection = parent.getItemAtPosition(position) as String
                var pos = -1
                for (i in 0 until DoctorName.size) {
                    if (DoctorName.get(i).equals(selection)) {
                        pos = i
                        break
                    }
                }
                println("Position $pos") //check it now in Logcat
                namePOS = pos;
            }
        })

        save.setOnClickListener {
            if (fname.text.toString().trim().isNotEmpty() && meeting.text.toString().trim()
                    .isNotEmpty()
                && statespin.selectedItemPosition != 0
            ) {

                if (from == "Add") {
                    for (i in 0 until DoctorName.size) {
                        if (DoctorName[i].contains(fname.text.toString())) {
                            namePOS = i
                            AddMeeting()
                        } else {

                        }
                    }
                    if (namePOS == -1) {
                        toast("Invalid Doctor Name")
                        fname.setError("Invalid Doctor Name")
                    }
                } else {
                    for (i in 0 until DoctorName.size) {
                        if (DoctorName[i].contains(fname.text.toString())) {
                            namePOS = i
                            val locationManager =
                                getSystemService(LOCATION_SERVICE) as LocationManager
                            val criteria = Criteria()

                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {

                            }
                            var location = locationManager.getLastKnownLocation(
                                locationManager.getBestProvider(
                                    criteria,
                                    false
                                )!!
                            )

                            lat = location!!.latitude.toString()
                            long = location!!.longitude.toString()

                            if (statespin.selectedItemPosition == 4) {
                                if (feedback.text.toString().isNotEmpty()) {
                                    EditMeeting()
                                } else {
                                    if(feedback.text.toString().isEmpty()){
                                        feedback.error = "Required field*"
                                    }
                                }
                            } else {
                                EditMeeting()

                            }
                        } else {

                        }
                    }
                    if (namePOS == -1) {
                        toast("Invalid Doctor Name")
                        fname.setError("Invalid Doctor Name")
                    }
                }

            } else {
                if (fname.text.toString().trim().isEmpty()) {
                    fname.error = "Required field*"
                }
                /* if (hospitalname.text.toString().trim().isEmpty()) {
                     hospitalname.error = "Required field*"
                 }*/
                if (meeting.text.toString().trim().isNotEmpty()) {
                    meeting.error = "Required field*"
                }
                if (statespin.selectedItemPosition == 0) {
                    Toast.makeText(applicationContext, "Please select status", Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }

        meeting.setOnClickListener {
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog =
                DatePickerDialog(this@Schedule_Meeting, this@Schedule_Meeting, year, month, day)
            datePickerDialog.datePicker.setMinDate(System.currentTimeMillis())
            datePickerDialog.show()
        }

    }

    fun toast(msg: String) {
        val t = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
        t.setGravity(Gravity.CENTER, 0, 0)
        t.show()
    }

    private fun AddMeeting() {
        var progressDialog = ProgressDialog(this);
        progressDialog.setMessage("Creating Meeting...");
        progressDialog.show()

        val builder = JsonObject()
        builder.addProperty("doctor", CentresArrays[namePOS].id.toString())
        builder.addProperty("employee", pref!!.getString("empid", "").toString())
        builder.addProperty("meeting_datetime", meeting.text.toString())
        builder.addProperty("status", statespin.selectedItem.toString())


        val file = File("")
        Log.e("request", builder.toString())
        val call: Call<ResponseBody?>? = ApproveUtils.Get.addMeeting(builder)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                Log.w(
                    "json",
                    Gson().toJson(response)
                )
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Meeting Added Successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
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

    private fun EditMeeting() {
        var progressDialog = ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show()

        val builder = JsonObject()
        builder.addProperty("doctor", CentresArrays[namePOS].id.toString())
        builder.addProperty("employee", pref!!.getString("empid", "").toString())
        builder.addProperty("meeting_datetime", meeting.text.toString())
        builder.addProperty("status", statespin.selectedItem.toString())
        builder.addProperty("cur_lat", lat)
        builder.addProperty("cur_long", long)
        builder.addProperty("status_feedback", feedback.text.toString())


        val file = File("")
        Log.e("request", builder.toString())
        val call: Call<Edit_Resp?>? = ApproveUtils.Get.editMeeting(editID, builder)
        call!!.enqueue(object : Callback<Edit_Resp?> {
            override fun onResponse(
                call: Call<Edit_Resp?>,
                response: Response<Edit_Resp?>
            ) {
                Log.w(
                    "json",
                    Gson().toJson(response)
                )
                if (response.isSuccessful) {
                    if (response.body()!!.status == "Success") {
                        Toast.makeText(activity, response.body()!!.response, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        try {
                            val factory =
                                LayoutInflater.from(this@Schedule_Meeting)
                            val deleteDialogView: View =
                                factory.inflate(R.layout.alert_view, null)
                            val tvAlertViewMessage =
                                deleteDialogView.findViewById<TextView>(R.id.tvAlerViewMessage)
                            val close =
                                deleteDialogView.findViewById<TextView>(R.id.close)
                            tvAlertViewMessage.text = response.body()!!.response

                            close.setOnClickListener {
                                deleteDialog!!.dismiss()
                            }
                            deleteDialog =
                                AlertDialog.Builder(this@Schedule_Meeting)
                                    .setCancelable(true).create()


                            //deleteDialog = new AlertDialog.Builder(context).setCancelable(canBeCancelled).create();
                            deleteDialog!!.setView(
                                deleteDialogView
                            )
                            deleteDialog!!.show()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT)

                }


                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<Edit_Resp?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("failure", "Error " + t.message)
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT)

            }
        })

    }


    fun Doctors() {
        if (Appconstants.net_status(this)) {
            CentresArrays.clear()
            DoctorName.clear()
            val call = ApproveUtils.Get.getcustrips(userID)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        pDialog.dismiss()
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val data = OrderDetail()
                                data.customer_name = otpval[i].name
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].mobile.toString()

                                DoctorName.add(data.customer_name + ", " + data.mobile)
                                CentresArrays.add(data)

                            }
                            var adap = ArrayAdapter(
                                this@Schedule_Meeting,
                                android.R.layout.select_dialog_item,
                                DoctorName
                            );
                            fname.setThreshold(2)
                            fname.setAdapter(adap)

                        } else {
                            Toast.makeText(
                                this@Schedule_Meeting,
                                example.getMessage(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            pDialog.dismiss()


                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    pDialog.dismiss()
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Schedule_Meeting,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
            })
        } else {
            Toast.makeText(
                this@Schedule_Meeting,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    override fun onBackPressed() {
        exit()
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

    fun exit() {
        val alert = androidx.appcompat.app.AlertDialog.Builder(this)
        alert.setTitle("Exit?")
        alert.setMessage("Are you sure want to exit?")
        alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            finish()
        })
        alert.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val popup = alert.create()
        popup.show()
    }


    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        myYear = p1
        myday = p3
        myMonth = p2
        val c = Calendar.getInstance()
        hour = c[Calendar.HOUR]
        minute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this@Schedule_Meeting,
            this@Schedule_Meeting,
            hour,
            minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        myHour = p1;
        myMinute = p2;
        var ampm = ""
        var mymins = ""
        if (myHour >= 12) {
            ampm = "PM"
        } else if (myHour < 12) {
            ampm = "AM"
        }
        if (myMinute < 10) {
            mymins = "0" + myMinute
        } else {
            mymins = myMinute.toString()
        }
        meeting.setText(
            myday.toString() + "-" + (myMonth + 1).toString() + "-" +
                    myYear.toString() + " " +
                    myHour.toString() + ":" +
                    mymins.toString() + " " + ampm
        );
    }
}