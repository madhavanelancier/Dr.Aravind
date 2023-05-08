package com.elancier.meenatchi_CRM

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.Adapers.MyFamilyAdap_report_fish
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.DataClasses.SpinnerPojo
import com.elancier.meenatchi_CRM.Family.Family_Main
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.elancier.vanithamarket.Prolist
import kotlinx.android.synthetic.main.activity_family__list.*
import kotlinx.android.synthetic.main.activity_family__list.list_item
import kotlinx.android.synthetic.main.activity_family__list.shimmer_view_container
import kotlinx.android.synthetic.main.activity_family__list.textView23
import kotlinx.android.synthetic.main.activity_family_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fish_report_list : AppCompatActivity() {
    lateinit var pDialog: Dialog
    var villagearr=ArrayList<SpinnerPojo>()
    var CentresArrays = java.util.ArrayList<CentresData>()
    var ProductNames = java.util.ArrayList<String>()
    lateinit var adp : MyFamilyAdap_report_fish
    lateinit var click : MyFamilyAdap_report_fish.OnItemClickListener
    internal  var pref: SharedPreferences?=null

    internal  var editor: SharedPreferences.Editor?=null

    var posid=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_report)

        pDialog = Dialog(this@Fish_report_list)
        pref = this@Fish_report_list.getSharedPreferences("MyPref", 0)

        shimmer_view_container.visibility= View.VISIBLE

        shimmer_view_container.startShimmer()

        //toolbar.title = "Application Form"
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        //}
        val ab=supportActionBar
        ab!!.setTitle("Stocks");
        ab!!.title="Stocks"
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        searchView2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()) {
                    var CentresArraysSearch = java.util.ArrayList<CentresData>()
                    if (s.length > 1){
                    for (i in 0 until ProductNames.size) {
                        Log.e("names",ProductNames.toString())
                        if (ProductNames[i].contains(s.toString().toLowerCase())) {
                            val data = CentresData()
                            data.CName = CentresArrays[i].CName
                            data.Cid = CentresArrays[i].Cid.toString()
                            data.CMembers = CentresArrays[i].CMembers
                            data.CTotal =  CentresArrays[i].CTotal

                            //db.CentreInsert(data)
                            CentresArraysSearch.add(data)
                        }
                    }
                        adp = MyFamilyAdap_report_fish(CentresArraysSearch, this@Fish_report_list, click)
                        list_item.adapter = adp
                }

                }
                else{
                    adp = MyFamilyAdap_report_fish(CentresArrays, this@Fish_report_list, click)
                    list_item.adapter = adp
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        click = object : MyFamilyAdap_report_fish.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }
        list_item.setLayoutManager(LinearLayoutManager(this) as RecyclerView.LayoutManager?);
        adp = MyFamilyAdap_report_fish(CentresArrays, this@Fish_report_list, click)
        list_item.adapter = adp

       // getVillage()
        families()


    }
   /* fun getVillage(){

        pDialog= Dialog(this)
        Appconstands.loading_show(this@Fish_report_list, pDialog).show()

        //Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
        val call = ApproveUtils.Get.getvillage()
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {

                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.getStatus() == "Success") {
                        var arr=example.getMessage()
                        var otpval= arr!!.data
                        for(i in 0 until otpval!!.size) {
                            val data=SpinnerPojo()
                            data.name = otpval[i].name
                            data.id = otpval[i].id
                            data.code = otpval[i].villageCode
                            data.status = otpval[i].status
                            if(otpval[i].status=="on") {
                                villagearr.add(data)

                            }
                        }
                        spinner2.adapter = SpinAdapter(this@Fish_report_list!!,R.layout.tax_spinner_list_item,villagearr)
                        try{
                            spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    println("position : "+position)
                                    //Groupcodes().execute(spindata[position].id)
                                    posid=villagearr.get(position).id.toString()
                                    println("positionid : "+posid)
                                    families()

                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {

                                }
                            }
                        }
                        catch (e:Exception){

                        }

                    } else {
                        Toast.makeText(
                            this@Fish_report_list,
                            example.getStatus(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                //Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@Fish_report_list,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }*/

    fun families(){
        if (Appconstants.net_status(this)) {

            CentresArrays.clear()
            ProductNames.clear()

            val call = ApproveUtils.Get.product_stocks()
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            textView23.visibility=View.GONE
                            var arr=example.getResponse()
                            //textView33.setText(arr!!.data?.get(0)?.family.toString())
                            //textView36.setText(arr!!.data?.get(0)?.aquaponics.toString())

                            for(i in 0 until arr!!.size) {
                                val data= CentresData()
                                data.CName = arr[i].name
                                data.Cid = arr[i].id.toString()
                                data.CMembers = "Brand - "+arr[i].brand_nm
                                data.CTotal = "Available Stocks - "+arr[i].stock

                                //db.CentreInsert(data)
                                CentresArrays.add(data)
                                ProductNames.add(arr[i].name.toString().toLowerCase())

                            }
                            adp = MyFamilyAdap_report_fish(
                                CentresArrays,
                                this@Fish_report_list,
                                click
                            )
                            list_item.adapter = adp
                            textView23.visibility=View.GONE

                            shimmer_view_container.stopShimmer()

                            shimmer_view_container.visibility=View.GONE


                            if(CentresArrays.isEmpty()){
                                textView23.visibility=View.VISIBLE
                                shimmer_view_container.stopShimmer()

                                shimmer_view_container.visibility=View.GONE


                            }

                        } else {
                            /*Toast.makeText(this@Fish_report_list, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                            shimmer_view_container.visibility=View.GONE
                            textView23.visibility=View.VISIBLE
                            shimmer_view_container.stopShimmer()

                        }
                    }
                    else{
                        textView23.visibility=View.VISIBLE

                        shimmer_view_container.stopShimmer()

                        shimmer_view_container.visibility=View.GONE

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Fish_report_list,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility=View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Fish_report_list,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE

        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
        return true
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


    fun toast(msg:String){
        val toast= Toast.makeText(this@Fish_report_list,msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
}
