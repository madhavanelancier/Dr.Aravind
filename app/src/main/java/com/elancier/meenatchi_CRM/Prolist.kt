package com.elancier.vanithamarket

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.elancier.meenatchi_CRM.Appconstands
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.google.gson.JsonArray
import com.google.gson.JsonObject

import kotlinx.android.synthetic.main.activity_advance_request.*
import kotlinx.android.synthetic.main.activity_branch__list.*
import kotlinx.android.synthetic.main.activity_prolist.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList

class Prolist : AppCompatActivity() {
    var brandid = ArrayList<String>()
    var brandname = ArrayList<String>()
    var spinnerid = ArrayList<String>()
    var spinnername = ArrayList<String>()
    var proidarr = ArrayList<String>()
    var pronamearr = ArrayList<String>()
    var Allproducts = ArrayList<CentresData>()
    var CentresArrays = ArrayList<CentresData>()
    lateinit var adp: SelectListAdap
        val activity = this
    var bid=""
    var finaltot=0.0
    var totqty=0
    lateinit var brand_spinners:Spinner
    lateinit var product_spinners:Spinner
    var discbrand=ArrayList<String>()
    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null
    var pop: AlertDialog? = null

    var propos=0
    var brandpos=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prolist)
        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Create Order"
        if (Appconstands.net_status(activity)) {
           // getBrands()
            getDiscount()
            //getCloudinary()
        }else{
            Toast.makeText(
                activity,
                "Check your Network connection",
                Toast.LENGTH_LONG
            ).show()

        }



        textView80.setText(intent.extras!!.getString("name"))

        button2.setOnClickListener {
            var totalqty=0
            var arr=JsonArray()

            for(i in 0 until CentresArrays.size){

                totalqty+=CentresArrays[i].CLocation!!.toInt()
                var obj=JsonObject()
                obj.addProperty("pid",CentresArrays[i].Cid)
                obj.addProperty("qty",CentresArrays[i].CLocation)
                obj.addProperty("discount",CentresArrays[i].time1)
                arr.add(obj)

            }
            if(CentresArrays.size>0&&totalqty!=0){
                sendExpense(pref!!.getString("cusid","").toString(),totalqty.toString(),arr)
            }
            else{
                if(CentresArrays.isEmpty()){
                    toast("Please Select Any Product")
                }
                if(totalqty==0){
                    toast("Please fill quantity")
                }
            }
        }


        addpro.setOnClickListener {
            editarea()
        }



    }

    fun sendExpense(name : String,
                    qty : String,
                    description : JsonArray,
                   ){


        val pDialog= ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Submitting Order...")
        pDialog.show()
        /*{"user":"9791981428","tid":"V00002","request_type":"4"
        "amount":"1000",
        "tour_plan":[{"tour_date":"","day":"","tour_city":"","customer":""}],
        "expenditure_plan":[{"expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""}]}*/

        val obj = JsonObject()
        val objarr = JsonArray()
        objarr.add(description)
        obj.addProperty("cus_id", name)
        obj.addProperty("total_qty", qty)
        obj.addProperty("enter_by", pref!!.getString("empid",""))
        obj.add("details", description)

        println("sendExpense : " + obj)

        val call = ApproveUtils.Get.sendorder(obj)//(user,tid,requesttypes[requesttype.selectedItemPosition].id.toString(),total_amount.text.toString(),TourPlans,ExpenditurePlans)
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

    fun toast(text:String){
        val toast=Toast.makeText(applicationContext,text,Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

    fun total(pos:Int,qty:String){
        finaltot=0.0
        totqty=0
        var obj=CentresData()
        obj.CName=CentresArrays[pos].CName
        obj.Cid=CentresArrays[pos].Cid
        obj.CTotal=CentresArrays[pos].CTotal
        obj.CMembers=CentresArrays[pos].CMembers
        obj.CStatus=CentresArrays[pos].CStatus
        obj.CLocation=qty
        obj.time1=CentresArrays[pos].time1
        obj.time2=CentresArrays[pos].time2
        obj.prodpos=CentresArrays[pos].prodpos
      CentresArrays.set(pos,obj)
        for(i in 0 until CentresArrays.size){
            if(CentresArrays[i].CLocation!!!="0"){
                finaltot+=CentresArrays[i].CTotal!!.toDouble()*CentresArrays[i].CLocation!!.toDouble()
                totqty+=CentresArrays[i].CLocation!!.toInt()
            }
        }

       /* if(finaltot==0.0){
            textView79.visibility=View.GONE

        }
        else{
            textView79.visibility=View.VISIBLE
            textView79.setText("Total Amount: "+finaltot.toString())
        }*/

    }

    fun disc(pos:Int,qty:String,brand:String){
        if(discbrand.isEmpty()||(!discbrand.contains(brand))) {
            discbrand.add(brand)
            Log.e("val",discbrand.toString())
            var obj = CentresData()
            obj.CName = CentresArrays[pos].CName
            obj.Cid = CentresArrays[pos].Cid
            obj.CTotal = CentresArrays[pos].CTotal
            obj.CMembers = CentresArrays[pos].CMembers
            obj.CStatus = CentresArrays[pos].CStatus
            obj.CLocation = CentresArrays[pos].CLocation
            obj.time1 = qty
            obj.time2 = CentresArrays[pos].time2
            obj.prodpos = CentresArrays[pos].prodpos
            CentresArrays.set(pos, obj)
            /* for(i in 0 until CentresArrays.size){
                 if(i!=pos){
                     var obj=CentresData()
                     obj.CName=CentresArrays[i].CName
                     obj.Cid=CentresArrays[i].Cid
                     obj.CTotal=CentresArrays[i].CTotal
                     obj.CMembers=CentresArrays[i].CMembers
                     obj.CStatus=CentresArrays[i].CStatus
                     obj.CLocation=CentresArrays[i].CLocation
                     obj.time1="0"
                     obj.time2=CentresArrays[i].time2
                     CentresArrays.set(pos,obj)
                 }
             }*/
        /*    prolistview.setLayoutManager(LinearLayoutManager(activity));
            adp = SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
            prolistview.adapter = adp*/
            adp.notifyDataSetChanged()
        }
        else{
            Log.e("inside","else")
            Toast.makeText(applicationContext,"Discount applied already for this brand",Toast.LENGTH_LONG).show()
           /* prolistview.setLayoutManager(LinearLayoutManager(activity));
            adp = SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
            prolistview.adapter = adp*/
            adp.notifyDataSetChanged()

        }



    }

    fun editarea() {
        var brid = ""
        var nmarr = ArrayList<String>()
        var idarr = ArrayList<String>()
        nmarr.add("Select")
        idarr.add("0")

        val dialogBuilder = AlertDialog.Builder(this)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.familydetails_popup, null)
        brand_spinners = dialogView.findViewById<Spinner>(R.id.frelation);
        product_spinners = dialogView.findViewById<Spinner>(R.id.mmarital);

        val cancel = dialogView.findViewById<ImageButton>(R.id.close);
        val add = dialogView.findViewById<Button>(R.id.button)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        pop = dialogBuilder.create()
        pop!!.show()


        brand_spinners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(brand_spinners.selectedItemPosition!=0){
                    brandpos=position
                    getProducts(brandid[brand_spinners.selectedItemPosition])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        product_spinners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(product_spinners.selectedItemPosition!=0){
                    propos=position
                    //getProducts(brandid[brand_spinners.selectedItemPosition])
                }
                else{
                    //toast("Please select product")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        if (Appconstands.net_status(activity)) {
            getBrands()
            //getDiscount()
            //getCloudinary()
        }else{
            Toast.makeText(
                activity,
                "Check your Network connection",
                Toast.LENGTH_LONG
            ).show()

        }
        //fageshort.setText(short)


        cancel.setOnClickListener {
            pop!!.dismiss();
            //doLcatload(type.toString(), "1")

        }
        add.setOnClickListener {
            if(Allproducts[propos].CMembers.toString().toInt()>0) {
                if (product_spinners.selectedItemPosition != 0) {
                    Log.e("not0", CentresArrays.size.toString())
                    if (CentresArrays.isNotEmpty()) {
                        var id: Int? = null
                        Log.e("not1", CentresArrays.size.toString())

                        for (j in 0 until CentresArrays.size) {
                            Log.e("inside", CentresArrays[j].CName.toString())
                            if (CentresArrays[j].CName!!.toString() == (Allproducts[propos].CName.toString())
                                && CentresArrays[j].time2 == Allproducts[propos].time2
                            ) {
                                id = j
                            }
                        }
                        if (id != null) {

                            var obj = CentresData()
                            obj.CName = CentresArrays[id].CName
                            obj.Cid = CentresArrays[id].Cid
                            obj.CTotal = CentresArrays[id].CTotal
                            obj.CMembers = CentresArrays[id].CMembers
                            obj.CStatus = CentresArrays[id].CStatus
                            obj.CLocation =
                                (CentresArrays[id].CLocation!!.toInt() + 1).toString()
                            obj.time1 = CentresArrays[id].time1
                            obj.time2 = CentresArrays[id].time2
                            obj.prodpos = CentresArrays[id].prodpos
                            CentresArrays.set(id, obj)
                            Log.e("name", CentresArrays[id].CName.toString())
                            Log.e("qty", CentresArrays[id].CLocation.toString())
                            Log.e("pos", propos.toString())
                            prolistview.setLayoutManager(LinearLayoutManager(activity));
                            adp =
                                SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
                            prolistview.adapter = adp

                        } else {
                            CentresArrays.add(Allproducts[propos])
                            Log.e("name", Allproducts[propos].CName.toString())
                            Log.e("pos", propos.toString())
                            prolistview.setLayoutManager(LinearLayoutManager(activity));
                            adp =
                                SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
                            prolistview.adapter = adp
                        }
                    } else {
                        CentresArrays.add(Allproducts[propos])
                        Log.e("name", Allproducts[propos].CName.toString())
                        Log.e("pos", propos.toString())
                        prolistview.setLayoutManager(LinearLayoutManager(activity));
                        adp =
                            SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
                        prolistview.adapter = adp
                    }

                    pop!!.dismiss();

                }
            }
            else{
                toast("No stock available in this product")
            }
        }
    }



    fun removeitem(pos:Int,disc:String,brand: String){
        if(disc!="0"){
            if(discbrand.size==1){
                discbrand.removeAt(0)
            }
            else{
                for(i in 0 until discbrand.size){
                    try {
                        if (discbrand[i] == brand) {
                            discbrand.removeAt(i)
                        }
                    }
                    catch (e:Exception){

                    }
                }
            }
        }
        CentresArrays.removeAt(pos)
       /* prolistview.setLayoutManager(LinearLayoutManager(activity));
        adp = SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
        prolistview.adapter = adp*/
        adp.notifyDataSetChanged()

        Log.e("discarr",discbrand.toString())
        Log.e("size",CentresArrays.size.toString())
    }

    fun discremove(pos:Int,qty:String,brand:String){
        if(discbrand.size==1){
            discbrand.removeAt(0)
        }
        else{
            for(i in 0 until discbrand.size){
                try {
                    if (discbrand[i] == brand) {
                        discbrand.removeAt(i)
                    }
                }
                catch (e:Exception){

                }
            }
        }

            Log.e("val",discbrand.toString())
            var obj = CentresData()
            obj.CName = CentresArrays[pos].CName
            obj.Cid = CentresArrays[pos].Cid
            obj.CTotal = CentresArrays[pos].CTotal
            obj.CMembers = CentresArrays[pos].CMembers
            obj.CStatus = CentresArrays[pos].CStatus
            obj.CLocation = CentresArrays[pos].CLocation
            obj.time1 = qty
            obj.time2 = CentresArrays[pos].time2
            obj.prodpos = CentresArrays[pos].prodpos
            CentresArrays.set(pos, obj)
            /* for(i in 0 until CentresArrays.size){
                 if(i!=pos){
                     var obj=CentresData()
                     obj.CName=CentresArrays[i].CName
                     obj.Cid=CentresArrays[i].Cid
                     obj.CTotal=CentresArrays[i].CTotal
                     obj.CMembers=CentresArrays[i].CMembers
                     obj.CStatus=CentresArrays[i].CStatus
                     obj.CLocation=CentresArrays[i].CLocation
                     obj.time1="0"
                     obj.time2=CentresArrays[i].time2
                     CentresArrays.set(pos,obj)
                 }
             }*/
            /*prolistview.setLayoutManager(LinearLayoutManager(activity));
            adp = SelectListAdap(CentresArrays, spinnerid, spinnername, this@Prolist)
            prolistview.adapter = adp*/

        adp.notifyDataSetChanged()



    }

    fun getBrands(){
        brandid.clear()
        brandname.clear()
        val call = ApproveUtils.Get.getbrands()
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("requesttypes", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "Success") {
                        var res=example.getResponse()
                        brandid.add("0")
                        brandname.add("Select")
                        for(i in 0 until res!!.size){
                            brandid.add(res[i].id.toString())
                            brandname.add(res[i].name.toString())
                        }
                        val adap=ArrayAdapter(this@Prolist,R.layout.support_simple_spinner_dropdown_item,brandname)
                        brand_spinners.adapter=adap
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

    fun getDiscount(){
        spinnerid.clear()
        spinnername.clear()
        val call = ApproveUtils.Get.getdiscount()
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("requesttypes", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "Success") {
                        var res=example.getResponse()
                        spinnerid.add("0")
                        spinnername.add("Select Discount")
                        for(i in 0 until res!!.size){
                            spinnerid.add(res[i].id.toString())
                            spinnername.add(res[i].name.toString())
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


    fun getProducts(bid:String){
        proidarr.clear()
        pronamearr.clear()
        Allproducts.clear()
        val call = ApproveUtils.Get.getproducts(bid)
        call.enqueue(object : Callback<Resp_trip> {
            override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                Log.e("requesttypes", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_trip
                    if (example.getStatus() == "Success") {
                        var res=example.getResponse()
                        proidarr.add("0")
                        pronamearr.add("Select")
                        var Centerdata=CentresData()
                        Centerdata.CName="Select"
                        Centerdata.Cid="0"
                        Centerdata.CTotal="0"
                        Centerdata.CMembers="0"
                        Centerdata.CStatus="0"
                        Centerdata.CLocation="0"
                        Centerdata.time1="0"
                        Centerdata.time2="0"
                        Centerdata.time2="0"
                        Centerdata.prodpos="0"
                        Allproducts.add(Centerdata)
                        for(i in 0 until res!!.size){
                            if(res[i].status=="1") {
                                proidarr.add(res[i].id.toString())
                                pronamearr.add(res[i].name.toString())
                                var Centerdata=CentresData()
                                Centerdata.CName = res[i].name.toString()
                                Centerdata.Cid = res[i].id.toString()
                                Centerdata.CTotal = res[i].open_stock.toString()
                                Centerdata.CMembers = res[i].stock.toString()
                                Centerdata.CStatus = res[i].status.toString()
                                Centerdata.CLocation = "0"
                                Centerdata.time1 = "0"
                                Centerdata.time2 = res[i].brand.toString()
                                Centerdata.prodpos = res[i].brand_nm.toString()
                                Allproducts.add(Centerdata)
                            }
                            val adap=ArrayAdapter(this@Prolist,R.layout.support_simple_spinner_dropdown_item,pronamearr)
                            product_spinners.adapter=adap
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}