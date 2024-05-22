package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.Appconstands.CheckingPermissionIsEnabledOrNot
import com.elancier.meenatchi_CRM.Appconstands.RequestMultiplePermission
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import com.elancier.meenatchi_CRM.service.MediaUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.customer_add.*
import kotlinx.android.synthetic.main.request_add.address
import kotlinx.android.synthetic.main.request_add.fname
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class Customer_Add : AppCompatActivity() {

    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    var CentresArrays = ArrayList<OrderDetail>()
    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    internal lateinit var fi: File
    internal lateinit var fi1: File
    internal lateinit var fi2: File
    internal lateinit var fi3: File
    internal lateinit var fi4: File

    var panimage = ""
    var bankimage = ""
    internal lateinit var byteArray1: ByteArray
    internal lateinit var byteArray2: ByteArray
    internal lateinit var byteArray3: ByteArray
    internal lateinit var byteArray4: ByteArray
    internal lateinit var byteArray5: ByteArray
    var imagecode = ""
    var imagecode1 = ""
    var imagecode2 = ""
    var imagecode3 = ""
    var imagecode4 = ""
    lateinit var pDialog: ProgressDialog
    internal var nmarr: MutableList<String> = ArrayList()
    internal var citynmarr: MutableList<String> = ArrayList()
    internal var ctyidarr: MutableList<String> = ArrayList()
    internal var idarr: MutableList<String> = ArrayList()
    val activity = this
    var api_key = "";
    var api_secret = "";
    var cloud_name = "";
    var frm = ""
    var imgCount = 0
    var imgCountValid = 0
    var latistr = ""
    var longstr = ""
    var image1path = ""
    var image2path = ""
    var image3path = ""
    var image4path = ""
    var image5path = ""

    var image1data = ""
    var image2data = ""
    var image3data = ""
    var image4data = ""
    var image5data = ""
    var lat = 0.0
    var longi = 0.0
    var from=""
    var editID=""
    var editImages=kotlin.collections.ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)

        nmarr = ArrayList<String>()
        idarr = ArrayList<String>()
        citynmarr = ArrayList<String>()
        ctyidarr = ArrayList<String>()

        from=intent.extras!!.getString("from").toString()
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()

        pDialog = ProgressDialog(this)
        pDialog.setMessage("Processing...")
        pDialog.show()

        types()
        citytypes("")

        if(from=="Edit"){
            editID=intent.extras!!.getString("id").toString()
            save.visibility=View.INVISIBLE
            getView()
            ab!!.title = "View Doctor"


        }
        else{
            ab!!.title = "Add Doctor"

        }

        specialization.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if(position!=0){
                    specErr.visibility=View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })

        statespin.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if(position!=0){
                    cityErr.visibility=View.GONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        })

        pinloc.setOnClickListener {
            startActivityForResult(Intent(this,GetLocation_Admin::class.java),120)
        }

        save.setOnClickListener {
            if (fname.text.toString().trim().isNotEmpty() && hospitalname.text.toString().trim()
                    .isNotEmpty()
                && pinloc.text.toString().trim().isNotEmpty() && address.text.toString().trim()
                    .isNotEmpty()
                && specialization.selectedItemPosition != 0 && statespin.selectedItemPosition != 0
                && (hospital_contact.text.toString().length==10) && (doc_contact.text.toString().length==10)&&imgCountValid>0
                ) {

                uploadMultiFile()
            } else {
                if (fname.text.toString().trim().isEmpty()) {
                    fname.error = "Required field*"
                }
                if (hospitalname.text.toString().trim().isEmpty()) {
                    hospitalname.error = "Required field*"
                }
                if (pinloc.text.toString().trim().isEmpty()) {
                    pinloc.error = "Required field*"
                }
                if (address.text.toString().trim().isEmpty()) {
                    address.error = "Required field*"
                }

                if (doc_contact.text.toString().length<10) {
                    doc_contact.error = "Invalid Mobile Number*"
                }
                if (specialization.selectedItemPosition == 0) {
                    toast("Select Specialization")
                    specErr.visibility=View.VISIBLE
                }
                if (statespin.selectedItemPosition == 0) {
                    toast("Select City")
                    cityErr.visibility=View.VISIBLE

                }

                if (hospital_contact.text.toString().length<10) {
                    hospital_contact.error = "Invalid Mobile Number*"
                }

                if(imgCountValid==0){
                    toast("Please select atleast one image")
                    imageErr.visibility=View.VISIBLE

                }

            }
        }


        cardView3!!.setOnClickListener {
            imgCount=1;
            if(CheckingPermissionIsEnabledOrNot(this)){
                if(from=="Add") {
                    selectImage()
                }
                else{
                    startActivity(Intent(this,FullScreen::class.java)
                        .putExtra("from",hospitalname.text.toString())
                        .putExtra("image",editImages[0]))

                }
            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }

        cardView3two!!.setOnClickListener {
            imgCount=2;
            if(CheckingPermissionIsEnabledOrNot(this)){
                if(from=="Add"){
                selectImage()
                }
                else{
                    startActivity(Intent(this,FullScreen::class.java)
                        .putExtra("from",hospitalname.text.toString())
                        .putExtra("image",editImages[1]))

                }

            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }
        cardView3three!!.setOnClickListener {
            imgCount=3;

            if(CheckingPermissionIsEnabledOrNot(this)){
                if(from=="Add"){
                    selectImage()
                }
                else{
                    startActivity(Intent(this,FullScreen::class.java)
                        .putExtra("from",hospitalname.text.toString())
                        .putExtra("image",editImages[2]))

                }
            }
            else{
                RequestMultiplePermission(activity)
            }

        }

        cardView3four!!.setOnClickListener {
            imgCount=4;

            if(CheckingPermissionIsEnabledOrNot(this)){
                if(from=="Add"){
                    selectImage()
                }
                else{
                    startActivity(Intent(this,FullScreen::class.java)
                        .putExtra("from",hospitalname.text.toString())
                        .putExtra("image",editImages[3]))
                }
            }
            else{
                RequestMultiplePermission(activity)
            }

        }

        cardView3five!!.setOnClickListener {
            imgCount=5;
            if(CheckingPermissionIsEnabledOrNot(this)){
                if(from=="Add"){
                    selectImage()
                }
                else{
                    startActivity(Intent(this,FullScreen::class.java)
                        .putExtra("from",hospitalname.text.toString())
                        .putExtra("image",editImages[4]))
                }
            }
            else{
                RequestMultiplePermission(activity)
            }

        }

    }


    fun selectImage(){
        ImagePicker.with(this)
            .saveDir(getExternalFilesDir(null)!!)
            //.crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }



    private fun uploadMultiFile() {
        var progressDialog =  ProgressDialog(this);
        progressDialog.setMessage("Creating Doctor...");
        progressDialog.show()
        val filePaths: java.util.ArrayList<String> = java.util.ArrayList()
        if(image1path.isNotEmpty()){
            filePaths.add(image1path)
        }
        if(image2path.isNotEmpty()){
            filePaths.add(image2path)
        }
        if(image3path.isNotEmpty()){
            filePaths.add(image3path)
        }
        if(image4path.isNotEmpty()){
            filePaths.add(image4path)
        }
        if(image5path.isNotEmpty()){
            filePaths.add(image5path)
        }

        if(filePaths.size>0) {
            Log.e("filepaths",filePaths.toString())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("name", fname.text.toString())
            builder.addFormDataPart("employee", pref!!.getString("empid","").toString())
            builder.addFormDataPart("specialization", idarr[specialization.selectedItemPosition].toString())
            builder.addFormDataPart("clinic", hospitalname.text.toString())
            builder.addFormDataPart("address", address.text.toString())
            builder.addFormDataPart("city", ctyidarr[statespin.selectedItemPosition].toString())
            builder.addFormDataPart("doctor_contact_number",doc_contact.text.toString())
            builder.addFormDataPart("hospital_contact_number",hospital_contact.text.toString())
            builder.addFormDataPart("location_pin",pinloc.text.toString())
            builder.addFormDataPart("lat",latistr)
            builder.addFormDataPart("long",longstr)

            for (i in 0 until filePaths.size) {
                Log.e("fileap",filePaths[i].toString())
                var file:File?=null
                try{
                     file = File(MediaUtils.getPath(this,filePaths[i].toUri()))
                }
                catch (e:Exception){
                     file = File((filePaths[i].toString().removePrefix("file:///")))
                }

                builder.addFormDataPart(
                    "clinic_photo[]",
                    file!!.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
            // Map is used to multipart the file using okhttp3.RequestBody
            // Multiple Images

            val file = File("")
            val requestBody = builder.build()
            Log.e("request",requestBody.parts().toString())
            val call: Call<ResponseBody?>? = ApproveUtils.Get.addDoctor(requestBody)
            call!!.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    Log.w(
                        "json",
                        Gson().toJson(response)
                    )
                    if(response.isSuccessful) {
                        Toast.makeText(activity, "Doctor Added Successfully", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                    else{
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT)

                    }


                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d("failure", "Error " + t.message)
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT)

                }
            })
        }
        else{
            Toast.makeText(applicationContext,"Atleast select 1 image", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2404) {
            if (resultCode != RESULT_CANCELED) {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val selectedImage: Uri? = data.data

                    /*val myBitmap = Uri.fromFile(File(picturePath))
            val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myBitmap)
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()*/;
                    if (imgCount == 1) {
                        imgCountValid=1
                        image1data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image1path = selectedImage.toString()
                        imageErr.visibility=View.GONE
                        Log.e("image1",image1path)
                        Glide.with(this@Customer_Add).load(image1path.toUri()).placeholder(R.drawable.loading_icon).into(img1)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                    } else if (imgCount == 2) {
                        imgCountValid=2
                        image2data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image2path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image2path.toUri()).placeholder(R.drawable.loading_icon).into(img2)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                        imageErr.visibility=View.GONE

                    } else if (imgCount == 3) {
                        imgCountValid=3
                        image3data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image3path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image3path.toUri()).placeholder(R.drawable.loading_icon).into(img3)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                        imageErr.visibility=View.GONE

                    } else if (imgCount == 4) {
                        imgCountValid=4
                        image4data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image4path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image4path.toUri()).placeholder(R.drawable.loading_icon).into(img4)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                        imageErr.visibility=View.GONE

                    } else if (imgCount == 5) {
                        imgCountValid=5
                        image5data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image5path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image5path.toUri()).placeholder(R.drawable.loading_icon).into(img5)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                        imageErr.visibility=View.GONE

                    }
                    //println("image Data : "+Base64.encodeToString(byteArray, Base64.DEFAULT))
                }
            }
        }
        if(requestCode==120){
            if (resultCode != RESULT_CANCELED) {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    latistr=data.getStringExtra("lattitude").toString()
                    longstr=data.getStringExtra("longitude").toString()
                    address.setText(data.getStringExtra("location").toString())
                    pinloc.setText(data.getStringExtra("pincode").toString())
                    pinloc.setError(null)
                    address.setError(null)
                }
            }
        }
    }


    fun getView() {
        if (Appconstants.net_status(this)) {

            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getDoctor(editID)
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                fname.setText(otpval[i].name.toString())
                                fname.setFocusable(false)

                                if(from=="Edit"){
                                    for(j in 0 until idarr.size){
                                        if(idarr[j]==otpval[i].specialization){
                                            specialization.setSelection(j)
                                            specialization.setEnabled(false)
                                        }
                                    }

                                    for(k in 0 until ctyidarr.size){
                                        if(ctyidarr[k]==otpval[i].city){
                                            statespin.setSelection(k)
                                            statespin.setEnabled(false)
                                        }
                                    }
                                }

                                hospitalname.setText(otpval[i].clinic)
                                hospitalname.setFocusable(false)

                                pinloc.setText(otpval[i].location_pin)
                                pinloc.setEnabled(false)

                                address.setText(otpval[i].address)
                                address.setFocusable(false)

                                doc_contact.setText(otpval[i].doctor_contact_number)
                                doc_contact.setFocusable(false)

                                hospital_contact.setText(otpval[i].hospital_contact_number)
                                hospital_contact.setFocusable(false)

                                editImages= otpval[i].clinic_photo!!

                                try{
                                    Glide.with(this@Customer_Add).load(editImages[0]).placeholder(R.drawable.loading_icon).into(img1)
                                }
                                catch (e:Exception){
                                    cardView3.setEnabled(false)

                                }

                                try{
                                    Glide.with(this@Customer_Add).load(editImages[1]).placeholder(R.drawable.loading_icon).into(img2)

                                }
                                catch (e:Exception){
                                    cardView3two.setEnabled(false)

                                }

                                try{
                                    Glide.with(this@Customer_Add).load(editImages[2]).placeholder(R.drawable.loading_icon).into(img3)

                                }
                                catch (e:Exception){
                                    cardView3three.setEnabled(false)

                                }

                                try{
                                    Glide.with(this@Customer_Add).load(editImages[3]).placeholder(R.drawable.loading_icon).into(img4)

                                }
                                catch (e:Exception){
                                    cardView3four.setEnabled(false)

                                }

                                try{
                                    Glide.with(this@Customer_Add).load(editImages[4]).placeholder(R.drawable.loading_icon).into(img5)

                                }
                                catch (e:Exception){
                                    cardView3five.setEnabled(false)

                                }


                            }
                            pDialog.dismiss()

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }


    fun types() {
        if (Appconstants.net_status(this)) {
            nmarr.clear()
            idarr.clear()
            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getspecialized()
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            nmarr.add("Select Specialization")
                            idarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                nmarr.add(type)
                                idarr.add(id)



                            }
                            val spin = ArrayAdapter(
                                this@Customer_Add,
                                R.layout.support_simple_spinner_dropdown_item,
                                nmarr
                            )
                            specialization.adapter = spin

                            pDialog.dismiss()

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun citytypes(state: String) {
        if (Appconstants.net_status(this)) {
            citynmarr.clear()
            ctyidarr.clear()
            val call: Call<Resp_trip>
            call = ApproveUtils.Get.getcities()
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            citynmarr.add("Select City")
                            ctyidarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval = example.getResponse()
                            for (i in 0 until otpval!!.size) {
                                val id = otpval[i].id.toString()
                                val type = otpval[i].name.toString()
                                citynmarr.add(type)
                                ctyidarr.add(id)

                            }
                            val spin = ArrayAdapter(
                                this@Customer_Add,
                                R.layout.support_simple_spinner_dropdown_item,
                                citynmarr
                            )
                            statespin.adapter = spin
                            pDialog.dismiss()

                        } else {
                            toast(example.getMessage().toString())
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Customer_Add,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
            })
        } else {
            Toast.makeText(
                this@Customer_Add,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun toast(msg: String) {
        val t = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        t.setGravity(Gravity.CENTER, 0, 0)
        t.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home -> {

                exit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        exit()
    }

    fun exit(){
        val alert=androidx.appcompat.app.AlertDialog.Builder(this)
        alert.setTitle("Exit?")
        alert.setMessage("Are you sure want to exit?")
        alert.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
            finish()
        })
        alert.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val popup=alert.create()
        popup.show()
    }
}

