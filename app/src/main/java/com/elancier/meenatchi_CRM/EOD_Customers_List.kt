package com.elancier.meenatchi_CRM


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cloudinary.Cloudinary
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_branch__list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class EOD_Customers_List : AppCompatActivity(){
    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    internal lateinit var fname: Spinner
    internal lateinit var fcode: EditText
    internal lateinit var fage: EditText
    var CentresArrays = ArrayList<OrderDetail>()
    var CentresArrays1 = ArrayList<OrderDetail>()
    var CentresArraysdup = ArrayList<OrderDetail>()
    var CentresArraysdup1 = ArrayList<OrderDetail>()
    val activity=this

    var posglo=0;
    lateinit var adp : MyCustomerAdap_EOD
    lateinit var adpdup : MyCustomerAdap_EOD
    lateinit var adp1 : MyOrderAdap
    lateinit var adp1dup : MyOrderAdap
    var cusid=""
    lateinit var click : MyCustomerAdap_EOD.OnItemClickListener
    lateinit var click1 : MyOrderAdap.OnItemClickListener
    var pop: AlertDialog?=null

    var stateid=""
    var cityname=""

    internal lateinit var histlist: ListView
    internal var catidarr: MutableList<String> = ArrayList()
    internal var nmarr: MutableList<String> = ArrayList()
    internal var staffnmarrdup: MutableList<String> = ArrayList()
    internal var imgarr: MutableList<String> = ArrayList()
    internal lateinit var progbar: Dialog
    internal lateinit var checklist:MutableList<String>
    lateinit var searchView:SearchView
    var update=""
    var catid=""
    var customerid=""
    var catnm=""
    var title:TextView?=null
     var save:TextView?=null
     var refed:TextInputEditText?=null
     var ordamt:TextInputEditText?=null
     var notes:TextInputEditText?=null
     var image1:ImageView?=null
     var image2:ImageView?=null
    var api_key="";
    var api_secret="";
    var cloud_name="";

    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    var imagecode=""
    internal lateinit var fi: File
    var panimage=""
    var bankimage=""
    var frm=""

    internal lateinit var byteArray1: ByteArray
    var imagecode1=""
    internal lateinit var fi1: File
    lateinit var pDialog:ProgressDialog
    internal var cdarr: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch__list)

        try{
            var frms=intent.extras
            frm=frms!!.getString("cus").toString()
            stateid=frms!!.getString("state").toString()
            cityname=frms!!.getString("city").toString()

        }
        catch (e:Exception){

        }

        shimmer_view_container.visibility= View.VISIBLE
        shimmer_view_container.startShimmer()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)

        if(frm=="customer"||frm=="eod") {
            ab!!.title = "Customers"
        }
        else{
            ab!!.title = "Previous Orders"
        }

        getWindow().setSoftInputMode(
            WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        catidarr=ArrayList<String>()
        nmarr=ArrayList<String>()
        imgarr=ArrayList<String>()
        cdarr=ArrayList<String>()
        checklist=ArrayList<String>()
        cloudinary()

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()

        click = object : MyCustomerAdap_EOD.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }

        }

        click1 = object : MyOrderAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }

        }


        memberslist.setLayoutManager(LinearLayoutManager(this));
        adp = MyCustomerAdap_EOD(CentresArrays, this@EOD_Customers_List, click)
        memberslist.adapter = adp

        memberslist.setLayoutManager(LinearLayoutManager(this));
        adpdup = MyCustomerAdap_EOD(CentresArrays1, this@EOD_Customers_List, click)
        memberslist.adapter = adpdup

        memberslist.setLayoutManager(LinearLayoutManager(this));
        adp1 = MyOrderAdap(CentresArraysdup, this@EOD_Customers_List, click1)
        memberslist.adapter = adp1

        memberslist.setLayoutManager(LinearLayoutManager(this));
        adp1dup = MyOrderAdap(CentresArraysdup1, this@EOD_Customers_List, click1)
        memberslist.adapter = adp1dup

        swiperefresh.setOnRefreshListener {
            Log.i("refresh", "onRefresh called from SwipeRefreshLayout")
            shimmer_view_container.visibility= View.VISIBLE
            shimmer_view_container.startShimmer()
            onResume()
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
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
                CentresArraysdup.clear()
                CentresArraysdup1.clear()
                if(newText!!.length>=3) {

                    if (CentresArrays.isNotEmpty()) {

                        for (i in 0 until CentresArrays.size) {
                            if (CentresArrays[i].customer_name!!.toLowerCase().contains(newText!!.toLowerCase())) {
                                var data=OrderDetail()
                                data.customer_name = CentresArrays[i].customer_name
                                data.id = CentresArrays[i].id.toString()
                                data.mobile = CentresArrays[i].mobile.toString()
                                data.address = CentresArrays[i].address.toString()
                                data.city = CentresArrays[i].city
                                data.state = CentresArrays[i].state
                                data.image = CentresArrays[i].image!!.split(",")[0]
                                data.contact_person=CentresArrays[i].contact_person
                                data.created_at=""
                                data.updated_at=""

                                if(frm=="customer"||frm=="eod") {
                                    CentresArraysdup.add(data)
                                }
                                else if(frm=="order"){
                                    val data= OrderDetail()
                                    data.customer_name = CentresArrays[i].customer_name
                                    data.id = CentresArrays[i].id.toString()
                                    data.mobile = CentresArrays[i].mobile.toString()
                                    data.customer_id = CentresArrays[i].customer_id.toString()
                                    data.address = CentresArrays[i].address.toString()
                                    data.city = CentresArrays[i].city
                                    try {
                                        data.image = CentresArrays[i].image!!
                                    }
                                    catch (e:Exception){
                                        data.image =""

                                    }
                                    try {
                                        data.image1 = CentresArrays[i].image1!!
                                        }
                                     catch (e:Exception){
                                         data.image1 = ""

                                    }
                                    data.created_at=""
                                    data.updated_at=""
                                    CentresArraysdup1.add(data)

                                }

                            }
                            if(frm=="customer"||frm=="eod") {
                                adpdup = MyCustomerAdap_EOD(
                                    CentresArraysdup,
                                    this@EOD_Customers_List,
                                        click
                                )
                                memberslist.adapter = adpdup
                                // memberslist.adapter = adp
                                shimmer_view_container.stopShimmer()
                                shimmer_view_container.visibility=View.GONE

                                if(CentresArraysdup.isEmpty()){
                                    textView23.visibility=View.VISIBLE

                                }
                            }
                            else{
                                adp1dup = MyOrderAdap(
                                    CentresArraysdup1,
                                    this@EOD_Customers_List,
                                    click1
                                )
                                memberslist.adapter = adp1dup
                                // memberslist.adapter = adp
                                shimmer_view_container.stopShimmer()
                                shimmer_view_container.visibility=View.GONE

                                if(CentresArraysdup1.isEmpty()){
                                    textView23.visibility=View.VISIBLE

                                }
                            }
                        }
                    }
                }
                else if(newText.length==0){
                    CentresArraysdup.clear()
                    CentresArraysdup1.clear()
                    if(frm=="customer"||frm=="eod") {
                        adp = MyCustomerAdap_EOD(
                            CentresArrays,
                            this@EOD_Customers_List,
                                click
                        )
                        memberslist.adapter = adp
                        // memberslist.adapter = adp
                        shimmer_view_container.stopShimmer()

                        shimmer_view_container.visibility = View.GONE


                        if (CentresArrays.isEmpty()) {
                            textView23.visibility = View.VISIBLE

                        }
                    }
                    else if(frm=="order"){
                        adp1 = MyOrderAdap(
                            CentresArrays,
                            this@EOD_Customers_List,
                            click1
                        )
                        memberslist.adapter = adp1
                        // memberslist.adapter = adp
                        shimmer_view_container.stopShimmer()

                        shimmer_view_container.visibility = View.GONE


                        if (CentresArrays.isEmpty()) {
                            textView23.visibility = View.VISIBLE

                        }
                    }
                }

                return false
            }
        })
        return true
    }



    override fun onOptionsItemSelected(item:MenuItem): Boolean {



        // Handle item selection
        when (item.itemId) {
            android.R.id.home ->{
              onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun cloudinary(){
        if (Appconstants.net_status(this)) {
            val call = ApproveUtils.Get.getcloudinary()
            call.enqueue(object : Callback<Resp_otps> {
                override fun onResponse(call: Call<Resp_otps>, response: Response<Resp_otps>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_otps
                        println(example)
                        if (example.status == "Success") {

                            var otpval=example!!.response
                            api_key=otpval!!.api_key.toString()
                            api_secret=otpval!!.api_secret.toString()
                            cloud_name=otpval!!.cloud_name.toString()

                        } else {
                            Toast.makeText(this@EOD_Customers_List, example.status, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_otps>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@EOD_Customers_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@EOD_Customers_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }




    fun toast(msg:String){
        val toast= Toast.makeText(this@EOD_Customers_List,msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
    fun editarea(brcode:String,anm:String,acode:String,aid:String,short:String) {
        var brid = ""
        var nmarr = ArrayList<String>()
        var idarr = ArrayList<String>()
        nmarr.add("Select")
        idarr.add("0")

        val dialogBuilder = AlertDialog.Builder(this@EOD_Customers_List)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this@EOD_Customers_List.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.addchannels_popup, null)
        fname = dialogView.findViewById<Spinner>(R.id.fname);
        fcode = dialogView.findViewById<EditText>(R.id.fcode);
        val tit= dialogView.findViewById<TextView>(R.id.tit);
        fage = dialogView.findViewById<EditText>(R.id.fage);
        val cancel = dialogView.findViewById<ImageButton>(R.id.cancel);
        val add = dialogView.findViewById<Button>(R.id.button)
        tit.setText("Edit VIllage")
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)
        add.text = "Update"
        pop = dialogBuilder.create()
        pop!!.show()

        fage.setText(acode)
        //fageshort.setText(short)

        fcode.setText(anm)

        cancel.setOnClickListener {
            pop!!.dismiss();
            //doLcatload(type.toString(), "1")

        }
        add.setOnClickListener {

            if (fcode.text.toString().trim().isNotEmpty() && fage.text.toString().trim().isNotEmpty()) {
                val type = pref.getString("id", "")
                progbar = Dialog(this@EOD_Customers_List)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.save)
                progbar.setCancelable(false)
                progbar.show()
                update = ""

                if(Appconstants.net_status(this)) {
                    /*dosavecatupdate(
                        fcode.text.toString().trim(),
                        fage.text.toString().trim(),aid)*/
                }
                else{
                    Toast.makeText(applicationContext, "You're offline", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }

    /*private fun dosavecatupdate( nm: String,cd: String,id:String) {

        val call = ApproveUtils.Get.editvillage(id,nm,cd)

        Log.e("inside", "$nm,msh$id")
        call.enqueue(object : Callback<Resp_dup> {
            override fun onResponse(call: Call<Resp_dup>, response: Response<Resp_dup>) {


                Log.v("responce", response.toString())
                if (response.isSuccessful) {


                    val login = response.body()
                    //if (!login.getCustomers().isEmpty()) {
                    Log.e("inssucc", "succ")
                    if (login!!.getStatus() == "Success") {
                        progbar.dismiss()
                        pop!!.dismiss()
                        onResume()

                        toast(login.getMessage()!!.getData().toString())


                    }
                    else
                    {
                        progbar.dismiss()

                        Toast.makeText(applicationContext,login.getMessage()!!.getData(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


            override fun onFailure(call:Call<Resp_dup>, throwable:Throwable) {
                Log.e("error", throwable.toString())
                //progbar.dismiss();
                progbar.dismiss()

                Toast.makeText(this@EOD_Customers_List, "Something went wrong", Toast.LENGTH_SHORT).show()

                //login.setEnabled(true);
                // Toast.makeText(MainActivity.this,"Failed"+t.toString(),Toast.LENGTH_SHORT).show();

            }
        })
    }*/


    override fun onResume() {
        super.onResume()
        textView23.visibility=View.GONE
        CentresArrays.clear()
        CentresArraysdup1.clear()
        CentresArraysdup.clear()
        if(frm=="customer"||frm=="eod"){
            Customers()
        }
        else if(frm=="order"){
           Orders()
        }

    }


    fun Customers(){
        if (Appconstants.net_status(this)) {
            CentresArrays.clear()
            println("tid : "+pref.getString("tid",""))
            val call = ApproveUtils.Get.eodCustomer(stateid,cityname)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example.getStatus())
                        if (example.getStatus() == "Success") {
                            //textView23.visibility=View.GONE
                            val otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val data= OrderDetail()
                                data.customer_name = otpval[i].name
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].mobile.toString()
                                data.address = otpval[i].address.toString()
                                data.city = otpval[i].city
                                data.state = otpval[i].state
                                try {
                                    data.image1 = otpval[i].image!!
                                }
                                catch (e:java.lang.Exception){
                                    data.image1 = ""

                                }
                                data.contact_person=otpval[i].contact_person
                                data.created_at=otpval[i].createdAt
                                data.updated_at=otpval[i].updatedAt
                                CentresArrays.add(data)

                            }
                            adp = MyCustomerAdap_EOD(
                                CentresArrays,
                                this@EOD_Customers_List,
                                    click
                            )
                            memberslist.adapter = adp
                            shimmer_view_container.stopShimmer()

                            shimmer_view_container.visibility=View.GONE


                            if(CentresArrays.isEmpty()){
                                textView23.visibility=View.VISIBLE

                            }

                        } else {
                            /*Toast.makeText(this@EOD_Customers_List, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                            shimmer_view_container.visibility=View.GONE
                            textView23.visibility=View.VISIBLE
                            shimmer_view_container.stopShimmer()

                        }
                    }
                    else{
                        shimmer_view_container.visibility=View.GONE
                        textView23.visibility=View.VISIBLE
                        shimmer_view_container.stopShimmer()
                    }
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@EOD_Customers_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility=View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            swiperefresh.isRefreshing=false
            Toast.makeText(
                this@EOD_Customers_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE

        }
    }

    fun Orders(){
        if (Appconstants.net_status(this)) {
            CentresArrays.clear()
            val call = ApproveUtils.Get.getordertrips(pref.getString("tid","").toString())
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            //textView23.visibility=View.GONE
                            val otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val data= OrderDetail()
                                data.customer_name = otpval[i].name
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].order_value.toString()
                                data.customer_id = otpval[i].customer_id.toString()
                                data.address = otpval[i].notes.toString()
                                data.city = otpval[i].order_details

                                    data.image = otpval[i].image
                                    try {
                                        data.image1 = otpval[i].image1
                                    }
                                    catch (e:java.lang.Exception){
                                        data.image1=""
                                    }

                                data.created_at=otpval[i].createdAt
                                data.updated_at=otpval[i].updatedAt
                                CentresArrays.add(data)

                            }
                            adp1 = MyOrderAdap(
                                CentresArrays,
                                this@EOD_Customers_List,
                                click1
                            )
                            memberslist.adapter = adp1
                            shimmer_view_container.stopShimmer()

                            shimmer_view_container.visibility=View.GONE


                            if(CentresArrays.isEmpty()){
                                textView23.visibility=View.VISIBLE

                            }

                        } else {
                            /*Toast.makeText(this@EOD_Customers_List, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*/
                            shimmer_view_container.visibility=View.GONE
                            textView23.visibility=View.VISIBLE
                            shimmer_view_container.stopShimmer()

                        }
                    }
                    else{
                        shimmer_view_container.visibility=View.GONE
                        textView23.visibility=View.VISIBLE
                        shimmer_view_container.stopShimmer()
                    }
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@EOD_Customers_List,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility=View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            swiperefresh.isRefreshing=false
            Toast.makeText(
                this@EOD_Customers_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE

        }
    }




    private fun dodeletecat( nm: String,cd: String,rate:String) {

        val call = ApproveUtils.Get.getbr2(nm,cd)

        Log.e("inside", "$nm,msh$call")
        call.enqueue(object : Callback<Resp_dup> {
            override fun onResponse(call: Call<Resp_dup>, response: Response<Resp_dup>) {


                Log.v("responce", response.toString())
                if (response.isSuccessful) {


                    val login = response.body()
                    //if (!login.getCustomers().isEmpty()) {
                    Log.e("inssucc", "succ")
                    if (login!!.getStatus() == "Success") {
                        progbar.dismiss()
                        //pop!!.dismiss()
                        onResume()

                        Toast.makeText(applicationContext, rate, Toast.LENGTH_SHORT).show()


                    }
                    else
                    {
                        progbar.dismiss()

                       // Toast.makeText(applicationContext,login.getStatus(), Toast.LENGTH_SHORT).show();

                    }
                    /*}
                    else{
                        //progbar.dismiss();

                        //Toast.makeText(LoginActivity.this, "Bad Credentials", Toast.LENGTH_SHORT).show();

                    }*/
                }
            }


            override fun onFailure(call: Call<Resp_dup>, throwable: Throwable) {
                Log.e("error", throwable.toString())
                //progbar.dismiss();
                progbar.dismiss()

                Toast.makeText(this@EOD_Customers_List, "Something went wrong", Toast.LENGTH_SHORT).show()

                //login.setEnabled(true);
                // Toast.makeText(MainActivity.this,"Failed"+t.toString(),Toast.LENGTH_SHORT).show();

            }
        })
    }
    fun dosavedel(user:String){
        progbar = Dialog(this@EOD_Customers_List)
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progbar.setContentView(R.layout.load)
        progbar.setCancelable(false)
        progbar.show()
        var AlertDialog=AlertDialog.Builder(this)
        AlertDialog.setTitle("Deactivate?")
        AlertDialog.setMessage("Are you sure?")
        AlertDialog.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->

                dodeletecat(user,"off","De-activated")
                dialogInterface.dismiss()


            })
        AlertDialog.setNegativeButton("No",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                progbar!!.dismiss()
                onResume()
            })
        val pops=AlertDialog.create()
        pops.show()


    }

    fun dosavedelenable(user:String){
        progbar = Dialog(this@EOD_Customers_List)
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progbar.setContentView(R.layout.load)
        progbar.setCancelable(false)
        progbar.show()
        var AlertDialog=AlertDialog.Builder(this)
        AlertDialog.setTitle("Activate?")
        AlertDialog.setMessage("Are you sure?")
        AlertDialog.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialogInterface, i ->

                dodeletecat(user,"on","Activated")
                dialogInterface.dismiss()


            })
        AlertDialog.setNegativeButton("No",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
                progbar!!.dismiss()

                onResume()
            })
        val pops=AlertDialog.create()
        pops.show()


    }

    fun EditOffline(id:String,name:String,pos:Int,CentreArrays:OrderDetail){
        if (frm=="eod"){
            val it = Intent()
            it.putExtra("customer_name",CentreArrays.customer_name.toString())
            it.putExtra("id",CentreArrays.id.toString())
            it.putExtra("mobile",CentreArrays.mobile.toString())
            it.putExtra("address",CentreArrays.address.toString())
            it.putExtra("city",CentreArrays.city.toString())
            it.putExtra("state",CentreArrays.state.toString())
            setResult(Activity.RESULT_OK,it)
            finish()
        }else {
            cusid = id
            val openwith = Dialog(this)
            openwith.setOnDismissListener {
                println("dismiss")
            }
            openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
            val popUpView = this@EOD_Customers_List!!.layoutInflater.inflate(R.layout.customer_order, null)
            var close = popUpView.findViewById<ImageButton>(R.id.imageButton10)
            title = popUpView.findViewById(R.id.textView16) as TextView
            title!!!!.setText(name)
            save = popUpView.findViewById(R.id.save) as TextView
            //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            refed = popUpView.findViewById(R.id.ref) as TextInputEditText
            ordamt = popUpView.findViewById(R.id.ordamt) as TextInputEditText
            notes = popUpView.findViewById(R.id.notes) as TextInputEditText
            val listval = popUpView.findViewById(R.id.textView84) as TextView
            image1 = popUpView.findViewById(R.id.imageView9) as ImageView
            image2 = popUpView.findViewById(R.id.imageView10) as ImageView
            val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
            popUpView.animation = animMoveToTop

            image1!!.setImageResource(R.mipmap.placeholders)
            image2!!.setImageResource(R.mipmap.placeholders)





            openwith.setContentView(popUpView)
            openwith.setCancelable(true)
            openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            openwith.window!!.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val displaymetrics = DisplayMetrics();
            this!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            val width = (displaymetrics.widthPixels * 1);
            val height = (displaymetrics.heightPixels * 1);
            openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            openwith.show()

            close.setOnClickListener {
                openwith.dismiss()
            }

            image1!!.setOnClickListener {

                if (CheckingPermissionIsEnabledOrNot(this) && (panimage.isEmpty() || panimage == "null")) {

                    selectImage()

                } else {
                    RequestMultiplePermission(activity!!)
                }

            }

            image2!!.setOnClickListener {

                if (CheckingPermissionIsEnabledOrNot(this) && (panimage.isEmpty() || panimage == "null")) {

                    selectImage1()


                } else {
                    RequestMultiplePermission(activity!!)
                }

            }


            save!!.setOnClickListener {

                if (refed!!.text.toString().trim().isNotEmpty() &&
                        ordamt!!.text.toString().trim().isNotEmpty()) {
                    openwith.dismiss()
                    pDialog = ProgressDialog(this!!);
                    pDialog.setMessage("Submitting...")
                    pDialog.setCancelable(false)
                    pDialog.show()

                    Handler().postDelayed({
                        if (imagecode.isNotEmpty() || imagecode1.isNotEmpty()) {
                            SendLogin(pref.getString("mobile", "").toString(), imagecode, imagecode1)

                        } else {
                            SendLogin(pref.getString("mobile", "").toString(), imagecode, imagecode1)
                        }

                    }, 2000)


                } else {
                    if (refed!!.text.toString().trim().isEmpty()) {
                        refed!!.setError("Required field")
                    }
                    if (ordamt!!.text.toString().trim().isEmpty()) {
                        ordamt!!.setError("Required field")
                    }

                }

            }
        }

    }

    fun Editorders(id:Int){
        posglo=id;
        customerid=CentresArrays[id].customer_id.toString()
        cusid=CentresArrays[id].id.toString()
        val openwith = Dialog(this)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = this@EOD_Customers_List!!.layoutInflater.inflate(R.layout.customer_order, null)
        var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        title=popUpView.findViewById(R.id.textView16) as TextView
        title!!!!.setText(CentresArrays[id].customer_name)
        save=popUpView.findViewById(R.id.save) as TextView
        save!!.setText("UPDATE")
        //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        refed=popUpView.findViewById(R.id.ref) as TextInputEditText
        ordamt=popUpView.findViewById(R.id.ordamt) as TextInputEditText
        notes=popUpView.findViewById(R.id.notes) as TextInputEditText
        refed!!.setText(CentresArrays[id].city)
        ordamt!!.setText(CentresArrays[id].mobile)
        notes!!.setText(CentresArrays[id].address)
        val listval=popUpView.findViewById(R.id.textView84) as TextView
        image1=popUpView.findViewById(R.id.imageView9) as ImageView
        image2=popUpView.findViewById(R.id.imageView10) as ImageView
        val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
        popUpView.animation =animMoveToTop

        image1!!.setImageResource(R.mipmap.placeholders)
        image2!!.setImageResource(R.mipmap.placeholders)

        if(CentresArraysdup1.isEmpty()) {
            println("inside img")
            if (CentresArrays[id].image!!.isNotEmpty()) {
                Glide.with(this).load(CentresArrays[id].image).into(image1!!)
            }

            if (CentresArrays[id].image1!!.isNotEmpty()) {
                Glide.with(this).load(CentresArrays[id].image1).into(image2!!)
            }
        }
        else{
            if (CentresArraysdup1[id].image!!.isNotEmpty()) {
                Glide.with(this).load(CentresArraysdup1[id].image).into(image1!!)
            }

            if (CentresArraysdup1[id].image1!!.isNotEmpty()) {
                Glide.with(this).load(CentresArraysdup1[id].image1).into(image2!!)
            }
        }

        openwith.setContentView(popUpView)
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val displaymetrics = DisplayMetrics();
        this!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width =  (displaymetrics.widthPixels * 1);
        val height =  (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        openwith.show()

        close.setOnClickListener {
            openwith.dismiss()
        }

        image1!!.setOnClickListener {

            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                selectImage()

            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }

        image2!!.setOnClickListener {

            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                selectImage1()


            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }


        save!!.setOnClickListener {

            if(refed!!.text.toString().trim().isNotEmpty()&&
                ordamt!!.text.toString().trim().isNotEmpty()) {
                openwith.dismiss()
                pDialog = ProgressDialog(this!!);
                pDialog.setMessage("Submitting...")
                pDialog.setCancelable(false)
                pDialog.show()

                Handler().postDelayed({
                    if(imagecode.isNotEmpty()||imagecode1.isNotEmpty()) {
                        SendLogin(pref.getString("mobile","").toString(),imagecode,imagecode1)

                    }
                    else{
                        SendLogin(pref.getString("mobile","").toString(),"","")
                    }

                }, 2000)

            }
            else{
                if(refed!!.text.toString().trim().isEmpty()){
                    refed!!.setError("Required field")
                }
                if(ordamt!!.text.toString().trim().isEmpty()){
                    ordamt!!.setError("Required field")
                }

            }

        }

    }

    fun SendLogin(mobile:String,url:String,url1:String){
        var image=""
        if(url.isNotEmpty()&&url1.isNotEmpty()){
            imagecode=url
            imagecode1=url1
        }
        else if(url.isEmpty()&&url1.isNotEmpty()){

            if(CentresArraysdup1.isEmpty()) {
                if (CentresArrays[posglo].image!!.isNotEmpty()) {
                    imagecode = CentresArrays[posglo].image.toString()
                    imagecode1 = "data:image/png;base64"+","+url1
                    ;
                }
            }
            else{
                if (CentresArraysdup1[posglo].image!!.isNotEmpty()) {
                    imagecode = CentresArraysdup1[posglo].image.toString()
                    imagecode1 = CentresArraysdup1[posglo].image1.toString()
                }
            }


        }
        else if(url.isNotEmpty()&&url1.isEmpty()){
            if(CentresArraysdup1.isEmpty()) {
                try {
                    imagecode = "data:image/png;base64" + "," + url
                    if (CentresArrays[posglo].image!!.isNotEmpty()) {
                        //imagecode1 = CentresArrays[posglo].image1.toString();

                    }
                }
                catch (e:Exception){

                }
            }
            else{
                imagecode = "data:image/png;base64"+","+url
                /*if (CentresArraysdup1[posglo].image!!.isNotEmpty()) {
                    //=CentresArrays[posglo].image1.toString();

                }*/
            }
        }


        lateinit var call:Call<Resp_otp>
        if(frm=="customer"||frm=="eod"){
            call = ApproveUtils.Get.getentry(pref.getString("tid","").toString(),
                cusid,notes!!.text.toString(),refed!!.text.toString(),ordamt!!.text.toString(),
                    imagecode,imagecode1,mobile)
        }
        else if(frm=="order"){
            call= ApproveUtils.Get.getentry_update(pref.getString("tid","").toString(),
                customerid,notes!!.text.toString(),refed!!.text.toString(),ordamt!!.text.toString(),
                imagecode,imagecode1,mobile,cusid)
        }

        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr=example.message
                        toast(arr.toString())
                        onResume()
                        //finish()
                    } else {
                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })

    }

    public fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 102)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 103)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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

    inner class Get : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo: ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            runOnUiThread {

                pDialo = ProgressDialog(this@EOD_Customers_List);
                pDialo.setMessage("Uploading....");
                pDialo.setIndeterminate(false);
                pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialo.setCancelable(false);
                //pDialo.setMax(3)
                pDialo.show()
            }
            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val config = HashMap<Any, Any>();
            config.put("cloud_name", cloud_name);
            config.put("api_key", api_key);
            config.put("api_secret", api_secret);
            val cloudinary = Cloudinary(config);
            var k =""
            var k1 =""
            try {
                if(imagecode.isNotEmpty()) {
                    val fi = cloudinary.uploader().upload("data:image/png;base64" + "," + imagecode, HashMap<Any, Any>());
                    k= fi.get("url") as String
                }
                if(imagecode1.isNotEmpty()){
                    val fis = cloudinary.uploader().upload("data:image/png;base64"+","+imagecode1, HashMap<Any, Any>());
                    k1=fis.get("url") as String

                }

                Log.e("fival", k.toString());

                runOnUiThread{
                    pDialo.dismiss()
                    SendLogin(pref.getString("mobile","").toString(),k,k1)
                }

                //finish()


            } catch (e: IOException) {
                e.printStackTrace();
                Log.e("except",e.toString())
                //pDialo.dismiss()
            }

            return result
        }

        override fun onPostExecute(resp: String?) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1) {
                    if (resultCode == Activity.RESULT_OK && null != data) {
                        var picturePath: String? = null
                        var selectedImage = data!!.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        //Log.i("original path1", picturePath + "")

                        //removeimg.setVisibility(View.VISIBLE);
                        // addimgbut.setVisibility(View.GONE);

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

                        image1!!.setImageBitmap(resizeBitmap)
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
                } else if (requestCode == 102) {


                    try {

                        var selectedImageUri = data!!.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity!!.getBaseContext().getContentResolver(),
                            selectedImageUri
                        )
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                        image1!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                        val path = getImgPath(selectedImageUri!!)
                        //getBase64FromPath(path);
                        if (path != null) {
                            val f = File(path!!)

                            selectedImageUri = Uri.fromFile(f)

                        }


                    } catch (e: Exception) {
                        val thumbnail = data!!.extras!!.get("data") as Bitmap?
                        val stream = ByteArrayOutputStream()
                        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        byteArray = stream.toByteArray()

                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                        saveImage(thumbnail)

                        //imglin.visibility = View.VISIBLE
                        image1!!.setImageBitmap(thumbnail)
                        //upload.setText("Retake")
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
                        //val image1 = CommonFunctions.decodeFile1(picturePath, 0, 0)
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity!!.getBaseContext().getContentResolver(),
                            selectedImage
                        )

                        val resizeBitmap =
                            resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        image2!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray1 = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                        Log.e("imagecode", imagecode1)

                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)

                        }

                    } else {

                    }
                } else if (requestCode == 103) {


                    try {

                        var selectedImageUri = data!!.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity!!.getBaseContext().getContentResolver(),
                            selectedImageUri
                        )
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                        image2!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray1 = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)

                        val path = getImgPath(selectedImageUri!!)
                        //getBase64FromPath(path);
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

                        //imglin.visibility = View.VISIBLE
                        image2!!.setImageBitmap(thumbnail)
                        //upload.setText("Retake")
                    }




                }
            }




        }
        catch (e:Exception){
        }
    }


    override fun onBackPressed() {
        if (!searchView.isIconified())
        {
            searchView.setIconified(true)
            searchView.onActionViewCollapsed()

            if(frm=="order") {
                adp1 = MyOrderAdap(
                    CentresArrays,
                    this@EOD_Customers_List,
                    click1
                )
                memberslist.adapter = adp1
                shimmer_view_container.stopShimmer()

                shimmer_view_container.visibility = View.GONE


                if (CentresArrays.isEmpty()) {
                    textView23.visibility = View.VISIBLE

                }
            }
            else if(frm=="customer"||frm=="eod"){
                adp= MyCustomerAdap_EOD(
                    CentresArrays,
                    this@EOD_Customers_List,
                        click
                )
                memberslist.adapter = adp
                shimmer_view_container.stopShimmer()

                shimmer_view_container.visibility = View.GONE


                if (CentresArrays.isEmpty()) {
                    textView23.visibility = View.VISIBLE

                }
            }

        }
        else
        {
            super.onBackPressed()
        }
    }
}

private fun <E> MutableList<E>.add(element: OrderDetail) {

}
