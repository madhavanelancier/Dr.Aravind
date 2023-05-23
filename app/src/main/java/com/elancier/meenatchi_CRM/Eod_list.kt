package com.elancier.meenatchi_CRM

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elancier.meenatchi_CRM.Adapers.EODAdap
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Connection
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import kotlinx.android.synthetic.main.activity_eod_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Eod_list : AppCompatActivity(),EODAdap.OnItemClickListener,DatePickerDialog.OnDateSetListener  {
    val activity=this
    lateinit var pDialog : Dialog
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    lateinit var adp : EODAdap
    var eodlists = ArrayList<EOD_data>()//JSONArray()
    lateinit var adpdup : EODAdap
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

    private var fromDate: String? = null
    private var fromDatePicker: DatePickerDialog? = null
    private var fromDatePickerListener: OnDateSetListener? = null
    private var fromDateCalendar: Calendar? = null
    private var toDate: String? = null
    private var toDatePicker: DatePickerDialog? = null
    private var toDatePickerListener: OnDateSetListener? = null
    private var toDateCalendar: Calendar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eod_list)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Meetings"
        pDialog = Dialog(activity)
        pref = activity.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()


        recyclerview.setLayoutManager(LinearLayoutManager(this));

        adp = EODAdap(activity,eodlists,activity)
        recyclerview.adapter = adp

        adpdup = EODAdap(activity,eodlistsdup,activity)
        recyclerview.adapter = adpdup


        fromDateCalendar = Calendar.getInstance()
        fromDatePickerListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                fromDateCalendar!!.set(Calendar.YEAR, year)
                fromDateCalendar!!.set(Calendar.MONTH, monthOfYear)
                fromDateCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                fromDate =
                    fromDateCalendar!!.get(Calendar.YEAR).toString() + "-" + (fromDateCalendar!!.get(
                        Calendar.MONTH
                    ) + 1) + "-" + fromDateCalendar!!.get(Calendar.DAY_OF_MONTH)
                search_date.setText(fromDate);
                toDatePicker!!.datePicker.minDate = fromDateCalendar!!.getTimeInMillis()
            }
        fromDatePicker = DatePickerDialog(
            this,
            fromDatePickerListener,
            fromDateCalendar!!.get(Calendar.YEAR),
            fromDateCalendar!!.get(Calendar.MONTH),
            fromDateCalendar!!.get(Calendar.DAY_OF_MONTH)
        )


        toDateCalendar = Calendar.getInstance()
        toDatePickerListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                toDateCalendar!!.set(Calendar.YEAR, year)
                toDateCalendar!!.set(Calendar.MONTH, monthOfYear)
                toDateCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                toDate = toDateCalendar!!.get(Calendar.YEAR).toString() + "-" + (toDateCalendar!!.get(
                    Calendar.MONTH
                ) + 1) + "-" + toDateCalendar!!.get(Calendar.DAY_OF_MONTH)
                todate.setText(toDate);
            }

        toDatePicker = DatePickerDialog(
            this,
            toDatePickerListener,
            toDateCalendar!!.get(Calendar.YEAR),
            toDateCalendar!!.get(Calendar.MONTH),
            toDateCalendar!!.get(Calendar.DAY_OF_MONTH)
        )

        add.setOnClickListener {
            startActivity(
                Intent(activity, Schedule_Meeting::class.java)
                    .putExtra("from","Add")
            )
        }

        search_date.setOnClickListener {
            filter="from"



            fromDatePicker!!.show();
        }

        todate.setOnClickListener {
            filter="to"

            toDatePicker!!.show();
        }


        textView34.setOnClickListener {
            search_date.setText("")
            todate.setText("")
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

    override fun onResume() {
        super.onResume()
        eodlistsdup.clear()
        getEODs("")
    }

    fun getEODs(search: String){
        eodlists.clear()
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
            if(search_date.text.toString().isNotEmpty()){
                from=search_date.text.toString()
            }
            else{
                from="0"
            }
            if(todate.text.toString().isNotEmpty()){
                to=todate.text.toString()
            }
            else{
                to="0"
            }

            val call = ApproveUtils.Get.getMeetings(pref!!.getString("empid","")!!,from,to)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println("Resp "+example)
                        if (example.getStatus() == "Success") {
                            //eodlist = JSONArray()

                            for (r in 0 until example.getResponse()!!.size) {

                                val res = example.getResponse()!!.get(r)
                                var patient=EOD_data()
                                patient.customerID =  res.id!!
                                patient.cname = res.doctor_name
                                patient.mmob = res.meeting_datetime
                                patient.tval = res.hospital_name
                                patient.sval = res.status
                                eodlists.add(patient)
                                //eodlist.put(obj)
                                adp()
                            }
                        } else {
                            Toast.makeText(activity, example.getMessage(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                    }
                    adp()
                    pDialog.dismiss()
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
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
        if(search_date.text.isNotEmpty()||todate.text.toString().isNotEmpty()){
            search_date.setText("")
            todate.setText("")
            getEODs("")
        }
        else{
            finish()

        }
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
                                eodlists[i].tval!!.toString().toLowerCase().contains(newText!!.toLowerCase())||
                                eodlists[i].sval!!.toString().toLowerCase().contains(newText!!.toLowerCase())) {
                                val data= EOD_data()
                                data.cname = eodlists[i].cname
                                data.customerID = eodlists[i].customerID.toString()
                                data.mmob = eodlists[i].mmob.toString()
                                data.tval = eodlists[i].tval.toString()
                                data.sval = eodlists[i].sval.toString()
                                eodlistsdup.add(data)


                            }
                            adpdup = EODAdap(
                                this@Eod_list,eodlistsdup, activity)
                            recyclerview.adapter = adpdup
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
                    adp = EODAdap(
                        this@Eod_list,
                        eodlists,
                        activity!!
                    )
                    recyclerview.adapter = adp
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
        adp = EODAdap(activity,eodlists,activity)
        recyclerview.adapter = adp

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
            getEODs("")
        }
        else{
            myYear1 = p1
            myday1 = p3
            myMonth1 = p2
            todate.setText(
                myday1.toString() + "-" + (myMonth1+1).toString() + "-" +
                        myYear1.toString()
            );
            getEODs("")

        }

    }
    }

