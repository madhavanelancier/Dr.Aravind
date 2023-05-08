package com.elancier.meenatchi_CRM

import android.app.Dialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.Adapers.MyPlant_HarvAdap
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp
import kotlinx.android.synthetic.main.activity_family__list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Plant_report : AppCompatActivity() {
    lateinit var pDialog: Dialog
    var posid=""
    var CentresArrays = java.util.ArrayList<CentresData>()

    lateinit var adp : MyPlant_HarvAdap
    lateinit var click : MyPlant_HarvAdap.OnItemClickListener
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_report)
        pref = this@Plant_report.getSharedPreferences("MyPref", 0)

        shimmer_view_container.visibility= View.VISIBLE

        shimmer_view_container.startShimmer()


        posid= intent.extras!!.getString("id")!!.toString()

        //toolbar.title = "Application Form"
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        //}
        val ab=supportActionBar
        ab!!.setTitle("Plant Harvest");
        ab!!.title="Plant Harvest"
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        click = object : MyPlant_HarvAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }
        list_item.setLayoutManager(LinearLayoutManager(this) as RecyclerView.LayoutManager?);
        adp = MyPlant_HarvAdap(CentresArrays, this@Plant_report, click)
        list_item.adapter = adp

        families()

    }
    fun families(){
        if (Appconstants.net_status(this)) {

            CentresArrays.clear()

            val call = ApproveUtils.Get.getMem2(Appconstants.Domin+"view_plantreport/"+posid)
            call.enqueue(object : Callback<Resp> {
                override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp
                        println(example)
                        if (example.getStatus() == "Success") {
                            textView23.visibility=View.GONE
                            var arr=example.getMessage()
                            var otpval= arr!!.data!![0].plant_harvest
                            //textView33.setText(arr!!.data?.get(0)?.family.toString())
                            //textView36.setText(arr!!.data?.get(0)?.aquaponics.toString())

                            for(i in 0 until otpval!!.size) {
                                val data= CentresData()
                                data.CName = otpval[i].plant_type
                                data.Cid = otpval[i].id
                                data.CMembers = "Qty - "+otpval[i].harvest_qty+" KG"
                                data.CLocation = "Seeded Date - "+otpval[i].seeded_date

                                data.CStatus = "Harvest Date - "+otpval[i].harvest_date
                                //db.CentreInsert(data)
                                CentresArrays.add(data)

                            }
                            adp = MyPlant_HarvAdap(
                                CentresArrays,
                                this@Plant_report,
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
                            Toast.makeText(this@Plant_report, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()
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

                override fun onFailure(call: Call<Resp>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Plant_report,
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
                this@Plant_report,
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
}
