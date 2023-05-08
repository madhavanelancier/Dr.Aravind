package com.elancier.meenatchi_CRM

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Search_model
import kotlinx.android.synthetic.main.activity_search_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search_Activity : AppCompatActivity() {
    lateinit var sidelogo: ImageView
    lateinit var mainlogos: ImageView
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    var religion=""
    internal lateinit var progbar: Dialog


    var logid=""
    var balance=""

    var searcharr=ArrayList<CentresData>()

    lateinit var catlists: RecyclerView
    var searcharrays = java.util.ArrayList<CentresData>()
    lateinit var adp : MySearchAdap
    lateinit var click : MySearchAdap.OnItemClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_)

        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()


        logid=pref!!.getString("logid","").toString()
        balance=pref!!.getString("balance","").toString()


        if((logid.isEmpty()&&balance.isEmpty())||balance=="0"){
            amount.visibility=View.VISIBLE
        }
        else{

            amount.visibility=View.GONE

        }

        editor!!.putString("item1","on")
        editor!!.commit()

        val intent=intent.extras
        //searcharr=intent!!.getStringArrayList("array") as ArrayList<CentresData>

        sidelogo=findViewById(R.id.imageView4) as ImageView
        mainlogos=findViewById(R.id.imageView11) as ImageView
        catlists=findViewById(R.id.searchlist) as RecyclerView

        var sitelogo=pref!!.getString("sidelogo","").toString()
        var mainlogo=pref!!.getString("mainlogo","").toString()

        //Log.e("sitelogo",sitelogo)
        //Glide.with(this).load(sitelogo).placeholder(R.mipmap.logo_imgs).into(sidelogo)
        //Glide.with(this).load(mainlogo).placeholder(R.mipmap.select_logo).into(mainlogos)



        var religion=intent!!.getString("religion").toString()
        var genderval=intent!!.getString("genderval").toString()
        var age1=intent!!.getString("age1").toString()
        var age2=intent!!.getString("age2").toString()
        var caste=intent!!.getString("caste").toString()
        var marry=intent!!.getString("marry").toString()
        var edu=intent!!.getString("edu").toString()
        var start=intent!!.getString("start").toString()
        var end=intent!!.getString("end").toString()

        if(age1=="-SELECT-"){
            age1=""
        }
        if(age2=="-SELECT-"){
            age2=""
        }
        if(edu=="-SELECT-"){
            edu=""
        }
        if(caste=="0"){
            caste=""
        }




        click = object : MySearchAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }

        val mLayoutManager2 = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false);
        catlists.setLayoutManager(mLayoutManager2);

        adp = MySearchAdap(searcharr, this@Search_Activity, click)
        catlists.adapter = adp

        search(religion,genderval,age1,age2,caste,marry,edu,start,end)



    }
    fun search(religion:String,gender:String,agef:String,aget:String,caste:String,marital:String,edu:String,start:String,limit:String){

        progbar = Dialog(this@Search_Activity)
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progbar.setContentView(R.layout.load)
        progbar.setCancelable(false)
        progbar.show()

        if (Appconstants.net_status(this)) {

            searcharr.clear()
            println("params"+religion+","+gender+","+agef+","+aget+","+caste+","+marital+","+edu+","+start+","+limit)

            val call = ApproveUtils.Get.search(religion,gender,agef,aget,caste,marital,edu,start,limit)

            call.enqueue(object: Callback<Search_model> {
                @SuppressLint("ResourceType")
                override fun onResponse(
                    call: Call<Search_model>,
                    response: Response<Search_model>
                ) {
                    Log.e("getLogin responce",response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Search_model
                        //println(example)
                        if (example.status == "Success") {
                            val mem = example.response!!
                            textView29.visibility=View.GONE
                            for(i in 0 until mem.size) {
                                val data = CentresData()

                                data.CMembers=mem[i].birthday;
                                data.CStatus=mem[i].caste.toString();
                                data.time2=mem[i].age;
                                data.CName=mem[i].name;
                                data.time1=mem[i].education;
                                data.CLocation=mem[i].place;
                                data.Cid=mem[i].id;
                                data.CTotal=mem[i].image;

                                searcharr.add(data)

                            }
                            println("searcharrsize"+searcharr.size)

                            if(searcharr.size==0){
                                textView29.visibility=View.VISIBLE
                            }
                            adp = MySearchAdap(searcharr, this@Search_Activity, click)
                            catlists.adapter = adp

                            adp.notifyDataSetChanged()

                            progbar!!.dismiss()




                            //view_pager!!.adapter = adapters



                        }
                        else {
                            Log.e("getLogin else", example.status!!)
                            textView29.visibility=View.VISIBLE
                            progbar!!.dismiss()


                        }
                    }
                }

                override fun onFailure(call: Call<Search_model>, t: Throwable) {
                    Log.e("getLogin Fail response", t.toString())
                    progbar!!.dismiss()
                    textView29.visibility=View.VISIBLE
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Search_Activity!!,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {


                    }

                }
            })
        }
        else{
            Toast.makeText(applicationContext,"You're offline", Toast.LENGTH_SHORT).show()
        }


    }
}
