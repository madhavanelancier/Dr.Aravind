package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.cloudinary.Cloudinary
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.meenatchi_CRM.Appconstands.CheckingPermissionIsEnabledOrNot
import com.elancier.meenatchi_CRM.Appconstands.RequestMultiplePermission
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import kotlinx.android.synthetic.main.customer_add.*
import kotlinx.android.synthetic.main.request_add.address
import kotlinx.android.synthetic.main.request_add.cityspin
import kotlinx.android.synthetic.main.request_add.fname
import kotlinx.android.synthetic.main.request_add.mob
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Create Doctor"

        nmarr = ArrayList<String>()
        idarr = ArrayList<String>()
        citynmarr = ArrayList<String>()
        ctyidarr = ArrayList<String>()

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()

        pDialog = ProgressDialog(this)
        pDialog.setMessage("Processing...")
        pDialog.show()

        types()
        citytypes("")

        pinloc.setOnClickListener {
            startActivityForResult(Intent(this,GetLocation_Admin::class.java),120)
        }

        save.setOnClickListener {
            if (fname.text.toString().trim().isNotEmpty() && hospitalname.text.toString().trim()
                    .isNotEmpty()
                && pinloc.text.toString().trim().isNotEmpty() && address.text.toString().trim()
                    .isNotEmpty()
                && specialization.selectedItemPosition != 0 && statespin.selectedItemPosition != 0
                && hospital_contact.text.toString().trim()
                    .isNotEmpty() && doc_contact.text.toString().trim().isNotEmpty()&&imgCountValid>0
            ) {

                uploadMultiFile()
            } else {
                if (fname.text.toString().trim().isEmpty()) {
                    fname.error = "Required field*"
                }
                if (hospitalname.text.toString().trim().isEmpty()) {
                    hospitalname.error = "Required field*"
                }
                if (pinloc.text.toString().trim().length < 10) {
                    pinloc.error = "Required field*"
                }
                if (address.text.toString().trim().isEmpty()) {
                    address.error = "Required field*"
                }
                if (doc_contact.text.toString().trim().isEmpty()) {
                    doc_contact.error = "Required field*"
                }
                if (specialization.selectedItemPosition == 0) {
                    toast("Select Specialization")
                }
                if (statespin.selectedItemPosition == 0) {
                    toast("Select City")
                }
                if (hospital_contact.text.toString().trim().isEmpty()) {
                    designtion.error = "Required field*"
                }
                if (doc_contact.text.toString().trim().isEmpty()) {
                    designtion.error = "Required field*"
                }
                if(imgCountValid==0){
                    toast("Please select atleast one image")

                }

            }
        }


        cardView3!!.setOnClickListener {
            imgCount=1;
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }

        cardView3two!!.setOnClickListener {
            imgCount=2;
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()

            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }
        cardView3three!!.setOnClickListener {
            imgCount=3;

            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity)
            }

        }

        cardView3four!!.setOnClickListener {
            imgCount=4;

            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity)
            }

        }

        cardView3five!!.setOnClickListener {
            imgCount=5;
            if(CheckingPermissionIsEnabledOrNot(this)){
                selectImage()
            }
            else{
                RequestMultiplePermission(activity)
            }

        }

    }


    fun selectImage(){
        ImagePicker.with(this)
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
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
                val file = File((filePaths[i].toString().removePrefix("file:///")))

                builder.addFormDataPart(
                    "clinic_photo[]",
                    file.getName(),
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
                        startActivity(
                            Intent(
                                this@Customer_Add,
                                Customers_List::class.java
                            )
                        )
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
                        Glide.with(this@Customer_Add).load(image1path.toUri()).placeholder(R.drawable.loading_icon).into(img1)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))
                    } else if (imgCount == 2) {
                        imgCountValid=2
                        image2data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image2path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image2path.toUri()).placeholder(R.drawable.loading_icon).into(img2)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))

                    } else if (imgCount == 3) {
                        imgCountValid=3
                        image3data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image3path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image3path.toUri()).placeholder(R.drawable.loading_icon).into(img3)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))

                    } else if (imgCount == 4) {
                        imgCountValid=4
                        image4data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image4path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image4path.toUri()).placeholder(R.drawable.loading_icon).into(img4)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))

                    } else if (imgCount == 5) {
                        imgCountValid=5
                        image5data = ""//Base64.encodeToString(byteArray, Base64.DEFAULT)
                        image5path = selectedImage.toString()
                        Glide.with(this@Customer_Add).load(image5path.toUri()).placeholder(R.drawable.loading_icon).into(img5)//setImageBitmap(modifyOrientation(BitmapFactory.decodeFile(picturePath),picturePath))//(BitmapFactory.decodeFile(picturePath))

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
                }
            }
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

/* try {
     var frms = intent.extras
     frm = frms!!.getString("cus").toString()

 } catch (e: Exception) {

 }
 if (frm == "customer") {
 } else {
     ab!!.title = "Add Expense"
 }*/
/*

  types()
  cloudinary()

  save.setOnClickListener {
      if(fname.text.toString().trim().isNotEmpty()&&mob.text.toString().trim().isNotEmpty()
          &&address.text.toString().trim().isNotEmpty()&&doc_contact.text.toString().trim().isNotEmpty()
          &&cityspin.selectedItemPosition!=0&&statespin.selectedItemPosition!=0
          &&designtion.text.toString().trim().isNotEmpty()&&mob.text.toString().length==10){

              SendLogin("","","")
      }
      else{
          if(fname.text.toString().trim().isEmpty()){
              fname.error="Required field*"
          }
          if(mob.text.toString().trim().isEmpty()){
              mob.error="Required field*"
          }
          if(mob.text.toString().trim().length<10){
              mob.error="Required field*"
              toast("Invalid Mobile number")
          }
          if(address.text.toString().trim().isEmpty()){
              address.error="Required field*"
          }
          if(doc_contact.text.toString().trim().isEmpty()){
              doc_contact.error="Required field*"
          }
          if(cityspin.selectedItemPosition==0){
              toast("Select City")
          }
          if(statespin.selectedItemPosition==0){
              toast("Select State")
          }
          if(designtion.text.toString().trim().isEmpty()){
              designtion.error="Required field*"
          }
          toast("Please fill required fields")
      }
  }

  cardView3!!.setOnClickListener {
      if(CheckingPermissionIsEnabledOrNot(this)){
          selectImage()
      }
      else{
          RequestMultiplePermission(activity!!)
      }

  }

  cardView3two!!.setOnClickListener {
      if(CheckingPermissionIsEnabledOrNot(this)){
          selectImage1()
      }
      else{
          RequestMultiplePermission(activity!!)
      }

  }
  cardView3three!!.setOnClickListener {

      if(CheckingPermissionIsEnabledOrNot(this)){
          selectImage2()
      }
      else{
          RequestMultiplePermission(activity!!)
      }

  }

  cardView3four!!.setOnClickListener {

      if(CheckingPermissionIsEnabledOrNot(this)){
          selectImage3()
      }
      else{
          RequestMultiplePermission(activity!!)
      }

  }

  cardView3five!!.setOnClickListener {

      if(CheckingPermissionIsEnabledOrNot(this)){
          selectImage4()
      }
      else{
          RequestMultiplePermission(activity!!)
      }

  }

  statespin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
          println("position : "+position)
          //Groupcodes().execute(spindata[position].id)
          if (statespin.selectedItemPosition!=0){
              citytypes(statespin.selectedItem.toString())
          }
      }

      override fun onNothingSelected(parent:AdapterView<*>) {

      }
  }
}




fun SendLogin(mobile:String,url:String,url1:String){
  lateinit var call:Call<Resp_otp>
  lateinit var pDialo:ProgressDialog
  pDialo = ProgressDialog(this@Customer_Add);
  pDialo.setMessage("Saving....");
  pDialo.setIndeterminate(false);
  pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
  pDialo.setCancelable(false);
  //pDialo.setMax(3)
  pDialo.show()
  var image=""


  if(frm=="customer"){
      call = ApproveUtils.Get.getcustentry(pref.getString("tid","").toString(),
         fname.text.toString().trim(),mob.text.toString().trim(),
          address.text.toString().trim(),
         citynmarr[cityspin.selectedItemPosition],nmarr[statespin.selectedItemPosition],image,
          "","data:image/png;base64"+","+imagecode,"data:image/png;base64"+","+imagecode1,
          "data:image/png;base64"+","+imagecode2,"data:image/png;base64"+","+imagecode3,"data:image/png;base64"+","+imagecode4,"")

      val ob = JSONObject()
      ob.put("tid",pref.getString("tid","").toString())
      ob.put("customer_name",fname.text.toString().trim())
      ob.put("mobile",mob.text.toString().trim())
      ob.put("address",address.text.toString().trim())
      ob.put("city",citynmarr[cityspin.selectedItemPosition])
      ob.put("state",nmarr[statespin.selectedItemPosition])
      ob.put("image",image)
      //ob.put("contact_person",contact.text.toString().trim())
      ob.put("designation",designtion.text.toString().trim())
      ob.put("image1","data:image/png;base64"+","+imagecode)
      ob.put("image2","data:image/png;base64"+","+imagecode1)
      ob.put("image3","data:image/png;base64"+","+imagecode2)
      ob.put("image4","data:image/png;base64"+","+imagecode3)
      ob.put("image5","data:image/png;base64"+","+imagecode4)
      Log.d("add customer",ob.toString())
  }
  else{
      call = ApproveUtils.Get.getexpentry(pref.getString("tid","").toString(),
          pref.getString("mobile","").toString(),idarr[cityspin.selectedItemPosition] ,fname!!.text.toString(),address!!.text.toString(),
          image,"",mob.text.toString().trim())
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
                  pDialo.dismiss()
                  finish()
                  //onResume()
              } else {
                  pDialo.dismiss()

                  Toast.makeText(
                      activity,
                      example.message,
                      Toast.LENGTH_LONG
                  ).show()
              }
          }
          //  pDialog.dismiss()

      }

      override fun onFailure(call: Call<Resp_otp>, t: Throwable) {
          Log.e("Fail response", t.toString())
          pDialo.dismiss()

          if (t.toString().contains("time")) {
              Toast.makeText(
                  activity,
                  "Poor network connection",
                  Toast.LENGTH_LONG
              ).show()
          }
          //pDialog.dismiss()

      }
  })


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
//                            textView23.visibility= View.GONE
                      var otpval=example!!.response

                      api_key=otpval!!.api_key.toString()
                      api_secret=otpval!!.api_secret.toString()
                      cloud_name=otpval!!.cloud_name.toString()

                  } else {
                      Toast.makeText(this@Customer_Add, example.status, Toast.LENGTH_SHORT)
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
                      this@Customer_Add,
                      "Poor network connection",
                      Toast.LENGTH_LONG
                  ).show()
              }
          }
      })
  }
  else{
      Toast.makeText(
          this@Customer_Add,
          "You're offline",
          Toast.LENGTH_LONG
      ).show()

  }
}

public fun selectImage() {
  val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
  val builder = AlertDialog.Builder(activity)
  builder.setTitle("Add Photo!")
  builder.setItems(options) { dialog, item ->
      if (options[item] == "Take Photo") {
          val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          startActivityForResult(intent, 102)
      } else if (options[item] == "Choose from Gallery") {
          val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
          val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          startActivityForResult(intent, 103)
      } else if (options[item] == "Choose from Gallery") {
          val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          i.type = "image/*"
          startActivityForResult(i, 2)
      } else if (options[item] == "Cancel") {
          dialog.dismiss()
      }
  }
  builder.show()
}

public fun selectImage2() {
  val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
  val builder = AlertDialog.Builder(activity)
  builder.setTitle("Add Photo!")
  builder.setItems(options) { dialog, item ->
      if (options[item] == "Take Photo") {
          val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          startActivityForResult(intent, 104)
      } else if (options[item] == "Choose from Gallery") {
          val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          i.type = "image/*"
          startActivityForResult(i, 3)
      } else if (options[item] == "Cancel") {
          dialog.dismiss()
      }
  }
  builder.show()
}
public fun selectImage3() {
  val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
  val builder = AlertDialog.Builder(activity)
  builder.setTitle("Add Photo!")
  builder.setItems(options) { dialog, item ->
      if (options[item] == "Take Photo") {
          val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          startActivityForResult(intent, 105)
      } else if (options[item] == "Choose from Gallery") {
          val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          i.type = "image/*"
          startActivityForResult(i, 4)
      } else if (options[item] == "Cancel") {
          dialog.dismiss()
      }
  }
  builder.show()
}
public fun selectImage4() {
  val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
  val builder = AlertDialog.Builder(activity)
  builder.setTitle("Add Photo!")
  builder.setItems(options) { dialog, item ->
      if (options[item] == "Take Photo") {
          val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          startActivityForResult(intent, 106)
      } else if (options[item] == "Choose from Gallery") {
          val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          i.type = "image/*"
          startActivityForResult(i, 5)
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
  // Log.i("utilsresult", !! result+ "")
  return result

}
fun saveImage(myBitmap: Bitmap): String {
  val bytes = ByteArrayOutputStream()
  myBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
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
                  val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
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

                  img1!!.setImageBitmap(resizeBitmap)
                  cardView3two.visibility=View.VISIBLE

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
                  img1!!.setImageBitmap(resizeBitmap)
                  cardView3two.visibility=View.VISIBLE

                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray = stream.toByteArray()
                  imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                  val path = getImgPath(selectedImageUri!!)
                  if (path != null) {
                      val f = File(path!!)
                      selectedImageUri = Uri.fromFile(f)
                  }
              } catch (e: Exception) {
                  val thumbnail = data!!.extras!!.get("data") as Bitmap?
                  val stream = ByteArrayOutputStream()
                  thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray = stream.toByteArray()
                  imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                  saveImage(thumbnail)
                  img1!!.setImageBitmap(thumbnail)
                  cardView3two.visibility=View.VISIBLE
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
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImage
                  )
                  val resizeBitmap =
                      resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                //  imageView10!!.setImageBitmap(resizeBitmap)
                  img2!!.setImageBitmap(resizeBitmap)
                  cardView3three.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray1 = stream.toByteArray()
                  imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                  Log.e("imagecode", imagecode1)
                  val path = getImgPath(selectedImage!!)
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
                  //imageView10!!.setImageBitmap(resizeBitmap)
                  img2!!.setImageBitmap(resizeBitmap)
                  cardView3three.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray1 = stream.toByteArray()
                  imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                  val path = getImgPath(selectedImageUri!!)
                  if (path != null) {
                      val f = File(path!!)
                      selectedImageUri = Uri.fromFile(f)

                  }
              } catch (e: Exception) {
                  val thumbnail = data!!.extras!!.get("data") as Bitmap?
                  val stream = ByteArrayOutputStream()
                  thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray1 = stream.toByteArray()
                  imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                  saveImage(thumbnail)
                  img2!!.setImageBitmap(thumbnail)
                  cardView3three.visibility=View.VISIBLE
                 // imageView10!!.setImageBitmap(thumbnail)
              }

          }

          else if (requestCode ==3) {
              if (resultCode == Activity.RESULT_OK && null != data) {
                  var picturePath: String? = null
                  var selectedImage = data!!.data
                  picturePath = getImgPath(selectedImage)
                  fi2 = File(picturePath!!)
                  val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
                  //Log.i("original path1", picturePath + "")
                  Log.i(
                      "pathsizeeeeee",
                      (fi2.length() / 1024).toString() + "      " + yourSelectedImage
                  )
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImage
                  )
                  val resizeBitmap =
                      resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                  //  imageView10!!.setImageBitmap(resizeBitmap)
                  img3!!.setImageBitmap(resizeBitmap)
                  cardView3four.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray2 = stream.toByteArray()
                  imagecode2 = Base64.encodeToString(byteArray2, Base64.DEFAULT)
                  Log.e("imagecode", imagecode2)
                  val path = getImgPath(selectedImage!!)
                  if (path != null) {
                      val f = File(path!!)
                  }
              } else {

              }
          } else if (requestCode == 104) {
              try {
                  var selectedImageUri = data!!.data
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImageUri
                  )
                  val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                  //imageView10!!.setImageBitmap(resizeBitmap)
                  img3!!.setImageBitmap(resizeBitmap)
                  cardView3four.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray2 = stream.toByteArray()
                  imagecode2 = Base64.encodeToString(byteArray2, Base64.DEFAULT)
                  val path = getImgPath(selectedImageUri!!)
                  if (path != null) {
                      val f = File(path!!)
                      selectedImageUri = Uri.fromFile(f)

                  }
              } catch (e: Exception) {
                  val thumbnail = data!!.extras!!.get("data") as Bitmap?
                  val stream = ByteArrayOutputStream()
                  thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray2 = stream.toByteArray()
                  imagecode2 = Base64.encodeToString(byteArray2, Base64.DEFAULT)
                  saveImage(thumbnail)
                  img3!!.setImageBitmap(thumbnail)
                  cardView3four.visibility=View.VISIBLE
                  // imageView10!!.setImageBitmap(thumbnail)
              }

          }

          else if (requestCode ==4) {
              if (resultCode == Activity.RESULT_OK && null != data) {
                  var picturePath: String? = null
                  var selectedImage = data!!.data
                  picturePath = getImgPath(selectedImage)
                  fi3= File(picturePath!!)
                  val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
                  //Log.i("original path1", picturePath + "")
                  Log.i(
                      "pathsizeeeeee",
                      (fi3.length() / 1024).toString() + "      " + yourSelectedImage
                  )
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImage
                  )
                  val resizeBitmap =
                      resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                  //  imageView10!!.setImageBitmap(resizeBitmap)
                  img4!!.setImageBitmap(resizeBitmap)
                  cardView3five.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray3 = stream.toByteArray()
                  imagecode3 = Base64.encodeToString(byteArray3, Base64.DEFAULT)
                  Log.e("imagecode", imagecode3)
                  val path = getImgPath(selectedImage!!)
                  if (path != null) {
                      val f = File(path!!)
                  }
              } else {

              }
          } else if (requestCode == 105) {
              try {
                  var selectedImageUri = data!!.data
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImageUri
                  )
                  val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                  //imageView10!!.setImageBitmap(resizeBitmap)
                  img4!!.setImageBitmap(resizeBitmap)
                  cardView3five.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray3 = stream.toByteArray()
                  imagecode3 = Base64.encodeToString(byteArray3, Base64.DEFAULT)
                  val path = getImgPath(selectedImageUri!!)
                  if (path != null) {
                      val f = File(path!!)
                      selectedImageUri = Uri.fromFile(f)

                  }
              } catch (e: Exception) {
                  val thumbnail = data!!.extras!!.get("data") as Bitmap?
                  val stream = ByteArrayOutputStream()
                  thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray3 = stream.toByteArray()
                  imagecode3 = Base64.encodeToString(byteArray3, Base64.DEFAULT)
                  saveImage(thumbnail)
                  img4!!.setImageBitmap(thumbnail)
                  cardView3five.visibility=View.VISIBLE
                  // imageView10!!.setImageBitmap(thumbnail)
              }

          }

          else if (requestCode ==5) {
              if (resultCode == Activity.RESULT_OK && null != data) {
                  var picturePath: String? = null
                  var selectedImage = data!!.data
                  picturePath = getImgPath(selectedImage)
                  fi4= File(picturePath!!)
                  val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)
                  //Log.i("original path1", picturePath + "")
                  Log.i(
                      "pathsizeeeeee",
                      (fi4.length() / 1024).toString() + "      " + yourSelectedImage
                  )
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImage
                  )
                  val resizeBitmap =
                      resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                  //  imageView10!!.setImageBitmap(resizeBitmap)
                  img5!!.setImageBitmap(resizeBitmap)
                  cardView3five.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray4 = stream.toByteArray()
                  imagecode4 = Base64.encodeToString(byteArray4, Base64.DEFAULT)
                  Log.e("imagecode", imagecode4)
                  val path = getImgPath(selectedImage!!)
                  if (path != null) {
                      val f = File(path!!)
                  }
              } else {

              }
          } else if (requestCode == 106) {
              try {
                  var selectedImageUri = data!!.data
                  val bitmap = MediaStore.Images.Media.getBitmap(
                      activity!!.getBaseContext().getContentResolver(),
                      selectedImageUri
                  )
                  val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                  //imageView10!!.setImageBitmap(resizeBitmap)
                  img5!!.setImageBitmap(resizeBitmap)
                  cardView3five.visibility=View.VISIBLE
                  val stream = ByteArrayOutputStream()
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray4 = stream.toByteArray()
                  imagecode4 = Base64.encodeToString(byteArray4, Base64.DEFAULT)
                  val path = getImgPath(selectedImageUri!!)
                  if (path != null) {
                      val f = File(path!!)
                      selectedImageUri = Uri.fromFile(f)

                  }
              } catch (e: Exception) {
                  val thumbnail = data!!.extras!!.get("data") as Bitmap?
                  val stream = ByteArrayOutputStream()
                  thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                  byteArray4 = stream.toByteArray()
                  imagecode4 = Base64.encodeToString(byteArray4, Base64.DEFAULT)
                  saveImage(thumbnail)
                  img5!!.setImageBitmap(thumbnail)
                  cardView3five.visibility=View.VISIBLE
                  // imageView10!!.setImageBitmap(thumbnail)
              }
          }
      }
  }
  catch (e:Exception){
  }
}



}*/