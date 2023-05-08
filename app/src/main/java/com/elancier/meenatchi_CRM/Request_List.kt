package com.elancier.meenatchi_CRM


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.meenatchi_CRM.Adapers.ExpenseListAdapter
import com.elancier.meenatchi_CRM.Adapers.RequestListAdapter
import com.elancier.meenatchi_CRM.Adapers.SalesListAdapter_Products
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_branch__list.*
import kotlinx.android.synthetic.main.activity_branch__list.shimmer_view_container
import kotlinx.android.synthetic.main.activity_branch__list.swiperefresh
import kotlinx.android.synthetic.main.activity_branch__list.textView23
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class Request_List : AppCompatActivity(),RequestListAdapter.OnItemClickListener,ExpenseListAdapter.OnItemClickListener {
    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    internal lateinit var fname: Spinner
    internal lateinit var fcode: EditText
    internal lateinit var fage: EditText
    var CentresArrays = ArrayList<OrderDetail>()
    var CentresArraysdup = ArrayList<OrderDetail>()
    val activity = this
    var CentresArrays1 = ArrayList<OrderDetail>()

    var CentresArraysdup1 = ArrayList<OrderDetail>()

    lateinit var adpdup : MyRequestAdap
    lateinit var adp1dup : MyExpAdap
    lateinit var adp: MyRequestAdap
    lateinit var adp1: MyExpAdap
    var cusid = ""
    lateinit var click: MyRequestAdap.OnItemClickListener
    lateinit var click1: MyExpAdap.OnItemClickListener
    var pop: AlertDialog? = null

    internal lateinit var histlist: ListView
    internal var catidarr: MutableList<String> = ArrayList()
    internal var nmarr: MutableList<String> = ArrayList()
    internal var staffnmarrdup: MutableList<String> = ArrayList()
    internal var imgarr: MutableList<String> = ArrayList()
    internal lateinit var progbar: Dialog
    internal lateinit var checklist: MutableList<String>
    lateinit var searchView: SearchView
    var catid = ""
    var customerid = ""
    var catnm = ""
    var title: TextView? = null
    var save: TextView? = null
    var refed: TextView? = null
    var ordamt: TextView? = null
    var notes: TextInputEditText? = null
    var image1: ImageView? = null
    var image2: ImageView? = null

    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    var imagecode = ""
    internal lateinit var fi: File
    var panimage = ""
    var bankimage = ""
    var frm = ""
    lateinit var update:Dialog
    internal lateinit var byteArray1: ByteArray
    var imagecode1 = ""
    internal lateinit var fi1: File
    lateinit var pDialog: ProgressDialog
    internal var cdarr: MutableList<String> = ArrayList()

    var Requestlist = JSONArray()
    lateinit var adps : RequestListAdapter
    lateinit var adps1 : SalesListAdapter_Products
    lateinit var adpe : ExpenseListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch__list)


        try {
            var frms = intent.extras
            frm = frms!!.getString("cus").toString()

        } catch (e: Exception) {

        }

        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)

      //  add_family.visibility=View.VISIBLE


        if (frm == "customer") {
            ab!!.title = "Sales List"
        } else {
            ab!!.title = "Order List"
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        catidarr = ArrayList<String>()
        nmarr = ArrayList<String>()
        imgarr = ArrayList<String>()
        cdarr = ArrayList<String>()
        checklist = ArrayList<String>()

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()

        click = object : MyRequestAdap.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int, viewType:Int) {

            }

        }
        click1 = object : MyExpAdap.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }

        }






        memberslist.setLayoutManager(LinearLayoutManager(this));
        adp = MyRequestAdap(CentresArrays, this@Request_List, click)
        memberslist.adapter = adp

        memberslist.setLayoutManager(LinearLayoutManager(this));
        adpdup = MyRequestAdap(CentresArrays1, this@Request_List, click)
        memberslist.adapter = adpdup



        swiperefresh.setOnRefreshListener {
            Log.i("refresh", "onRefresh called from SwipeRefreshLayout")
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmer()
            onResume()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {



        // Handle item selection
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun toast(msg: String) {
        val toast = Toast.makeText(this@Request_List, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun editarea(brcode: String, anm: String, acode: String, aid: String, short: String) {
        var brid = ""
        var nmarr = ArrayList<String>()
        var idarr = ArrayList<String>()
        nmarr.add("Select")
        idarr.add("0")

        val dialogBuilder = AlertDialog.Builder(this@Request_List)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this@Request_List.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.addchannels_popup, null)
        fname = dialogView.findViewById<Spinner>(R.id.fname);
        fcode = dialogView.findViewById<EditText>(R.id.fcode);
        val tit = dialogView.findViewById<TextView>(R.id.tit);
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

            if (fcode.text.toString().trim().isNotEmpty() && fage.text.toString().trim()
                    .isNotEmpty()
            ) {
                val type = pref.getString("id", "")
                progbar = Dialog(this@Request_List)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.save)
                progbar.setCancelable(false)
                progbar.show()




                if (Appconstants.net_status(this)) {

                } else {
                    Toast.makeText(applicationContext, "You're offline", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }



    override fun onResume() {
        super.onResume()
        textView23.visibility=View.GONE
        CentresArrays.clear()
        CentresArraysdup.clear()
        CentresArraysdup1.clear()
        if(frm=="customer"){
            Customers()
        }
        else if(frm=="order"){
           Orders()
        }

    }


    fun Customers(){
        if (Appconstants.net_status(this)) {
            val tid = pref.getString("cusid", "").toString()
            CentresArrays.clear()

            val calls = ApproveUtils.Get.getRequestList(tid,pref.getString("empid", "").toString())
            val objs = JSONObject()
            objs.put("tid",tid)
            println("tourid"+tid)
            calls.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, responses: Response<ResponseBody>) {
                    Log.e(objs.toString(),responses.toString())
                    if (responses.isSuccessful) {
                        val resp = responses.body()?.string()
                        val obj = JSONObject(resp.toString())
                        val status = obj.getString("status")
                        val message = obj.getString("message")
                        if (status == "Success") {
                            val response = obj.getJSONArray("response")
                            Requestlist = response
                        } else {
                            textView23.visibility = View.VISIBLE
                        }
                        adps = RequestListAdapter(Requestlist, activity, activity,frm)
                        memberslist.adapter = adps
                        /*
                        "id": 25,
                        "type": "Advance Request",
                        "name": null,
                        "user": "9894940560",
                        "tid": "V00018",
                        "image": "http://teamdev.co.in/vanitha/public/uploads/request/9209image.png",
                        "image1": "http://teamdev.co.in/vanitha/public/uploads/request/5000image1.png",
                        "description": null,
                        "amount": "6500",
                        "status": "Pending",
                        "approved_amount": "",
                        "notes": "",
                        "approved_date": "",
                        "tour_plan": [],
                        "expenditure_plan": []*/
                    } else {
                        textView23.visibility = View.VISIBLE
                    }
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    textView23.visibility = View.VISIBLE
                    swiperefresh.isRefreshing=false
                }

            })
        }else{
            Toast.makeText(
                this@Request_List,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE
            swiperefresh.isRefreshing=false
        }
    }

    fun Orders(){
        if (Appconstants.net_status(this)) {
            CentresArrays.clear()
            val tid = pref.getString("cusid", "").toString()
            Log.e("tid",tid)

            /*val call = ApproveUtils.Get.getexp(pref.getString("tid", "").toString())
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example.getMessage())
                        println(example.getStatus())
                        if (example.getStatus() == "Success") {
                            textView23.visibility = View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val data = OrderDetail()
                                data.customer_name = otpval[i].name.toString()
                                data.id = otpval[i].id.toString()
                                data.mobile = otpval[i].amount.toString()
                                data.address = otpval[i].description.toString()
                                data.shopnm = otpval[i].notes.toString()
                                data.city = otpval[i].type

                                data.image = otpval[i].image
                                data.image1 = otpval[i].image1
                                println("image" + data.image)
                                CentresArrays.add(data)
                            }
                            adp1 = MyExpAdap(
                                    CentresArrays,
                                    this@Request_List,
                                    click1
                            )
                            memberslist.adapter = adp1
                            shimmer_view_container.stopShimmer()

                            shimmer_view_container.visibility = View.GONE


                            if (CentresArrays.isEmpty()) {
                                textView23.visibility = View.VISIBLE

                            }

                        } else {
                            *//*Toast.makeText(this@Request_List, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*//*
                            shimmer_view_container.visibility = View.GONE
                            textView23.visibility = View.VISIBLE
                            shimmer_view_container.stopShimmer()

                        }
                    } else {
                        shimmer_view_container.visibility = View.GONE
                        textView23.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                                this@Request_List,
                                "Poor network connection",
                                Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                }
            })*/

            /* "id": 32,
            "type": "Stay",
            "name": null,
            "user": "9894940560",
            "tid": "V00018",
            "image": "http://teamdev.co.in/vanitha/public/uploads/expense/3173image.png",
            "image1": "http://teamdev.co.in/vanitha/public/uploads/expense/6457image1.png",
            "hotel_name": "GRT Hotel",
            "city": "",
            "from": null,
            "to": null,
            "by": null,
            "description": null,
            "amount": "2000"*/

            val calls = ApproveUtils.Get.getorders_list(tid,pref.getString("empid", "").toString())
            val objs = JSONObject()
            objs.put("tid",tid)
            calls.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, responses: Response<ResponseBody>) {
                    Log.e(objs.toString(),responses.toString())
                    if (responses.isSuccessful) {
                        val resp = responses.body()?.string()
                        val obj = JSONObject(resp.toString())
                        val status = obj.getString("status")
                        val message = obj.getString("message")
                        if (status == "Success") {
                            val response = obj.getJSONArray("response")
                            Requestlist = response
                        } else {
                            textView23.visibility = View.VISIBLE
                        }
                        adps = RequestListAdapter(Requestlist, activity, activity,frm)
                        memberslist.adapter = adps
                        /*
                        "id": 25,
                        "type": "Advance Request",
                        "name": null,
                        "user": "9894940560",
                        "tid": "V00018",
                        "image": "http://teamdev.co.in/vanitha/public/uploads/request/9209image.png",
                        "image1": "http://teamdev.co.in/vanitha/public/uploads/request/5000image1.png",
                        "description": null,
                        "amount": "6500",
                        "status": "Pending",
                        "approved_amount": "",
                        "notes": "",
                        "approved_date": "",
                        "tour_plan": [],
                        "expenditure_plan": []*/
                    } else {
                        textView23.visibility = View.VISIBLE
                    }
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    shimmer_view_container.stopShimmer()
                    shimmer_view_container.visibility = View.GONE
                    textView23.visibility = View.VISIBLE
                    swiperefresh.isRefreshing=false
                }

            })
        }
        else{
            Toast.makeText(
                    this@Request_List,
                    "You're offline",
                    Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE
            swiperefresh.isRefreshing=false
        }
    }





    fun EditOffline(id: String, name: String){
        cusid=id
        val openwith = Dialog(this)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = this@Request_List!!.layoutInflater.inflate(R.layout.customer_order, null)
        var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        title=popUpView.findViewById(R.id.textView16) as TextView
        title!!!!.setText(name)
        save=popUpView.findViewById(R.id.save) as TextView
        //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
        refed=popUpView.findViewById(R.id.ref) as TextInputEditText
        ordamt=popUpView.findViewById(R.id.ordamt) as TextInputEditText
        notes=popUpView.findViewById(R.id.notes) as TextInputEditText
        val listval=popUpView.findViewById(R.id.textView84) as TextView
        image1=popUpView.findViewById(R.id.imageView9) as ImageView
        image2=popUpView.findViewById(R.id.imageView10) as ImageView
        val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
        popUpView.animation =animMoveToTop

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

                if(refed!!.text.toString().isNotEmpty()&&
                    ordamt!!.text.toString().isNotEmpty()) {
                    openwith.dismiss()
                    pDialog = ProgressDialog(this!!);
                    pDialog.setMessage("Submitting...")
                    pDialog.setCancelable(false)
                    pDialog.show()
                    Handler().postDelayed({
                        SendLogin(pref.getString("mobile", "").toString())

                    }, 2000)

                }
                 else{
                    if(refed!!.text.toString().isEmpty()){
                        refed!!.setError("Required field")
                    }
                    if(ordamt!!.text.toString().isEmpty()){
                        ordamt!!.setError("Required field")
                    }

                }

        }

    }

    fun Editorders(id: Int){
        customerid=CentresArrays[id].customer_id.toString()
        cusid=CentresArrays[id].id.toString()
        val openwith = Dialog(this)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = this@Request_List!!.layoutInflater.inflate(R.layout.customer_order, null)
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

            if(refed!!.text.toString().isNotEmpty()&&
                ordamt!!.text.toString().isNotEmpty()) {
                openwith.dismiss()
                pDialog = ProgressDialog(this!!);
                pDialog.setMessage("Submitting...")
                pDialog.setCancelable(false)
                pDialog.show()
                Handler().postDelayed({
                    SendLogin(pref.getString("mobile", "").toString())

                }, 2000)

            }
            else{
                if(refed!!.text.toString().isEmpty()){
                    refed!!.setError("Required field")
                }
                if(ordamt!!.text.toString().isEmpty()){
                    ordamt!!.setError("Required field")
                }

            }

        }

    }

    fun SendLogin(mobile: String){
        lateinit var call:Call<Resp_otp>
        if(frm=="customer"){
            call = ApproveUtils.Get.getentry(pref.getString("tid", "").toString(),
                    cusid, notes!!.text.toString(), refed!!.text.toString(), ordamt!!.text.toString(),
                    "", "", mobile)
        }
        else if(frm=="order"){
            call= ApproveUtils.Get.getentry_update(pref.getString("tid", "").toString(),
                    customerid, notes!!.text.toString(), refed!!.text.toString(), ordamt!!.text.toString(),
                    "", "", mobile, cusid)
        }

        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr = example.message
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


    fun delete(id:String){
        val pdialog=ProgressDialog(this)
        pdialog.setMessage("Deleting...")
        pdialog.setCancelable(false)
        pdialog.show()

        val call = ApproveUtils.Get.deleteexp(Appconstands.Domin+"expenseDelete/"+id)
        call.enqueue(object : Callback<Resp_otp> {
            override fun onResponse(call: Call<Resp_otp>, response: Response<Resp_otp>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otp
                    println(example)
                    if (example.status == "Success") {
                        var arr = example.message
                        toast(arr.toString())
                        pdialog.dismiss()
                        onResume()
                        //finish()
                    } else {
                        pdialog.dismiss()

                        Toast.makeText(
                            activity,
                            example.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else{
                    pdialog.dismiss()
                }
               // pDialog.dismiss()
                //loading_show(activity).dismiss()
            }

            override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
                Log.e("Fail response", t.toString())
                pdialog.dismiss()

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

    fun clikffed(title: String,total:String,json:JSONArray) {
        try {

            update = Dialog(this)
            update.requestWindowFeature(Window.FEATURE_NO_TITLE)
            update.window!!.setBackgroundDrawable(
                ColorDrawable(Color.WHITE)
            )
            val v = layoutInflater.inflate(R.layout.feedback_popup, null)
            val prolist = v.findViewById<View>(R.id.feedlist) as RecyclerView
            val scroll = v.findViewById<View>(R.id.sroll) as ScrollView
            val nofeed = v.findViewById<View>(R.id.textView18) as TextView
            val feed = v.findViewById<View>(R.id.textView15) as TextView
            val feedvt = v.findViewById<View>(R.id.textView15vtime) as TextView
            val feedcut = v.findViewById<View>(R.id.textView15vcut) as TextView
            val textView88 = v.findViewById<View>(R.id.textView88) as TextView
            val laterbut = v.findViewById<View>(R.id.agree) as Button
            val viewfile = v.findViewById<View>(R.id.viewfile) as Button
            val download = v.findViewById<View>(R.id.download) as Button
            val close = v.findViewById<ImageButton>(R.id.imageButton11) as ImageButton

            if(frm=="customer") {
                feedcut.text = "Order Total - $total"
                feed.text = "INVOICE - "+title

            }
            else{
                feedcut.text = "Total Quantities - $total"
                feed.text = "ORDER ID - "+title

            }

            close.setOnClickListener {
                update.dismiss()
            }
            laterbut.setOnClickListener {
                update.dismiss()

            }

            adps1 = SalesListAdapter_Products(json, activity)
            prolist.adapter = adps1
            update.setContentView(v)

            val window = update.window
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            update.setCancelable(false)
            update.show()

        } catch (e: Exception) {
            Log.e("Log val", e.toString())
            //logger.info("PerformVersionTask error" + e.getMessage());
        }
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
    private fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
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
        catch (e: Exception){
        }
    }

    fun imgpo(pos: Int, req: String){
        if(CentresArraysdup1.isEmpty()){
            val openwith = Dialog(this)
            openwith.setOnDismissListener {
                println("dismiss")
            }
            openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
            val popUpView = this@Request_List!!.layoutInflater.inflate(R.layout.img_pop, null)
            var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            title=popUpView.findViewById(R.id.textView16) as TextView
            title!!!!.setText(CentresArrays[pos].customer_name)
            save=popUpView.findViewById(R.id.save) as TextView
            //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            refed=popUpView.findViewById(R.id.desc_lay) as TextView
            ordamt=popUpView.findViewById(R.id.reasonlay) as TextView
            val reasonamt=popUpView.findViewById(R.id.reasonamt) as TextView

            notes=popUpView.findViewById(R.id.notes) as TextInputEditText
            val listval=popUpView.findViewById(R.id.textView84) as TextView
            image1=popUpView.findViewById(R.id.imageView9) as ImageView
            image2=popUpView.findViewById(R.id.imageView10) as ImageView
            val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
            popUpView.animation =animMoveToTop

            image1!!.setImageResource(R.mipmap.noimage)
            image2!!.setImageResource(R.mipmap.noimage)

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

            refed!!.setText(CentresArrays[pos].address)
            ordamt!!.setText(CentresArrays[pos].shopnm)
            refed!!.setFocusable(false)
            ordamt!!.setFocusable(false)

            if(req=="Expense"){
                ordamt!!.visibility=View.GONE
                reasonamt!!.visibility=View.GONE
            }

            close.setOnClickListener {
                openwith.dismiss()
            }

            if(CentresArrays[pos].image!!.isNotEmpty()){

                Glide.with(this).load(CentresArrays[pos].image).into(image1!!)
                Glide.with(this).load(CentresArrays[pos].image).into(imageView7)
            }
            if(CentresArrays[pos].image1!!.isNotEmpty()){
                Glide.with(this).load(CentresArrays[pos].image1).into(image2!!)
            }

            image1!!.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.VISIBLE
                imageButton7.visibility=View.VISIBLE
            }
            imageButton7.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.GONE
                imageButton7.visibility=View.GONE

            }

        }
        else{
            val openwith = Dialog(this)
            openwith.setOnDismissListener {
                println("dismiss")
            }
            openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
            val popUpView = this@Request_List!!.layoutInflater.inflate(R.layout.img_pop, null)
            var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            title=popUpView.findViewById(R.id.textView16) as TextView
            title!!!!.setText(CentresArrays[pos].customer_name)
            save=popUpView.findViewById(R.id.save) as TextView
            //var close=popUpView.findViewById<ImageButton>(R.id.imageButton10)
            refed=popUpView.findViewById(R.id.desc_lay) as TextView
            ordamt=popUpView.findViewById(R.id.reasonlay) as TextView
            val reasonamt=popUpView.findViewById(R.id.reasonamt) as TextView
            notes=popUpView.findViewById(R.id.notes) as TextInputEditText
            val listval=popUpView.findViewById(R.id.textView84) as TextView
            image1=popUpView.findViewById(R.id.imageView9) as ImageView
            image2=popUpView.findViewById(R.id.imageView10) as ImageView
            val animMoveToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_top)
            popUpView.animation =animMoveToTop

            image1!!.setImageResource(R.mipmap.placeholders)
            image2!!.setImageResource(R.mipmap.placeholders)

            refed!!.setText(CentresArrays[pos].address)
            ordamt!!.setText(CentresArrays[pos].shopnm)
            ordamt!!.visibility=View.INVISIBLE
            reasonamt!!.visibility=View.INVISIBLE
            //refed!!.setFocusable(false)
            //ordamt!!.setFocusable(false)

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
                openwith.dismiss()
                imageView7.visibility=View.VISIBLE
                imageButton7.visibility=View.VISIBLE
            }
            imageButton7.setOnClickListener {
                openwith.dismiss()
                imageView7.visibility=View.GONE
                imageButton7.visibility=View.GONE

            }
            if(CentresArraysdup1[pos].image!!.isNotEmpty()){
                //view6.visibility=View.VISIBLE
                Glide.with(this).load(CentresArraysdup1[pos].image).into(image1!!)
                Glide.with(this).load(CentresArraysdup1[pos].image).into(imageView7)

            }
            if(CentresArraysdup1[pos].image1!!.isNotEmpty()){
               // view6.visibility=View.VISIBLE
                Glide.with(this).load(CentresArraysdup1[pos].image1).into(image2!!)
            }
        }
    }


    override fun onBackPressed() {

        try {
            if (update.isShowing) {
                update.dismiss()
            } else {
                super.onBackPressed()
            }
        }
        catch (e:Exception){
            super.onBackPressed()

        }
    }

    override fun OnRequestItemClick(view: View, position: Int, viewType: Int) {

    }

    override fun OnExpenseItemClick(view: View, position: Int, viewType: Int) {

    }
}
