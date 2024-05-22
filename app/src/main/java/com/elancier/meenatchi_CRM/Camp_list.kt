package com.elancier.meenatchi_CRM

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elancier.meenatchi_CRM.Adapers.CampAdap
import com.elancier.meenatchi_CRM.Adapers.EODAdap
import com.elancier.meenatchi_CRM.Adapers.StateCityAdapter
import com.elancier.meenatchi_CRM.Adapers.TourPlanAdapter
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Connection
import com.elancier.meenatchi_CRM.retrofit.Resp_otp
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_advance_request.planlist
import kotlinx.android.synthetic.main.activity_camp_list.camplist
import kotlinx.android.synthetic.main.activity_eod_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Camp_list : AppCompatActivity(),CampAdap.OnItemClickListener,DatePickerDialog.OnDateSetListener  {
    val activity=this
    lateinit var pDialog : Dialog
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    lateinit var adp : CampAdap
    var eodlists = ArrayList<EOD_data>()//JSONArray()
    lateinit var adpdup : CampAdap
    var eodlistsdup = java.util.ArrayList<EOD_data>()
    lateinit var searchView:SearchView
    var day = 0
    var month = 0
    var year= 0
    var myHour = 0
    var myMinute= 0
    var myYear = 0
    var myday = 0
    var myMonth = 0

    var myYear1 = 0
    var myday1 = 0
    var myMonth1 = 0
    var hour= 0
    var minute= 0
    var filter=""

    lateinit var locationDropdown:Spinner
    lateinit var camploc:EditText
    lateinit var date:TextView
    lateinit var locerror:TextView
    lateinit var crmattribute:TextView

    lateinit var  openwithDialog:AlertDialog.Builder
    lateinit var  popup:Dialog

    private var fromDate: String? = null
    private var fromDateDup: String? = null
    private var fromDatePicker: DatePickerDialog? = null
    private var fromDatePickerListener: OnDateSetListener? = null
    private var fromDateCalendar: Calendar? = null
    private var toDate: String? = null
    private var toDateDup: String? = null
    private var toDatePicker: DatePickerDialog? = null
    private var toDatePickerListener: OnDateSetListener? = null
    private var toDateCalendar: Calendar? = null
    var loclist=ArrayList<String>()
    var loclistIds=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_list)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Camp"
        pDialog = Dialog(activity)
        pref = activity.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()


        camplist.setLayoutManager(LinearLayoutManager(this));

        adp = CampAdap(activity,eodlists,activity)
        camplist.adapter = adp

        adpdup = CampAdap(activity,eodlistsdup,activity)
        camplist.adapter = adpdup
        nodata.setText("No Camps")


        add.setOnClickListener {
            addpopup()
           /* startActivity(
                Intent(activity, Schedule_Meeting::class.java)
                    .putExtra("from","Add")
            )*/
        }

        search_date.setOnClickListener {
            filter="from"
            fromDatePicker!!.show();
        }

        todate.setOnClickListener {
            if(search_date.text.isNotEmpty()) {
                filter = "to"
                toDatePicker!!.show();
            }
            else{
                search_date.setError("Please select from date")
                val toast=Toast.makeText(applicationContext,"Please select from date" , Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER,0,0)
                toast.show()
            }
        }


        textView34.setOnClickListener {
            search_date.setText("")
            todate.setText("")
            onResume()
        }



        swiperefresh.setOnRefreshListener {
            search_date.setText("")
            todate.setText("")
            onResume()
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
        }

       /* srchtxt.setOnKeyListener { view, i, keyEvent ->
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                if(srchtxt.text.toString().length>=2) {
                    getEODs(srchtxt.text.toString())
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
            return@setOnKeyListener false
        }
        imageButton.setOnClickListener {
            srchtxt.setText("")
            getEODs("")
        }
        */


    }

    fun addpopup() {

        var cal=Calendar.getInstance()
        var format=SimpleDateFormat("yyyy-MM-dd")
        var curdate=cal.time
        var currentdt=format.format(curdate)

         openwithDialog = AlertDialog.Builder(activity)
        val popUpView = layoutInflater.inflate(R.layout.addcamp_popup, null)

        locationDropdown=popUpView.findViewById(R.id.state);
        camploc=popUpView.findViewById(R.id.stay);
        date=popUpView.findViewById(R.id.date_txt);
        crmattribute=popUpView.findViewById(R.id.food);
        locerror=popUpView.findViewById(R.id.locerr)
        date.setText(currentdt)

        var spinadap=ArrayAdapter(activity,R.layout.support_simple_spinner_dropdown_item,loclist)
        locationDropdown.adapter=spinadap

        val submit = popUpView.findViewById(R.id.add_button) as Button
        val close = popUpView.findViewById(R.id.close) as ImageButton
        openwithDialog.setView(popUpView)
         popup = openwithDialog.create()
        //cname.requestFocus()
        popup.setCancelable(true)
        popup.window!!.setSoftInputMode(16)//WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        popup.show()

        close.setOnClickListener {
            popup.dismiss()
        }


        submit.setOnClickListener {
            if(locationDropdown.selectedItemPosition!=0&&camploc.text.isNotEmpty()&&date.text.isNotEmpty()){
                saveCamp()
            }
            else{
                if(locationDropdown.selectedItemPosition==0){
                    locerror.visibility=View.VISIBLE
                }
                if(camploc.text.isEmpty()){
                    camploc.setError("Required field*")
                }
                if(date.text.isEmpty()){
                    date.setError("Required field*")
                }

            }
        }




        //getStates(selectedstate)




        locationDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position!=0){
                    locerror.visibility=View.GONE
                }
                //getCity(states[position].state_name!!.toString(), selectedcity)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        date.setOnClickListener {
            val newCalendar = Calendar.getInstance()
            val fromDatePickerDialog = DatePickerDialog(
                activity!!, /*R.style.datepicker,*/
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val date2 = dateFormatter.format(newDate.time)
                    //val age = getAge(year,monthOfYear,dayOfMonth)
                    //if (age.toInt()>17&&age.toInt()<=55) {
                    date.setText(date2)
                    date.error = null
                    //}else{
                    //Toast.makeText(activity!!,"Age Should be above 18 to 55",Toast.LENGTH_LONG).show()
                    //}
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )
            fromDatePickerDialog!!.show()
        }


    }

    override fun onResume() {
        super.onResume()
        eodlistsdup.clear()
       // getEODs("")

        getLocations()
    }

    fun saveCamp(){
        if (Appconstants.net_status(this)) {
            //getEOD().execute()
            /*  val objs = JsonObject()
              objs.addProperty("trip",pref!!.getString("tid","")!!)//"9791981428"
              objs.addProperty("status",search)
              objs.addProperty("from",search_date.text.toString().trim())
              objs.addProperty("to",todate.text.toString().trim())
              println("obj : "+objs)*/
            pDialog =Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            var from=""
            var to=""


            val call = ApproveUtils.Get.campstore(pref!!.getString("empid", "").toString(),loclistIds[locationDropdown.selectedItemPosition],camploc.text.toString(),date.text.toString())
            call.enqueue(object : Callback<Resp_otp> {
                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                    Log.e("responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otp
                        println("Resp "+example)
                        if (example.status == "Success") {
                            Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                            pDialog.dismiss()
                            popup.dismiss()
                            CampList()

                        } else {
                            Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    else{
                    }
                }

                override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    adp()
                    pDialog.dismiss()
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            swiperefresh.isRefreshing=false
            Toast.makeText(
                activity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun CampList(){
        eodlists.clear()
        eodlistsdup.clear()
        if (Appconstants.net_status(this)) {
            //getEOD().execute()
            /*  val objs = JsonObject()
              objs.addProperty("trip",pref!!.getString("tid","")!!)//"9791981428"
              objs.addProperty("status",search)
              objs.addProperty("from",search_date.text.toString().trim())
              objs.addProperty("to",todate.text.toString().trim())
              println("obj : "+objs)*/
            pDialog =Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            var from=""
            var to=""

            var json=JsonObject()
            json.addProperty("user_id",pref!!.getString("empid", "").toString())
            val call = ApproveUtils.Get.getCamplist(json);
            call.enqueue(object : Callback<Resp_otp> {
                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                    Log.e("responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otp
                        println("Resp "+example)
                        if (example.status == "Success") {

                            for (r in 0 until example.response!!.size) {

                                val res = example.response!!.get(r)
                                var patient=EOD_data()

                                for(i in 0 until loclistIds.size){
                                    try {
                                        if (res.locations == loclistIds[i]) {
                                            patient.cname = loclist[i]
                                        }
                                    }
                                    catch (e:Exception){

                                    }
                                }
                                patient.mmob = res.date
                                patient.tval = res.camp_location
                                patient.sval = res.crm_att
                                eodlists.add(patient)
                                //eodlist.put(obj)
                                //eodlist.put(obj)
                            }
                            adp()
                            swiperefresh.isRefreshing=false

                        } else {
                            Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                        }
                        pDialog.dismiss()

                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    adp()
                    pDialog.dismiss()
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            swiperefresh.isRefreshing=false
            Toast.makeText(
                activity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getLocations(){
        loclist.clear()
        loclistIds.clear()
        loclist.add("Select Location")
        loclistIds.add("0")
        if (Appconstants.net_status(this)) {
            //getEOD().execute()
            /*  val objs = JsonObject()
              objs.addProperty("trip",pref!!.getString("tid","")!!)//"9791981428"
              objs.addProperty("status",search)
              objs.addProperty("from",search_date.text.toString().trim())
              objs.addProperty("to",todate.text.toString().trim())
              println("obj : "+objs)*/
            pDialog =Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            var from=""
            var to=""


            val call = ApproveUtils.Get.getpreferLocation(Appconstants.Domin+"prefer_location")
            call.enqueue(object : Callback<Resp_otp> {
                override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                    Log.e("responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otp
                        println("Resp "+example)
                        if (example.status == "Success") {
                            //eodlist = JSONArray()

                            for (r in 0 until example.response!!.size) {

                                val res = example.response!!.get(r)
                                loclist.add(res.name.toString())
                                loclistIds.add(res.id.toString())
                                //eodlist.put(obj)
                            }

                        } else {
                            Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                        }


                        pDialog.dismiss()

                        CampList()
                    }
                    else{
                    }
                }

                override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    adp()
                    pDialog.dismiss()
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            swiperefresh.isRefreshing=false
            Toast.makeText(
                activity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onBackPressed() {

            finish()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //adapter.getFilter().filter(newText)
                eodlistsdup.clear()
                if(newText!!.length>=3) {

                    if (eodlists.isNotEmpty()) {

                        for (i in 0 until eodlists.size) {
                            Log.e("pos",eodlists[i].cname.toString())
                            if (eodlists[i].cname!!.toString().toLowerCase().contains(newText!!.toLowerCase())||
                                eodlists[i].mmob!!.toString().toLowerCase().contains(newText!!.toLowerCase())||
                                eodlists[i].tval!!.toString().toLowerCase().contains(newText!!.toLowerCase())) {
                                val data= EOD_data()
                                data.cname = eodlists[i].cname
                                data.mmob = eodlists[i].mmob.toString()
                                data.tval = eodlists[i].tval.toString()
                                data.sval = eodlists[i].sval.toString()
                                eodlistsdup.add(data)


                            }
                            adpdup = CampAdap(
                                this@Camp_list,eodlistsdup, activity)
                            camplist.adapter = adpdup
                            // memberslist.adapter = adp

                            if(eodlistsdup.isEmpty()){
                                nodata.visibility=View.VISIBLE

                            }
                            else{
                                nodata.visibility=View.GONE

                            }

                            /*adp1dup = MyOrderAdap(
                                CentresArraysdup1,
                                this@Customers_List,
                                click1
                            )
                            memberslist.adapter = adp1dup
                            // memberslist.adapter = adp
                            shimmer_view_container.stopShimmer()
                            shimmer_view_container.visibility=View.GONE

                            if(CentresArraysdup1.isEmpty()){
                                textView23.visibility=View.VISIBLE

                            }
                            else{
                                textView23.visibility=View.GONE

                            }*/
                            //}
                        }
                    }
                }
                else if(newText.length==0){
                    eodlistsdup.clear()
                    adp = CampAdap(
                        this@Camp_list,
                        eodlists,
                        activity!!
                    )
                    camplist.adapter = adp
                    // memberslist.adapter = adp

                    if (eodlists.isEmpty()) {
                        nodata.visibility = View.VISIBLE

                    }
                    else{
                        nodata.visibility=View.GONE

                    }

                }

                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    inner class getEOD : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            pDialog = Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
           // eodlist = JSONArray()
            val con = Connection()
            val obj = JSONObject()
            obj.put("user",pref!!.getString("tid",""))
            try {
                Log.i("eod list", Appconstants.eodList + "  save inputtt      " + obj.toString())
                result = con.sendHttpPostjson(Appconstants.eodList, obj).toString()

            } catch (e: IOException) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialog.dismiss()
            try {
                println("resp : " + resp)
                val example = JSONObject(resp!!)
                if (example.getString("Status") == "Success") {
                    //eodlist = example.getJSONArray("Response")
                    adp()
                } else {
                    Toast.makeText(activity, example.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){

            }
        }
    }

    override fun OnItemClick(view: View, position: Int, viewType: Int) {
       /* val it = Intent(activity,Add_Eod::class.java)
        it.putExtra("edit",eodlist[position].jsonobject.toString())
        startActivity(it)*/
    }

    fun filter() {
        callin.visibility=View.VISIBLE
        search_date.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val cals = Calendar.getInstance()  // or pick another time zone if necessary
            cals.timeInMillis = System.currentTimeMillis()
            cals.set(Calendar.DAY_OF_MONTH, cals.get(Calendar.DAY_OF_MONTH) + 1)
            cals.set(Calendar.HOUR_OF_DAY, 0)
            cals.set(Calendar.MINUTE, 0)
            cals.set(Calendar.SECOND, 0)

            val datePickerDialog = DatePickerDialog(activity,

                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        search_date.setText(
                                dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                        )
                    }, mYear, mMonth, mDay
            )

            val simpledateformate = SimpleDateFormat("dd-MM-yyyy");
            // val date = simpledateformate.parse(datebilling.text.toString());
            //datePickerDialog.getDatePicker().setMinDate(cals.timeInMillis);

            datePickerDialog.show()

        }

        todate.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val cals = Calendar.getInstance()  // or pick another time zone if necessary
            cals.timeInMillis = System.currentTimeMillis()
            cals.set(Calendar.DAY_OF_MONTH, cals.get(Calendar.DAY_OF_MONTH) + 1)
            cals.set(Calendar.HOUR_OF_DAY, 0)
            cals.set(Calendar.MINUTE, 0)
            cals.set(Calendar.SECOND, 0)

            val datePickerDialog = DatePickerDialog(activity,

                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        todate.setText(
                                dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                        )
                        if(search_date.text.toString().isEmpty()){
                            Toast.makeText(applicationContext, "Please select from date", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            //getEODs("")
                        }

                    }, mYear, mMonth, mDay


            )

            val simpledateformate = SimpleDateFormat("dd-MM-yyyy");
            // val date = simpledateformate.parse(datebilling.text.toString());
            //datePickerDialog.getDatePicker().setMinDate(cals.timeInMillis);

            datePickerDialog.show()

        }

    }

    fun filters(){
        if(search_date.text.toString().isNotEmpty()&&todate.text.toString().isNotEmpty()){

        }
    }

    fun adp(){
        adp = CampAdap(activity,eodlists,activity)
        camplist.adapter = adp

        if(pDialog.isShowing){
            pDialog.dismiss()
        }
        if (eodlists.size==0){
            nodata.visibility=View.VISIBLE
        }else{
            nodata.visibility=View.GONE
        }
    }

    inner class EOD_data(){
        var customerID: Any? = null
        var cname: String? = null
        var mmob: String? = null
        var maddress: String? = null
        var city: String? = null
        var email: String? = null
        var state: String? = null
        var tval: String? = null
        var sval: String? = null
        var intime: String? = null
        var outtime: String? = null
        var magcapacity: String? = null
        var ctype: String? = null
        var cpro: String? = null
        var scomm: String? = null
        var plike: String? = null
        var pcomnt: String? = null
        var exfeed: String? = null
        var roomloc: String? = null
        var spur: String? = null
        var createdAt: String? = null
        var jsonobject : JSONObject? = null
    }



    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        if(filter=="from") {
            myYear = p1
            myday = p3
            myMonth = p2
            search_date.setText(
                myday.toString() + "-" + (myMonth+1).toString() + "-" +
                        myYear.toString()
            );
            //getEODs("")
        }
        else{
            myYear1 = p1
            myday1 = p3
            myMonth1 = p2
            todate.setText(
                myday1.toString() + "-" + (myMonth1+1).toString() + "-" +
                        myYear1.toString()
            );
            //getEODs("")

        }

    }
    }

