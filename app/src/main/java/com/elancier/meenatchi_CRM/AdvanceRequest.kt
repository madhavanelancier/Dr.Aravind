package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.meenatchi_CRM.Adapers.ExpenditurePlanAdapter
import com.elancier.meenatchi_CRM.Adapers.StateCityAdapter
import com.elancier.meenatchi_CRM.Adapers.TourPlanAdapter
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_advance_request.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdvanceRequest : AppCompatActivity(),TourPlanAdapter.OnItemClickListener,ExpenditurePlanAdapter.OnItemClickListener {
    val activity = this
    var TourPlans = JSONArray()
    var ExpenditurePlans = JSONArray()
    val CustomerSelect = 31
    lateinit var touradp : TourPlanAdapter
    lateinit var expenditureadp : ExpenditurePlanAdapter
    lateinit var stateadp : StateCityAdapter
    lateinit var cityadp : StateCityAdapter
    lateinit var requestypadp : StateCityAdapter
    var states = ArrayList<Response_trip>()
    var cities = ArrayList<Response_trip>()
    var requesttypes = ArrayList<Response_trip>()

    lateinit var state : Spinner
    lateinit var city : Spinner
    var tot = 0
    internal lateinit var pref: SharedPreferences

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advance_request)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab.title = "Advance Request"
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        imageView9.setImageResource(R.mipmap.placeholders)
        imageView10.setImageResource(R.mipmap.placeholders)
        data = intent.hasExtra("data")
        if (data){
            val obj = JSONObject(intent.extras!!.get("data").toString())
            editid = if (obj.has("id")) obj.getString("id") else ""
            val type = if (obj.has("type")) obj.getString("type") else ""
            val name = if (obj.has("name")) obj.getString("name") else ""
            val user = if (obj.has("user")) obj.getString("user") else ""
            val tid = if (obj.has("tid")) obj.getString("tid") else ""
            val image = if (obj.has("image")) obj.getString("image") else ""
            val image1 = if (obj.has("image1")) obj.getString("image1") else ""
            val description_val = if (obj.has("description")) obj.getString("description") else ""
            val amount = if (obj.has("amount")) Appconstands.rupees+obj.getString("amount") else ""
            val status = if (obj.has("status")) obj.getString("status") else ""
            val approved_amount = if (obj.has("approved_amount")) Appconstands.rupees+obj.getString("approved_amount") else ""
            val notes = if (obj.has("notes")) obj.getString("notes") else ""
            val approved_date = if (obj.has("approved_date")) obj.getString("approved_date") else ""
            val tour_plan = if (obj.has("tour_plan")) obj.getJSONArray("tour_plan") else JSONArray()
            val expenditure_plan = if (obj.has("expenditure_plan")) obj.getJSONArray("expenditure_plan") else JSONArray()
            req_name.setText(name)
            description.setText(description_val)
            total_amount.setText(amount)
            TourPlans = tour_plan
            ExpenditurePlans = expenditure_plan

            Glide.with(activity).load(image).error(R.mipmap.placeholders).into(imageView9)
            Glide.with(activity).load(image1).error(R.mipmap.placeholders).into(imageView10)
        }

        touradp = TourPlanAdapter(activity, TourPlans, activity)
        planlist.adapter = touradp

        expenditureadp = ExpenditurePlanAdapter(activity, ExpenditurePlans, activity)
        expanditurelist.adapter = expenditureadp

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

        add_tourplan.setOnClickListener {
            TourPlanPopup()
        }

        add_expanditureplan.setOnClickListener {
            ExpenditurePlanPopup()
        }

        imageView9!!.setOnClickListener {
            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity!!)
            }
        }

        imageView10!!.setOnClickListener {
            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){
                selectImage1()
            }
            else{
                RequestMultiplePermission(activity!!)
            }
        }

        request.setOnClickListener {

            if (/*req_name.text.isNotEmpty()&&description.text.isNotEmpty()&&*/requesttype.selectedItem.toString().isNotEmpty()&&TourPlans.length()!=0&&ExpenditurePlans.length()!=0){
                SendAdvanceRequest().execute()
                //sendAdvanceRequest()
            }else{
                /*if (req_name.text.isEmpty()){
                    req_name.setError("Required Field")
                }
                if (description.text.isEmpty()){
                    description.setError("Required Field")
                }*/2
                if (requesttype.selectedItem==null){
                    Toast.makeText(activity, "Select Request Type", Toast.LENGTH_SHORT).show()
                }
                if (TourPlans.length()==0){
                    Toast.makeText(activity, "Add Tour Plans", Toast.LENGTH_SHORT).show()
                }
                if (ExpenditurePlans.length()==0){
                    Toast.makeText(activity, "Add Expenditure Plans", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }
        }
    }

    fun TourPlanPopup(position: Int? = null, customer: String? = null, customer_nm: String? = null, selectedstate: String? = null, selectedcity: String? = null, daytxt: String? = null, datetxt: String? = null){
        val openwith = AlertDialog.Builder(activity)
        val popUpView = layoutInflater.inflate(R.layout.tourplan_popup, null)
        val close = popUpView.findViewById(R.id.close) as ImageButton
        val cname = popUpView.findViewById(R.id.cname) as TextView
        val date_txt = popUpView.findViewById(R.id.date_txt) as TextView
        val day = popUpView.findViewById(R.id.day) as EditText
        state = popUpView.findViewById(R.id.state) as Spinner
        city = popUpView.findViewById(R.id.city) as Spinner
        val add_button = popUpView.findViewById(R.id.add_button) as Button
        openwith.setView(popUpView)
        val popup = openwith.create()
        //cname.requestFocus()
        popup.setCancelable(true)
        popup.window!!.setSoftInputMode(16)//WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        popup.show()

        close.setOnClickListener{
            popup.dismiss()
        }

        stateadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, states, true, false, false)
        state.adapter=stateadp

        //getStates(selectedstate)


        if (!daytxt.isNullOrEmpty()){
          day.setText(daytxt)
        }
        if (!datetxt.isNullOrEmpty()){
            date_txt.setText(datetxt)
        }
        if (!customer_nm.isNullOrEmpty()){
            cname.setText(customer_nm)
        }

        state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //getCity(states[position].state_name!!.toString(), selectedcity)
                if (customer_nm.isNullOrEmpty()){
                    cname.setText("")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        cname.setOnClickListener {
            //customer selection
            if (city.selectedItem!=null) {
                popup.dismiss()
                val yu = Intent(activity, CustomersListbyCity::class.java)
                yu.putExtra("city", city.selectedItem.toString())
                yu.putExtra("state", state.selectedItem.toString())
                yu.putExtra("daytxt", day.text.toString())
                yu.putExtra("date_txt", date_txt.text.toString())
                startActivityForResult(yu, CustomerSelect)
            }else{
                Toast.makeText(activity, "Select State and City", Toast.LENGTH_SHORT).show()
            }
        }

        date_txt.setOnClickListener {
            val newCalendar = Calendar.getInstance()
            val fromDatePickerDialog = DatePickerDialog(activity!!, /*R.style.datepicker,*/ DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                val date2 = dateFormatter.format(newDate.time)
                //val age = getAge(year,monthOfYear,dayOfMonth)
                //if (age.toInt()>17&&age.toInt()<=55) {
                date_txt.setText(date2)
                date_txt.error = null
                //}else{
                //Toast.makeText(activity!!,"Age Should be above 18 to 55",Toast.LENGTH_LONG).show()
                //}
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
            fromDatePickerDialog!!.show()
        }

        add_button.setOnClickListener {
            if(customer.isNullOrEmpty()) {
                cname.setError("Required Field")
                return@setOnClickListener
            }
            if(date_txt.text.isNullOrEmpty()) {
                date_txt.setError("Required Field")
                return@setOnClickListener
            }
            if(day.text.isNullOrEmpty()) {
                day.setError("Required Field")
                return@setOnClickListener
            }
            if(state.selectedItem==null) {
                return@setOnClickListener
            }
            if(city.selectedItem==null) {
                return@setOnClickListener
            }
            if(city.selectedItem.toString().isEmpty()) {
                Toast.makeText(activity, "Select City", Toast.LENGTH_SHORT).show()
                //city.setError("Required Field")
                return@setOnClickListener
            }
            if(!customer.isNullOrEmpty()&&
                    date_txt.text.isNotEmpty()&&
                    day.text.isNotEmpty()&&
                    city.selectedItem.toString().isNotEmpty()) {
                val obj = JSONObject()
                obj.put("customer_name", cname.text.toString())
                obj.put("customer", customer)
                obj.put("tour_date", date_txt.text.toString())
                obj.put("state", state.selectedItem.toString())
                obj.put("day", day.text.toString())
                obj.put("tour_city", city.selectedItem.toString())
                if (position != null) TourPlans.put(position, obj) else TourPlans.put(obj)
                Toast.makeText(activity, "Tour Plan Added", Toast.LENGTH_SHORT).show()
                touradp = TourPlanAdapter(activity, TourPlans, activity)
                planlist.adapter = touradp
                popup.dismiss()
            }else{
                return@setOnClickListener
            }
        }

    }


    fun delete_tour(pos:Int){
        if (pos != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TourPlans.remove(pos)
        }
        Toast.makeText(activity, "Tour Plan Removed", Toast.LENGTH_SHORT).show()
        touradp = TourPlanAdapter(activity, TourPlans, activity)
        planlist.adapter = touradp
    }

    fun delete_expenditure(pos:Int){

        tot -= ExpenditurePlans.getJSONObject(pos).getString("total").toInt()
        total_amount.setText(tot.toString())
        if (pos != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ExpenditurePlans.remove(pos)
        }
        Toast.makeText(activity, "Expenditure Plan Removed", Toast.LENGTH_SHORT).show()
        expenditureadp = ExpenditurePlanAdapter(activity, ExpenditurePlans, activity)
        expanditurelist.adapter = expenditureadp
    }

    fun ExpenditurePlanPopup(position: Int? = null){
        var total_val=0
        val openwith = AlertDialog.Builder(activity)
        val popUpView = layoutInflater.inflate(R.layout.expenditureplan_popup, null)
        val close = popUpView.findViewById(R.id.close) as ImageButton
        val stay = popUpView.findViewById(R.id.stay) as EditText
        val food = popUpView.findViewById(R.id.food) as EditText
        val travel = popUpView.findViewById(R.id.travel) as EditText
        val others = popUpView.findViewById(R.id.others) as EditText
        val total = popUpView.findViewById(R.id.totaltxt) as TextView
        val date_txt = popUpView.findViewById(R.id.date_txt) as TextView
        state = popUpView.findViewById(R.id.state) as Spinner
        city = popUpView.findViewById(R.id.city) as Spinner
        val add_button = popUpView.findViewById(R.id.add_button) as Button
        openwith.setView(popUpView)
        val popup = openwith.create()
        //cname.requestFocus()
        popup.setCancelable(true)
        popup.window!!.setSoftInputMode(16)//WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        popup.show()
        close.setOnClickListener{
            popup.dismiss()
        }

        stateadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, states, true, false, false)
        state.adapter=stateadp
        var selectedstate : String? = null
        var selectedcity : String? = null
        if(position!=null){
            val objs = ExpenditurePlans.getJSONObject(position)
            val expnediture_date_val = objs.get("expnediture_date").toString()
            val expnediture_state_val = objs.get("expnediture_state").toString()
            val expnediture_city_val = objs.get("expnediture_city").toString()
            val stay_val = objs.get("stay").toString()
            val food_val = objs.get("food").toString()
            val travel_val = objs.get("travel").toString()
            val others_val = objs.get("others").toString()
            val totals_val = objs.get("total").toString()
            date_txt.setText(expnediture_date_val)
            selectedstate = expnediture_state_val
            selectedcity = expnediture_city_val
            stay.setText(stay_val)
            food.setText(food_val)
            travel.setText(travel_val)
            others.setText(others_val)
            total.setText(totals_val)
        }
        //getStates(selectedstate)
        state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //getCity(states[position].state_name!!.toString(), selectedcity)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        date_txt.setOnClickListener {
            val newCalendar = Calendar.getInstance()
            val fromDatePickerDialog = DatePickerDialog(activity!!, /*R.style.datepicker,*/ DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                val date2 = dateFormatter.format(newDate.time)
                //val age = getAge(year,monthOfYear,dayOfMonth)
                //if (age.toInt()>17&&age.toInt()<=55) {
                date_txt.setText(date2)
                date_txt.error = null
                //}else{
                //Toast.makeText(activity!!,"Age Should be above 18 to 55",Toast.LENGTH_LONG).show()
                //}
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
            fromDatePickerDialog!!.show()
        }
        val textwatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stay_val = if (stay.text.isNullOrEmpty()) 0 else Integer.parseInt(stay.text.toString())
                val food_val = if (food.text.isNullOrEmpty()) 0 else Integer.parseInt(food.text.toString())
                val travel_val = if (travel.text.isNullOrEmpty()) 0 else Integer.parseInt(travel.text.toString())
                val others_val = if (others.text.isNullOrEmpty()) 0 else Integer.parseInt(others.text.toString())
                total_val = stay_val+food_val+travel_val+others_val
                total.setText(total_val.toString())
            }
        }

        stay.addTextChangedListener(textwatcher)
        food.addTextChangedListener(textwatcher)
        travel.addTextChangedListener(textwatcher)
        others.addTextChangedListener(textwatcher)

        add_button.setOnClickListener {
            if (stay.text.isNullOrEmpty()){
                stay.setError("Required Field")
                return@setOnClickListener
            }
            if (food.text.isNullOrEmpty()){
                food.setError("Required Field")
                return@setOnClickListener
            }
            if (travel.text.isNullOrEmpty()){
                travel.setError("Required Field")
                return@setOnClickListener
            }

            if (others.text.isNullOrEmpty()){
                others.setError("Required Field")
                return@setOnClickListener
            }
            if (date_txt.text.isNullOrEmpty()){
                date_txt.setError("Required Field")
                return@setOnClickListener
            }
            if (stay.text.isNotEmpty()&&
                food.text.isNotEmpty()&&
                travel.text.isNotEmpty()&&
                others.text.isNotEmpty()&&
                date_txt.text.isNotEmpty()){
                val obj = JSONObject()
                obj.put("expenditure_date", date_txt.text.toString())
                obj.put("expenditure_state", state.selectedItem.toString())
                obj.put("expenditure_city", city.selectedItem.toString())
                obj.put("stay", stay.text.toString())
                obj.put("food", food.text.toString())
                obj.put("travel", travel.text.toString())
                obj.put("others", others.text.toString())
                obj.put("total", total.text.toString())
                if (position != null) ExpenditurePlans.put(position, obj) else ExpenditurePlans.put(obj)
                Toast.makeText(activity, "Expenditure Plan Added", Toast.LENGTH_SHORT).show()
                expenditureadp = ExpenditurePlanAdapter(activity, ExpenditurePlans, activity)
                expanditurelist.adapter = expenditureadp
                tot += total_val
                total_amount.setText(tot.toString())
                popup.dismiss()
            }else{
                return@setOnClickListener
            }
        }
    }

    fun sendAdvanceRequest(){
        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Sending Advance Request...")
        pDialog.show()
        /*{"user":"9791981428","tid":"V00002","request_type":"4"
        "amount":"1000",
        "tour_plan":[{"tour_date":"","day":"","tour_city":"","customer":""}],
        "expenditure_plan":[{"expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""}]}*/
        val user = pref.getString("mobile", "").toString()
        val tid = pref.getString("tid", "").toString()
        val obj = JSONObject()
        obj.put("user", user)
        obj.put("tid", tid)
        obj.put("request_type", requesttypes[requesttype.selectedItemPosition].id.toString())
        obj.put("amount", total_amount.text.toString())
        obj.put("tour_plan", TourPlans.toString())
        obj.put("expenditure_plan", ExpenditurePlans.toString())
        println("sendAdvanceRequest : " + obj)
        val gson = Gson()
        val listType = object : TypeToken<HashMap<String?, String?>?>() {}.type
        val map: HashMap<String, String> = gson.fromJson(obj.toString(), listType)
        println("sendAdvanceRequestmap : " + map)
        val call = ApproveUtils.Get.sendAdvanceRequest(obj)//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
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

    inner class SendAdvanceRequest :
            AsyncTask<String?, String?, String?>() {
        lateinit var pDialog : ProgressDialog
        override fun onPreExecute() {
            pDialog= ProgressDialog(activity)
            pDialog.setCancelable(false)
            pDialog.setMessage("Sending Advance Request...")
            pDialog.show()
        }

        override fun doInBackground(vararg param: String?): String? {
            var result: String? = null
            val con = Connection()
            try {
                val user = pref.getString("mobile", "").toString()
                val tid = pref.getString("tid", "").toString()
                val obj = JSONObject()
                if (data) obj.put("id",editid)
                obj.put("user", user)
                obj.put("tid", tid)
                obj.put("name", req_name.text.toString())
                obj.put("description", description.text.toString())
                obj.put("request_type", requesttypes[requesttype.selectedItemPosition].id.toString())
                obj.put("amount", total_amount.text.toString())
                obj.put("tour_plan", TourPlans)
                obj.put("expenditure_plan", ExpenditurePlans)
                obj.put("image", "")
                obj.put("image1", "")
                if(imagecode.isNotEmpty()) {
                    obj.put("image", "data:image/png;base64" + "," + imagecode)
                }
                if(imagecode1.isNotEmpty()) {
                    obj.put("image1", "data:image/png;base64" + "," + imagecode1)
                }
                /*println("sendAdvanceRequest : " + obj)
                val gson = Gson()
                val listType = object : TypeToken<HashMap<String?, String?>?>() {}.type
                val map: HashMap<String, String> = gson.fromJson(obj.toString(), listType)
                println("sendAdvanceRequestmap : " + map)*/
                val ur = if(data)"request_edit" else "add_request"
                Log.i(
                        "sendAdvanceRequest",
                        Appconstants.Domin+"$ur : " + obj.toString()
                )
                result = con.sendHttpPostjson(Appconstands.Domin+ur, obj).toString()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {
            Log.e("AdvanceRequest resp", resp!!)
            /*previewbody.setVisibility(View.VISIBLE)
            proceed.setVisibility(View.VISIBLE)
            progress_lay.setVisibility(View.GONE)*/
            pDialog.dismiss()
            try {
                val jobj = JSONObject(resp)
                val msg = jobj.getString("message")
                Toast.makeText(
                        activity,
                        msg,
                        Toast.LENGTH_LONG
                ).show()
                if (jobj.getString("status") == "Success") {
                    finish()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CustomerSelect){
            if (resultCode==Activity.RESULT_OK){
                if (data!=null) {
                    val city = data.getStringExtra("city")
                    val state = data.getStringExtra("state")
                    val daytxt = data.getStringExtra("daytxt")
                    val date_txt = data.getStringExtra("date_txt")
                    val customer = data.getStringExtra("customer")
                    val customer_name = data.getStringExtra("customer_name")

                    TourPlanPopup(customer_nm = customer_name, customer = customer, selectedcity = city, selectedstate = state, daytxt = daytxt, datetxt = date_txt)
                }
            }else if (resultCode==Activity.RESULT_CANCELED){
                if (data!=null) {
                    val city = data.getStringExtra("city")
                    val state = data.getStringExtra("state")
                    val daytxt = data.getStringExtra("daytxt")
                    val date_txt = data.getStringExtra("date_txt")
                    TourPlanPopup(selectedcity = city, selectedstate = state, daytxt = daytxt, datetxt = date_txt)
                }
            }
        }
        else if (requestCode == 1) {
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

    fun getCloudinary(){
        if (Appconstants.net_status(this)) {
            /*val call = ApproveUtils.Get.getcloudinary()
            call.enqueue(object : Callback<Resp_otps> {
                override fun onResponse(call: Call<Resp_otps>, response: Response<Resp_otps>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otps
                        println(example)
                        if (example.status == "Success") {
                            val otpval=example!!.response
                            api_key=otpval!!.api_key.toString()
                            api_secret=otpval!!.api_secret.toString()
                            cloud_name=otpval!!.cloud_name.toString()
                        } else {
                            Toast.makeText(activity, example.status, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<Resp_otps>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                                activity,
                                "Poor network connection",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })*/
        }
        else{
            Toast.makeText(
                    activity,
                    "You're offline",
                    Toast.LENGTH_LONG
            ).show()

        }
    }

    public fun selectImage() {
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

    public fun selectImage1() {
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

    fun getRequesttypes(){
        val call = ApproveUtils.Get.getreqtype()
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("requesttypes", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "Success") {
                        requesttypes = example.getResponse() as ArrayList<Response_trip>
                        requestypadp = StateCityAdapter(activity, R.layout.spinner_list_harvest, requesttypes, false, false, true)
                        requesttype.adapter = requestypadp
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

    override fun OnItemTourPlanClick(view: View, position: Int, viewType: Int) {
        val obj = TourPlans.getJSONObject(position)
        //Tour Selection
        val customer = if (obj.has("customer")) obj.get("customer").toString() else ""
        val customer_nm = if (obj.has("customer_name")) obj.get("customer_name").toString() else ""
        val selectedstate = if (obj.has("state")) obj.get("state").toString() else ""
        val selectedcity = if (obj.has("tour_city")) obj.get("tour_city").toString() else ""
        val daytxt = if (obj.has("day")) obj.get("day").toString() else ""
        val datetxt = if (obj.has("tour_date")) obj.get("tour_date").toString() else ""
        TourPlanPopup(position, customer, customer_nm, selectedstate, selectedcity, daytxt, datetxt)
    }

    override fun OnItemExpenditurePlanClick(view: View, position: Int, viewType: Int) {
        //Expenditure Selection
        ExpenditurePlanPopup(position)
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