package com.elancier.meenatchi_CRM

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elancier.meenatchi_CRM.Adapers.PatientAdap
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_branch__list.*
import kotlinx.android.synthetic.main.activity_eod_list.*
import kotlinx.android.synthetic.main.activity_eod_list.swiperefresh
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Patient_list : AppCompatActivity(),PatientAdap.OnItemClickListener {
    val activity=this
    lateinit var pDialog : Dialog
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    lateinit var adp : PatientAdap
    lateinit var adpdup : PatientAdap
    var eodlistsdup = java.util.ArrayList<EOD_data>()
    var eodlists = ArrayList<EOD_data>()//JSONArray()
    lateinit var searchView:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Patient List"
        pDialog = Dialog(activity)
        pref = activity.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()


        recyclerview.setLayoutManager(LinearLayoutManager(this));


        adp = PatientAdap(activity,eodlists,activity)
        recyclerview.adapter = adp

        adpdup = PatientAdap(activity,eodlistsdup,activity)
        recyclerview.adapter = adpdup

        add.setOnClickListener {
            startActivity(Intent(this,Patient_Add::class.java))
        }

        swiperefresh.setOnRefreshListener {
            getEODs("")
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
        }

        /* add.setOnClickListener {
            startActivity(
                Intent(activity, Schedule_Meeting::class.java))
        }*/

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
            val call = ApproveUtils.Get.getPatients(pref!!.getString("empid","")!!)
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
                                patient.cname = if (res.wife_name.isNullOrEmpty()) "" else res.wife_name!!
                                patient.mmob = if (res.mrd_number.isNullOrEmpty()) "" else res.mrd_number!!
                                patient.doctor = if (res.doctor_name.isNullOrEmpty()) "" else res.doctor_name!!
                                patient.sval = if (res.hospital_name.isNullOrEmpty()) "" else res.hospital_name!!
                                patient.city = if (res.city_name.isNullOrEmpty()) "" else res.city_name!!
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
        super.onBackPressed()
        finish()
        /*if(callin.visibility==View.VISIBLE){
            callin.visibility=View.GONE
            search_date.text.clear()
            todate.text.clear()
            getEODs("")
        }
        else{
            finish()
        }*/
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
                                eodlists[i].doctor!!.toString().toLowerCase().contains(newText!!.toLowerCase())||
                                eodlists[i].city!!.toString().toLowerCase().contains(newText!!.toLowerCase())) {
                                val data= EOD_data()
                                data.cname = eodlists[i].cname
                                data.customerID = eodlists[i].customerID.toString()
                                data.mmob = eodlists[i].mmob.toString()
                                data.doctor = eodlists[i].doctor.toString()
                                data.sval = eodlists[i].sval.toString()
                                data.city = eodlists[i].city.toString()

                                eodlistsdup.add(data)


                            }
                            adpdup = PatientAdap(
                                this@Patient_list,eodlistsdup, activity)
                            recyclerview.adapter = adpdup
                            // memberslist.adapter = adp

                            if(eodlistsdup.isEmpty()){
                                textView23.visibility=View.VISIBLE

                            }
                            else{
                                textView23.visibility=View.GONE

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
                        adp = PatientAdap(
                            this@Patient_list,
                            eodlists,
                            activity!!
                        )
                         recyclerview.adapter = adp
                        // memberslist.adapter = adp

                        if (eodlists.isEmpty()) {
                            textView23.visibility = View.VISIBLE

                        }
                        else{
                            textView23.visibility=View.GONE

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
                            getEODs("")
                        }

                    }, mYear, mMonth, mDay


            )

            val simpledateformate = SimpleDateFormat("dd-MM-yyyy");
            // val date = simpledateformate.parse(datebilling.text.toString());
            //datePickerDialog.getDatePicker().setMinDate(cals.timeInMillis);

            datePickerDialog.show()

        }

    }

    fun adp(){
        adp = PatientAdap(activity,eodlists,activity)
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
        var doctor: String? = null
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

}