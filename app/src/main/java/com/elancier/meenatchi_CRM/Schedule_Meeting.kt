package com.elancier.meenatchi_CRM

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.elancier.domdox.Adapters.PersonsAdapter
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.schedule_meeting.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import android.text.format.DateFormat;

class Schedule_Meeting : AppCompatActivity(),DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    val activity = this
    var persons = JSONArray()
    lateinit var personadap : PersonsAdapter
    lateinit var pDialog : ProgressDialog
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
    var CentresArrays = java.util.ArrayList<OrderDetail>()
    var DoctorName = java.util.ArrayList<String>()
    var userID=""
    var namePOS=0
    var day = 0
    var month = 0
    var year= 0
    var hour= 0
    var minute= 0
    var myday = 0
    var myMonth = 0
    var myYear = 0
    var myHour = 0
    var myMinute= 0
    var editID=""
    var from=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_meeting)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Schedule Meeting"

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        userID =pref!!.getString("empid", "").toString()
        pDialog = ProgressDialog(this)
        pDialog!!.setMessage("Processing...")
        pDialog.show()

        var arr=ArrayList<String>()
        arr.add("Select Status")
        arr.add("Planned")
        arr.add("Rescheduled")
        arr.add("Dropped")
        arr.add("Completed")

        var adap=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,arr)
        statespin.adapter=adap
        from=intent.extras!!.getString("from").toString()

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
            }
            catch (e:java.lang.Exception){

            }

        }
        catch (e:Exception){

        }

        Doctors()

        fname.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, rowId: Long) {
                val selection = parent.getItemAtPosition(position) as String
                var pos = -1
                for (i in 0 until DoctorName.size) {
                    if (DoctorName.get(i).equals(selection)) {
                        pos = i
                        break
                    }
                }
                println("Position $pos") //check it now in Logcat
                namePOS=pos;
            }
        })

        save.setOnClickListener {
            if (fname.text.toString().trim().isNotEmpty() && meeting.text.toString().trim().isNotEmpty()
                && statespin.selectedItemPosition!=0) {

                if(from=="Add") {
                    AddMeeting()
                }
                else
                {
                    EditMeeting()
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
                if (statespin.selectedItemPosition==0) {
                    Toast.makeText(applicationContext, "Please select status", Toast.LENGTH_SHORT).show()
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
            datePickerDialog.show()
        }

    }
    private fun AddMeeting() {
        var progressDialog =  ProgressDialog(this);
        progressDialog.setMessage("Creating Meeting...");
        progressDialog.show()

        val builder = JsonObject()
        builder.addProperty("doctor", CentresArrays[namePOS].id.toString())
        builder.addProperty("employee", pref!!.getString("empid","").toString())
        builder.addProperty("meeting_datetime", meeting.text.toString())
        builder.addProperty("status", statespin.selectedItem.toString())


        val file = File("")
        Log.e("request",builder.toString())
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
                if(response.isSuccessful) {
                    Toast.makeText(activity, "Meeting Added Successfully", Toast.LENGTH_SHORT)
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

    private fun EditMeeting() {
        var progressDialog =  ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show()

        val builder = JsonObject()
        builder.addProperty("doctor", CentresArrays[namePOS].id.toString())
        builder.addProperty("employee", pref!!.getString("empid","").toString())
        builder.addProperty("meeting_datetime", meeting.text.toString())
        builder.addProperty("status", statespin.selectedItem.toString())


        val file = File("")
        Log.e("request",builder.toString())
        val call: Call<ResponseBody?>? = ApproveUtils.Get.editMeeting(editID)
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
                    Toast.makeText(activity, "Meeting Updated Successfully", Toast.LENGTH_SHORT)
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


    fun Doctors(){
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
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val data= OrderDetail()
                                data.customer_name = otpval[i].name
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].mobile.toString()

                                DoctorName.add(data.customer_name+", "+data.mobile)
                                CentresArrays.add(data)

                            }
                            var adap = ArrayAdapter(this@Schedule_Meeting,android.R.layout.select_dialog_item, DoctorName);
                            fname.setThreshold(2)
                            fname.setAdapter(adap)

                        } else {
                            Toast.makeText(this@Schedule_Meeting, example.getMessage(), Toast.LENGTH_SHORT)
                                .show()
                            pDialog.dismiss()


                        }
                    }
                    else{

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
        }
        else{
            Toast.makeText(
                this@Schedule_Meeting,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
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
        meeting.setText(myday.toString()+"-" + myMonth.toString() + "-" +
            myYear.toString() + " " +
            myHour.toString() + ":" +
             myMinute.toString());
    }
}