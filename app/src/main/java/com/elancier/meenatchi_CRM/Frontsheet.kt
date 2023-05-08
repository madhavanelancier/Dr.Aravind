package com.elancier.meenatchi_CRM

import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import com.elancier.meenatchi_CRM.Adapers.StateCityAdapter
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.elancier.meenatchi_CRM.retrofit.Response_trip
import kotlinx.android.synthetic.main.activity_eod_frontsheet.*
import kotlinx.android.synthetic.main.activity_expense_add.city
import kotlinx.android.synthetic.main.activity_expense_add.state
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.view.MotionEvent

import android.view.GestureDetector
import kotlinx.android.synthetic.main.activity_eod_frontsheet.eodsubmit


class Frontsheet : AppCompatActivity(), GestureDetector.OnGestureListener {
    val activity = this
    internal lateinit var pref: SharedPreferences
    lateinit var stateadp : StateCityAdapter
    lateinit var cityadp : StateCityAdapter
    private var gestureScanner: GestureDetector? = null
    var selectedcity : String?=null
    var selectedstate : String?=null
    var context: Context?=null

    var states = ArrayList<Response_trip>()
    var cities = ArrayList<Response_trip>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eod_frontsheet)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab.title = "Front Sheet"
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        val fname = pref.getString("fname","")
        val lname = pref.getString("lname","")
        exe_name.setText(fname+" "+lname)
        context=this
        /*http://teamdev.co.in/vanitha/api/frontsheet
        { "user":"35","date":"2021-02-11"}*/
        if(Appconstands.net_status(activity)) {
            getEOD()
        }else{
            Toast.makeText(activity, "Check your Internet Connection and, Try again!", Toast.LENGTH_SHORT).show()
            finish()
        }
        stateadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, states, true, false, false)
        state.adapter=stateadp
        state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(Appconstands.net_status(activity)) {
                    //getCity(states[position].state_name!!.toString(), selectedcity)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



        from_time.setOnClickListener { // Get Current Time
            val c = Calendar.getInstance()
            val mHour = c[Calendar.HOUR_OF_DAY]
            val mMinute = c[Calendar.MINUTE]

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                activity,
                { view, hourOfDay, minute ->
                    println("from hourOfDay : "+"$hourOfDay")
                    println("from minute : "+"$minute")
                    var hour = hourOfDay.toString()
                    var min = minute.toString()
                    var ap = "AM"
                    if (hourOfDay > 12) {
                        hour = if((hourOfDay - 12)<=9)"0"+(hourOfDay - 12).toString() else (hourOfDay - 12).toString()
                        ap = "PM"
                    } else if (hourOfDay == 12) {
                        ap = "PM"
                    }
                    if (hourOfDay <=9) {
                        hour = "0$hourOfDay"
                    }
                    if (min.toInt() <=9) {
                        min = "0$min"
                    }
                    if (hourOfDay==0){
                        hour=12.toString()
                    }
                    println("from : "+"$hour:$min $ap")
                    from_time.text = "$hour:$min $ap"
                }, mHour, mMinute, false
            )
            timePickerDialog.show()
        }
        to_time.setOnClickListener { // Get Current Time
            val c = Calendar.getInstance()
            val mHour = c[Calendar.HOUR_OF_DAY]
            val mMinute = c[Calendar.MINUTE]

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                activity,
                { view, hourOfDay, minute ->
                    println("to hourOfDay : "+"$hourOfDay")
                    println("to minute : "+"$minute")
                    var hour = hourOfDay.toString()
                    var min = minute.toString()
                    var ap = "AM"
                    if (hourOfDay > 12) {
                        hour =  if((hourOfDay - 12)<=9)"0"+(hourOfDay - 12).toString() else (hourOfDay - 12).toString()
                        ap = "PM"
                    } else if (hourOfDay == 12) {
                        ap = "PM"
                    }
                    if (hourOfDay <=9) {
                        hour = "0$hourOfDay"
                    }
                    if (min.toInt() <=9 ) {
                        min = "0$min"
                    }
                    if (hourOfDay==0){
                        hour=12.toString()
                    }
                    println("to : "+"$hour:$min $ap")
                    to_time.text = "$hour:$min $ap"
                }, mHour, mMinute, false
            )
            timePickerDialog.show()
        }



        eodsubmit.setOnClickListener {
            if (from_time.text.isNotEmpty()&&to_time.text.isNotEmpty()&&planned.text.isNotEmpty()&&executed.text.isNotEmpty()) {
                sendEOD()
            }else{
                if (from_time.text.isEmpty()&&to_time.text.isNotEmpty()&&planned.text.isNotEmpty()&&executed.text.isNotEmpty()) {
                    from_time.setError("Required Field")
                }
                if (to_time.text.isEmpty()&&planned.text.isNotEmpty()&&executed.text.isNotEmpty()) {
                    to_time.setError("Required Field")
                }
                if (planned.text.isEmpty()&&executed.text.isNotEmpty()) {
                    planned.setError("Required Field")
                }
                if (executed.text.isEmpty()) {
                    executed.setError("Required Field")
                }
                return@setOnClickListener
            }
        }





    }




  /*  fun getStates(selectedstate: String? = null){
        val call = ApproveUtils.Get.getstates(pref.getString("mobile", "").toString())
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("states", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "Success") {
                        states = example.getResponse() as ArrayList<Response_trip>
                        stateadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, states, true, false, false)
                        state.adapter = stateadp
                        if (selectedstate != null) {
                            for (h in 0 until states.size) {
                                if (states[h].state_name == selectedstate) {
                                    state.setSelection(h)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                Log.e("states Fail", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        })
    }*/



   /* fun getCity(state: String, selectedcity: String? = null){
        if (Appconstants.net_status(this)) {
            val obj = JSONObject()
            obj.put("state", state)
            *//*citynmarr.clear()
            ctyidarr.clear()*//*
            cities.clear()
            cityadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, cities, false, true, false)
            city.adapter=cityadp
            val call = ApproveUtils.Get.getcities(state)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("cities", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            cities = example.getResponse() as ArrayList<Response_trip>
                            cityadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, cities, false, true, false)
                            city.adapter = cityadp
                            if (selectedcity != null) {
                                for (h in 0 until cities.size) {
                                    if (cities[h].city_name == selectedcity) {
                                        city.setSelection(h)
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                        }
                        if (example.getStatus() == "Success") {
                            *//* if(frm=="customer"){
                                citynmarr.add("Select City")
                            }
                            else{
                                //nmarr.add("Expense Type")

                            }
                            ctyidarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val id=otpval[i].id.toString()
                                val type=otpval[i].city_name.toString()
                                citynmarr.add(type)
                                ctyidarr.add(id)

                            }
                            val spin=ArrayAdapter(this@Customer_Add,R.layout.support_simple_spinner_dropdown_item,citynmarr)
                            cityspin.adapter=spin
                            pDialog.dismiss()*//*

                        } else {
                            *//*   Toast.makeText(this@Customer_Add, example.getStatus(), Toast.LENGTH_SHORT)
                                   .show()*//*
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("cities Fail", t.toString())
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
        else{
            Toast.makeText(
                activity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }


    }*/

    fun getEOD(){
        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Loading...")
        pDialog.show()
        val user = pref.getString("mobile", "").toString()
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val obj = JSONObject()
        obj.put("user",user)
        obj.put("date",date)
        println("getEOD : "+obj)
        val call = ApproveUtils.Get.getEOD(user,date)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("states", response.toString())
                if (response.isSuccessful()) {
                    val example = JSONObject(response.body()!!.string())
                    if (example.getString("status") == "Success") {
                        try {
                            val ob = example.getJSONObject("response")
                            /*{
                                "status": "Success",
                                "message": "",
                                "response": {
                                    "id": 4,
                                    "date": "2021-02-12",
                                    "time": "10:34:25",
                                    "user": "35",
                                    "state": "Tamil Nadu",
                                    "city": "Chennai",
                                    "start_time": "10:00 AM",
                                    "exit_time": "07:00 PM",
                                    "planned": "4",
                                    "executed": "5"
                                }
                            }*/
                            //val user = if (ob.has("user")) ob.getString("user") else ""
                            val state = if (ob.has("state")) ob.getString("state") else ""
                            val city = if (ob.has("city")) ob.getString("city") else ""
                            val start_time =
                                if (ob.has("start_time")) ob.getString("start_time") else ""
                            val exit_time =
                                if (ob.has("exit_time")) ob.getString("exit_time") else ""
                            val planned_ = if (ob.has("planned")) ob.getString("planned") else ""
                            val executed_ = if (ob.has("executed")) ob.getString("executed") else ""
                            val turnover_ = if (ob.has("turnover")) ob.getString("turnover") else ""
                            selectedstate = state
                            selectedcity = city
                            from_time.setText(start_time)
                            to_time.setText(exit_time)
                            planned.setText(planned_)
                            executed.setText(executed_)
                            turnover.setText(turnover_)
                            //getStates(selectedstate)
                        }catch (e:Exception){
                            finish()
                        }
                    } else {
                        Toast.makeText(activity, example.getString("message"), Toast.LENGTH_SHORT).show()
                        //getStates(selectedstate)
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("states Fail", t.toString())
                pDialog.dismiss()
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()

                }
                finish()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun sendEOD(){
        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Submitting EOD ...")
        pDialog.show()
        /*http://teamdev.co.in/vanitha/api/frontsheet_entry
        { "user":"35", "state":"Tamil Nadu", "city":"Madurai",
        "start_time":"11:00 AM", "exit_time":"07:00 PM", "planned":"1", "executed":"2"}*/
        val user = pref.getString("mobile", "").toString()
        val tid = pref.getString("tid", "").toString()
        val expense_type = ""
        val obj = JSONObject()
        obj.put("user", user)
        obj.put("state", state.selectedItem.toString())
        obj.put("city", city.selectedItem.toString())
        obj.put("start_time", from_time.text.toString())
        obj.put("exit_time", to_time.text.toString())
        obj.put("planned", planned.text.toString())
        obj.put("executed", executed.text.toString())
        obj.put("turnover", turnover.text.toString())
        println("sendEOD : " + obj)
        /*val gson = Gson()
        val listType = object : TypeToken<HashMap<String?, String?>?>() {}.type
        val map: HashMap<String, String> = gson.fromJson(obj.toString(), listType)
        println("sendAdvanceRequestmap : " + map)*/
        val call = ApproveUtils.Get.sendEOD(user,
                state.selectedItem.toString(),
                city.selectedItem.toString(),
                from_time.text.toString(),
                to_time.text.toString(),
                planned.text.toString(),
                executed.text.toString(),
                turnover.text.toString()
        )//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("eod", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                    if (example.getStatus() == "Success") {
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                Log.e("eod Fail", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()

                }
                pDialog.dismiss()
            }

        })
    }

    override fun onDown(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onShowPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }


}