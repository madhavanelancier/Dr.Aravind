package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.meenatchi_CRM.Adapers.StateCityAdapter
import com.elancier.meenatchi_CRM.retrofit.*
import kotlinx.android.synthetic.main.activity_advance_request.imageView10
import kotlinx.android.synthetic.main.activity_advance_request.imageView9
import kotlinx.android.synthetic.main.activity_advance_request.requesttype
import kotlinx.android.synthetic.main.activity_expense_add.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ExpenseAdd : AppCompatActivity() {
    val activity = this
    var requesttypes = ArrayList<Response_trip>()
    internal lateinit var pref: SharedPreferences
    lateinit var requestypadp : StateCityAdapter
    lateinit var stateadp : StateCityAdapter
    lateinit var cityadp : StateCityAdapter

    var selectedcity : String?=null
    var selectedstate : String?=null

    var states = ArrayList<Response_trip>()
    var cities = ArrayList<Response_trip>()

    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    var imagecode=""
    internal lateinit var fi: File
    var panimage=""
    var bankimage=""
    internal lateinit var byteArray1: ByteArray
    var imagecode1=""
    internal lateinit var fi1: File
    var api_key="";
    var api_secret="";
    var cloud_name="";
    var data=false
    var editid=""

    var BUS=""
    var TRAIN=""
    var AUTO=""
    var CAB=""
    var BREAKFAST=""
    var LUNCH=""
    var DINNER=""
    var Stay=""
    var Others=""

    var currenttype = ""
    var exptype=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_add)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab.title = "Expense"
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        imageView9.setImageResource(R.mipmap.placeholders)
        //imageView10.setImageResource(R.mipmap.placeholders)
        data = intent.hasExtra("data")


        requestypadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, requesttypes, false, false, true)
        requesttype.adapter = requestypadp

        if (Appconstands.net_status(activity)) {
            getRequesttypes()
            //getCloudinary()
        }else{
            Toast.makeText(
                    activity,
                    "Check your Network connection",
                    Toast.LENGTH_LONG
            ).show()
        }

        if(Appconstands.net_status(activity)) {
            //getStates(selectedstate)
            getExpenselimit()
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



        imageView9!!.setOnClickListener {
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity)
            }
        }

        imageView10!!.setOnClickListener {
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage1()
            }
            else{
                RequestMultiplePermission(activity)
            }
        }

        /*{"user":"9791981428","tid":"V00002","expense_type":"2","name":"food",
        "image":"http://teamdev.co.in/vanitha/assets/img/vanitha_logo.png,http://teamdev.co.in/vanitha/assets/img/vanitha_logo.png",
        "description":"Test expense","amount":"1000","city":"","from":"","to":"","by":"","hotel_name":""}*/
        requesttype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(requesttypes[position].type.toString()){
                    "Stay" -> {
                        currenttype = requesttypes[position].type.toString()
                        stay()
                    }
                    "Food" -> {
                        currenttype = requesttypes[position].type.toString()
                        food()
                    }
                    "Travel" -> {
                        currenttype = requesttypes[position].type.toString()
                        travel()
                    }
                    "Others" -> {
                        currenttype = requesttypes[position].type.toString()
                        others()
                    }
                    else -> hideall()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        expenserequest.setOnClickListener {
            if(amount.text.toString().isNotEmpty()) {

                when(currenttype){
                    "Stay" -> {
                        exptype="Stay"
                    }
                    "Food" -> {
                       // saveFood()
                        exptype=meal.selectedItem.toString()

                    }
                    "Travel" -> {
                        //saveTravel()
                        exptype=by.selectedItem.toString()

                    }
                    "Others" -> {
                        //saveOthers()
                        exptype="Others"

                    }
                }

                getExpense("", "", "", amount.text.toString())
            }
            else{
                Toast.makeText(applicationContext, "Please enter amount", Toast.LENGTH_SHORT).show()
                amount.requestFocus()
                amount.setError("Required field")
            }
        }
    }

    fun saveStay(){
        if (hotel.text.isEmpty()){
            hotel.setError("Required Field")
        }
        if (amount.text.toString().isEmpty()){
            amount.setError("Required Field")
        }
        try {
            if (city.selectedItem.toString().isEmpty()) {
                Toast.makeText(activity, "Select city", Toast.LENGTH_SHORT).show()
            }
        }catch (e:java.lang.Exception){
            Toast.makeText(activity, "Select city", Toast.LENGTH_SHORT).show()
            return
        }
        if (hotel.text.isNotEmpty()&&amount.text.isNotEmpty()&&city.selectedItem.toString().isNotEmpty()){

           // if(Stay.isNotEmpty()){
                    sendExpense(name = "Stay",
                        image = imagecode,
                        description = "",
                        amount = amount.text.toString(),
                        state = state.selectedItem.toString(),
                        city = city.selectedItem.toString(),
                        from = "",
                        to = "",
                        by = "",
                        hotel_name = hotel.text.toString())

           // }


        }else{
            return
        }
    }
    fun saveFood(){
        if (city.selectedItem.toString().isEmpty()){
            Toast.makeText(activity, "Select city", Toast.LENGTH_SHORT).show()
        }
        if (city.selectedItem.toString().isNotEmpty()){
            if(meal.selectedItem.toString()=="BREAKFAST") {
               // if (BREAKFAST.isNotEmpty()) {
                        sendExpense(
                            name = meal.selectedItem.toString(),
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = state.selectedItem.toString(),
                            city = city.selectedItem.toString(),
                            from = "",
                            to = "",
                            by = "",
                            hotel_name = hotel.text.toString()
                        )
                  /*  } else {
                        limitpopup("${meal.selectedItem.toString()} Food", amount.text.toString(), BREAKFAST)
                    }*/
                //}
            }
            else if(meal.selectedItem.toString()=="LUNCH") {
                //if (LUNCH.isNotEmpty()) {
                        sendExpense(
                            name = meal.selectedItem.toString(),
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = state.selectedItem.toString(),
                            city = city.selectedItem.toString(),
                            from = "",
                            to = "",
                            by = "",
                            hotel_name = hotel.text.toString()
                        )
                 /*   } else {
                        limitpopup("${meal.selectedItem.toString()} Food", amount.text.toString(), LUNCH)
                    }*/
               // }
            }
            else if(meal.selectedItem.toString()=="DINNER") {
                //if (DINNER.isNotEmpty()) {
                        sendExpense(
                            name = meal.selectedItem.toString(),
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = state.selectedItem.toString(),
                            city = city.selectedItem.toString(),
                            from = "",
                            to = "",
                            by = "",
                            hotel_name = hotel.text.toString()
                        )
                  /*  } else {
                        limitpopup("${meal.selectedItem.toString()} Food", amount.text.toString(), DINNER)
                    }*/
               // }
            }


        }else{
            return
        }
    }
    fun saveTravel(){
        if (from.text.isEmpty()){
            from.setError("Required Field")
        }
        if (to.text.toString().isEmpty()){
            to.setError("Required Field")
        }
        if (amount.text.toString().isEmpty()){
            amount.setError("Required Field")
        }
        if (from.text.isNotEmpty()&&to.text.isNotEmpty()&&amount.text.isNotEmpty()){
            if(by.selectedItem.toString()=="BUS") {
               // if (BUS.isNotEmpty()) {
                        sendExpense(name = "",
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = "",
                            city = "",
                            from = from.text.toString(),
                            to = to.text.toString(),
                            by = by.selectedItem.toString(),
                            hotel_name = "")
                    /*} else {
                        limitpopup("${by.selectedItem.toString()} Travel", amount.text.toString(), BUS)
                    }*/
              //  }
            }
            else if(by.selectedItem.toString()=="TRAIN") {
               // if (TRAIN.isNotEmpty()) {
                    //if (TRAIN.toDouble() >= amount.text.toString().toDouble()) {
                        sendExpense(name = "",
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = "",
                            city = "",
                            from = from.text.toString(),
                            to = to.text.toString(),
                            by = by.selectedItem.toString(),
                            hotel_name = "")
                 /*   } else {
                        limitpopup("${by.selectedItem.toString()} Travel", amount.text.toString(), TRAIN)
                    }*/
               // }

            }
            else if(by.selectedItem.toString()=="CAB") {
              //  if (CAB.isNotEmpty()) {
                   // if (CAB.toDouble() >= amount.text.toString().toDouble()) {
                        sendExpense(name = "",
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = "",
                            city = "",
                            from = from.text.toString(),
                            to = to.text.toString(),
                            by = by.selectedItem.toString(),
                            hotel_name = "")
                 /*   } else {
                        limitpopup("${by.selectedItem.toString()} Travel", amount.text.toString(), CAB)
                    }*/
               // }

            }
            else if(by.selectedItem.toString()=="AUTO") {
               // if (AUTO.isNotEmpty()) {
                   // if (AUTO.toDouble() >= amount.text.toString().toDouble()) {
                        sendExpense(name = "",
                            image = imagecode,
                            description = "",
                            amount = amount.text.toString(),
                            state = "",
                            city = "",
                            from = from.text.toString(),
                            to = to.text.toString(),
                            by = by.selectedItem.toString(),
                            hotel_name = "")
                   /* } else {
                        limitpopup("${by.selectedItem.toString()} Travel", amount.text.toString(), AUTO)
                    }*/
               // }

            }

        }else{
            return
        }
    }

    fun saveOthers(){
        if (description.text.isEmpty()){
            description.setError("Required Field")
        }
        if (amount.text.toString().isEmpty()){
            amount.setError("Required Field")
        }

        if (description.text.isNotEmpty()&&amount.text.isNotEmpty()){
            //if (Others.isNotEmpty()) {
                //if (Others.toDouble() >= amount.text.toString().toDouble()) {
                    sendExpense(
                        name = "Others",
                        image = imagecode,
                        description = description.text.toString(),
                        amount = amount.text.toString(),
                        state = "",
                        city = "",
                        from = "",
                        to = "",
                        by = "",
                        hotel_name = ""
                    )
                /*}
                else {
                    limitpopup("Others", amount.text.toString(), Others)
                }*/
            }
        }


    fun stay(){
        food_lay.visibility=View.GONE
        travel_lay.visibility=View.GONE
        others_lay.visibility=View.GONE

        stay_lay.visibility=View.VISIBLE
        city_lay.visibility=View.VISIBLE
        amount_lay.visibility=View.VISIBLE
    }

    fun food(){
        stay_lay.visibility=View.GONE
        travel_lay.visibility=View.GONE
        others_lay.visibility=View.GONE

        food_lay.visibility=View.VISIBLE
        city_lay.visibility=View.VISIBLE
        amount_lay.visibility=View.VISIBLE
    }

    fun travel(){
        stay_lay.visibility=View.GONE
        food_lay.visibility=View.GONE
        others_lay.visibility=View.GONE
        city_lay.visibility=View.GONE

        travel_lay.visibility=View.VISIBLE
        amount_lay.visibility=View.VISIBLE
    }

    fun hideall(){
        stay_lay.visibility=View.GONE
        food_lay.visibility=View.GONE
        travel_lay.visibility=View.GONE
        city_lay.visibility=View.GONE

        others_lay.visibility=View.VISIBLE
        amount_lay.visibility=View.VISIBLE
    }

    fun others(){
        stay_lay.visibility=View.GONE
        food_lay.visibility=View.GONE
        travel_lay.visibility=View.GONE
        city_lay.visibility=View.GONE

        others_lay.visibility=View.VISIBLE
        amount_lay.visibility=View.VISIBLE
    }

   /* fun getStates(selectedstate: String? = null){
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

    fun getExpenselimit(){
        val call = ApproveUtils.Get.getexplimit("http://teamdev.co.in/vanitha/api/expense_limit")
        call.enqueue(object : Callback<Resp_otps> {
            override fun onResponse(call: Call<Resp_otps>, response: Response<Resp_otps>) {
                Log.e("states", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otps
                    if (example.status == "Success") {
                        BUS= example.response!!.bus.toString()
                        TRAIN=example.response!!.train.toString()
                        AUTO=example.response!!.auto.toString()
                        CAB=example.response!!.cab.toString()
                        BREAKFAST=example.response!!.breakfast.toString()
                        LUNCH=example.response!!.lunch.toString()
                        DINNER=example.response!!.dinner.toString()
                        Stay=example.response!!.stay.toString()
                        Others=example.response!!.others.toString()

                    } else {
                        Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Resp_otps>, t: Throwable) {
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
    }

    /*fun getCity(state: String, selectedcity: String? = null){
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

    fun sendExpense(name : String,
                           image : String,
                           description : String,
                           amount : String,
                           state : String,
                           city : String,
                           from : String,
                           to : String,
                           by : String,
                           hotel_name : String){




        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Submitting Expense ...")
        pDialog.show()
        /*{"user":"9791981428","tid":"V00002","request_type":"4"
        "amount":"1000",
        "tour_plan":[{"tour_date":"","day":"","tour_city":"","customer":""}],
        "expenditure_plan":[{"expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""}]}*/
        val user = pref.getString("mobile", "").toString()
        val tid = pref.getString("tid", "").toString()
        val expense_type = requesttypes[requesttype.selectedItemPosition].id.toString()
        val obj = JSONObject()
        obj.put("user", user)
        obj.put("tid", tid)
        obj.put("expense_type", expense_type)
        obj.put("name", name)
        obj.put("description", description)
        obj.put("amount", amount)
        obj.put("state", state)
        obj.put("city", city)
        obj.put("from", from)
        obj.put("to", to)
        obj.put("by", by)
        obj.put("hotel_name", hotel_name)
        obj.put("image", "data:image/png;base64,$image")

        println("sendExpense : " + obj)
        /*val gson = Gson()
        val listType = object : TypeToken<HashMap<String?, String?>?>() {}.type
        val map: HashMap<String, String> = gson.fromJson(obj.toString(), listType)
        println("sendAdvanceRequestmap : " + map)*/
        val call = ApproveUtils.Get.sendExpense(user,
                tid,
                expense_type,
                name,
            "data:image/png;base64,$image",
                description,
                amount,
                state,
                city,
                from,
                to,
                by,
                hotel_name)//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("advancereq", response.toString())
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
                Log.e("advancereq Fail", t.toString())
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


    fun getExpense(name : String,
                    image : String,
                    description : String,
                    amounts : String){




        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Validating ...")
        pDialog.show()

        var cityval=""
        /*{"user":"9791981428","tid":"V00002","request_type":"4"
        "amount":"1000",
        "tour_plan":[{"tour_date":"","day":"","tour_city":"","customer":""}],
        "expenditure_plan":[{"expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""}]}*/
        val user = pref.getString("mobile", "").toString()
        val tid = pref.getString("tid", "").toString()
        val expense_type = requesttypes[requesttype.selectedItemPosition].id.toString()
        val obj = JSONObject()
        obj.put("user", user)
        obj.put("trip", tid)
        obj.put("expense_type", exptype)
        obj.put("amount", amounts)
        if(exptype=="Stay"){
            obj.put("city", city.selectedItem.toString())
            cityval=city.selectedItem.toString()
        }
        else{
            obj.put("city", "")
            cityval=""
        }


        println("checkExpense : " + obj)
        /*val gson = Gson()
        val listType = object : TypeToken<HashMap<String?, String?>?>() {}.type
        val map: HashMap<String, String> = gson.fromJson(obj.toString(), listType)
        println("sendAdvanceRequestmap : " + map)*/
        val call = ApproveUtils.Get.checkExpense(user,
            tid,
            exptype,
            amounts,
            cityval
            )//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("advancereq", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "0") {
                        amount.setText(null)
                        amount.requestFocus()
                        limitpopup(exptype,amounts,example.getremainAmount().toString())
                    }
                    else{
                        when(currenttype){
                            "Stay" -> {
                                saveStay()
                            }
                            "Food" -> {
                                saveFood()
                            }
                            "Travel" -> {
                                saveTravel()
                            }
                            "Others" -> {
                                saveOthers()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                Log.e("advancereq Fail", t.toString())
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



    fun limitpopup(type:String,amounts:String,limit:String){
      val alertd=AlertDialog.Builder(this)
        alertd.setTitle("Limit Exceed")
        alertd.setMessage("you are exceeding the limit of this expense for the day.")
        alertd.setCancelable(false)
        alertd.setPositiveButton("Ok",DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            //amount.setError("Your limit is Rs.$limit")

        })
        val pop=alertd.create()
        pop.show()
    }

    fun getRequesttypes(){
        val call = ApproveUtils.Get.getexptype()
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("requesttypes", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "Success") {
                        requesttypes = example.getResponse() as ArrayList<Response_trip>
                        requestypadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, requesttypes, false, false, true)
                        requesttype.adapter = requestypadp
                        if (!currenttype.isNullOrEmpty()){
                            for (h in 0 until requesttypes.size) {
                                if (requesttypes[h].type == currenttype) {
                                    requesttype.setSelection(h)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                Log.e("requesttypes Fail", t.toString())
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

    fun CheckingPermissionIsEnabledOrNot(context: Activity): Boolean {
        val INTERNET = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        val ACCESS_NETWORK_STATEt = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATEt == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED&&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED

    }

    fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 102)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
    fun selectImage1() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 103)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun getImgPath(uri: Uri?): String? {
        val result: String?
        val cursor = activity!!.contentResolver.query(uri!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        // Log.i("utilsresult", result!! + "")
        return result

    }
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                Environment.getExternalStorageDirectory().toString() + "IMAGE_DIRECTORY"
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(
                    wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                    activity!!,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
    private fun resize(image: Bitmap, maxWidth:Int, maxHeight:Int): Bitmap {
        if (maxHeight > 0 && maxWidth > 0)
        {
            val width = image.getWidth()
            val height = image.getHeight()
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap)
            {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            }
            else
            {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            val images = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            return images
        }
        else
        {
            return image
        }
    }

    private fun RequestMultiplePermission(context: Activity) {
        ActivityCompat.requestPermissions(context, arrayOf<String>(

                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        ), RequestPermissionCode
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                var picturePath: String? = null
                var selectedImage = data!!.data
                picturePath = getImgPath(selectedImage)
                fi = File(picturePath!!)
                val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
                Log.i(
                        "pathsizeeeeee",
                        (fi.length() / 1024).toString() + "      " + yourSelectedImage
                )
                //val image1 = CommonFunctions.decodeFile1(picturePath, 0, 0)
                val bitmap = MediaStore.Images.Media.getBitmap(
                        activity!!.getBaseContext().getContentResolver(),
                        selectedImage
                )

                val resizeBitmap =
                        resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                imageView9!!.setImageBitmap(resizeBitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                byteArray = stream.toByteArray()
                imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                Log.e("imagecode", imagecode)

                val path = getImgPath(selectedImage!!)
                //choose_files.setText("Remove Image")
                if (path != null) {
                    val f = File(path!!)
                }
            } else {

            }
        }
        else if (requestCode == 102) {
            try {
                var selectedImageUri = data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                        activity!!.getBaseContext().getContentResolver(),
                        selectedImageUri
                )
                val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                imageView9!!.setImageBitmap(resizeBitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                byteArray = stream.toByteArray()
                imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                val path = getImgPath(selectedImageUri!!)
                if (path != null) {
                    val f = File(path!!)
                    selectedImageUri = Uri.fromFile(f)
                }
            }
            catch (e: Exception) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap?
                val stream = ByteArrayOutputStream()
                thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                byteArray = stream.toByteArray()
                imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                saveImage(thumbnail)
                imageView9!!.setImageBitmap(thumbnail)
            }

        }
        else if (requestCode ==2) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                var picturePath: String? = null
                var selectedImage = data!!.data
                picturePath = getImgPath(selectedImage)
                fi1 = File(picturePath!!)
                val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
                //Log.i("original path1", picturePath + "")
                Log.i(
                        "pathsizeeeeee",
                        (fi1.length() / 1024).toString() + "      " + yourSelectedImage
                )
                val bitmap = MediaStore.Images.Media.getBitmap(
                        activity!!.getBaseContext().getContentResolver(),
                        selectedImage
                )
                val resizeBitmap =
                        resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                imageView10!!.setImageBitmap(resizeBitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                byteArray1 = stream.toByteArray()
                imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                Log.e("imagecode", imagecode1)
                val path = getImgPath(selectedImage!!)
                if (path != null) {
                    val f = File(path!!)
                }
            } else {

            }
        }
        else if (requestCode == 103) {
            try {
                var selectedImageUri = data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                        activity!!.getBaseContext().getContentResolver(),
                        selectedImageUri
                )
                val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                imageView10!!.setImageBitmap(resizeBitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                byteArray1 = stream.toByteArray()
                imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                val path = getImgPath(selectedImageUri!!)
                if (path != null) {
                    val f = File(path!!)
                    selectedImageUri = Uri.fromFile(f)

                }
            } catch (e: Exception) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap?
                val stream = ByteArrayOutputStream()
                thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                byteArray1 = stream.toByteArray()
                imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                saveImage(thumbnail)
                imageView10!!.setImageBitmap(thumbnail)
            }

        }
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
}