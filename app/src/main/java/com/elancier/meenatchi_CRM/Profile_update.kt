package com.elancier.meenatchi_CRM

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cloudinary.Cloudinary
import com.elancier.domdox.Common.CommonFunctions
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import kotlinx.android.synthetic.main.profile_update.*
import kotlinx.android.synthetic.main.request_add.address
import kotlinx.android.synthetic.main.request_add.cityspin
import kotlinx.android.synthetic.main.request_add.fname
import kotlinx.android.synthetic.main.request_add.mob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Profile_update:AppCompatActivity() {

    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    var CentresArrays = ArrayList<OrderDetail>()
    val RequestPermissionCode = 7
    internal lateinit var byteArray: ByteArray
    var imagecode=""
    internal lateinit var fi: File
    var panimage=""
    var bankimage=""
    internal lateinit var byteArray1: ByteArray
    var imagecode1=""
    internal lateinit var fi1: File
    lateinit var pDialog: ProgressDialog
    internal var nmarr: MutableList<String> = ArrayList()
    internal var citynmarr: MutableList<String> = ArrayList()
    internal var ctyidarr: MutableList<String> = ArrayList()
    internal var idarr: MutableList<String> = ArrayList()
    val activity=this
    var api_key="";
    var api_secret="";
    var cloud_name="";
    var frm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_update)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        try {
            var frms = intent.extras
            frm = frms!!.getString("cus").toString()

        } catch (e: Exception) {

        }

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)

        if (frm == "customer") {
            ab!!.title = "Profile Update"
        } else {
            ab!!.title = "Add Expense"
        }
        nmarr=ArrayList<String>()
        idarr=ArrayList<String>()
        citynmarr=ArrayList<String>()
        ctyidarr=ArrayList<String>()
        pref = applicationContext.getSharedPreferences("MyPref",0)
        editor = pref.edit()

        fname.setText(pref.getString("fname",""))
        mob.setText(pref.getString("lname",""))
        address.setText(pref.getString("mobile",""))
        email.setText(pref.getString("email",""))

        save.setOnClickListener {
            if(fname.text.toString().trim().isNotEmpty()&&mob.text.toString().trim().isNotEmpty()
                &&address.text.toString().trim().isNotEmpty()&&email.text.toString().trim().isNotEmpty()
                ){

                    SendLogin("","","")

            }
            else{
                if(fname.text.toString().trim().isEmpty()){
                    fname.error="Required field*"
                }
                if(mob.text.toString().trim().isEmpty()){
                    mob.error="Required field*"
                }
                if(address.text.toString().trim().isEmpty()){
                    address.error="Required field*"
                }
                if(email.text.toString().trim().isEmpty()){
                    email.error="Required field*"
                }

                toast("Please fill required fields")
            }
        }

  /*      imageone!!.setOnClickListener {

            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                selectImage()

            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }*/

        /*imageView10!!.setOnClickListener {

            if(CheckingPermissionIsEnabledOrNot(this)&&(panimage.isEmpty()||panimage=="null")){

                selectImage1()


            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }*/
        /*statespin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (statespin.selectedItemPosition!=0){
                    citytypes(statespin.selectedItem.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }*/



    }


    inner class Get : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo: ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            runOnUiThread {

                pDialo = ProgressDialog(this@Profile_update);
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
            val k =""
            val k1 =""
            try {
                if(imagecode.isNotEmpty()) {
                    val fi = cloudinary.uploader()
                        .upload("data:image/png;base64" + "," + imagecode, HashMap<Any, Any>());
                    fi.get("url")
                }
                if(imagecode1.isNotEmpty()){
                    val fis = cloudinary.uploader().upload("data:image/png;base64"+","+imagecode1, HashMap<Any, Any>());
                    fis.get("url")

                }

                Log.e("fival", k.toString());

                runOnUiThread{
                    pDialo.dismiss()
                    SendLogin("",k,k1)
                }

                //finish()


            } catch (e: IOException) {
                e.printStackTrace();
                Log.e("except",e.toString())
            }

            return result
        }

        override fun onPostExecute(resp: String?) {

        }
    }


    fun toast(msg:String){
        val t=Toast.makeText(this,msg,Toast.LENGTH_LONG)
        t.setGravity(Gravity.CENTER,0,0)
        t.show()
    }

    fun SendLogin(mobile:String,url:String,url1:String){
        lateinit var call:Call<Resp_otps>
        lateinit var pDialo:ProgressDialog
        pDialo = ProgressDialog(this@Profile_update);
        pDialo.setMessage("Saving....");
        pDialo.setIndeterminate(false);
        pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialo.setCancelable(false);
        //pDialo.setMax(3)
        pDialo.show()
        var image=""
        if(url.isNotEmpty()&&url1.isNotEmpty()){
            image=url+","+url1
        }
        else if(url.isEmpty()&&url1.isNotEmpty()){
            image=url1
        }
        else if(url.isNotEmpty()&&url1.isEmpty()){
            image=url
        }

        if(frm=="customer"){
            call = ApproveUtils.Get.getprofile(address.text.toString().trim(),fname.text.toString().trim(),
                mob.text.toString().trim(),email.text.toString().trim())
        }
        else{

        }

        call.enqueue(object : Callback<Resp_otps> {
            override fun onResponse(call: Call<Resp_otps>, response: Response<Resp_otps>) {
                Log.e("response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp_otps
                    println(example)
                    if (example.status == "Success") {
                        var arr=example.message
                        toast(arr.toString())
                        pDialo.dismiss()
                        editor!!.putString("mobile", address.text.toString().trim())
                        editor!!.putString("fname", fname.text.toString().trim())
                        editor!!.putString("lname", mob.text.toString().trim())
                        editor!!.putString("email", email.text.toString().trim())
                        editor!!.commit()
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

            override fun onFailure(call: Call<Resp_otps>, t: Throwable) {
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

    /*fun types(){
        if (Appconstants.net_status(this)) {
            nmarr.clear()
            idarr.clear()
            val call:Call<Resp_trip>
            if(frm=="customer"){
                call = ApproveUtils.Get.getstates(pref.getString("mobile", "").toString())
            }
            else{
                call = ApproveUtils.Get.getexptype()

            }
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            if(frm=="customer"){
                                nmarr.add("Select State")
                            }
                            else{
                                nmarr.add("Expense Type")

                            }
                            idarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()

                            for(i in 0 until otpval!!.size) {
                                val id=otpval[i].id.toString()
                                val type=otpval[i].state_name.toString()
                                nmarr.add(type)
                                idarr.add(id)

                            }
                            val spin=ArrayAdapter(this@Profile_update,R.layout.support_simple_spinner_dropdown_item,nmarr)
                            //statespin.adapter=spin
                            pDialog.dismiss()

                        } else {
                            *//*Toast.makeText(this@Profile_update, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*//*
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Profile_update,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()



                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Profile_update,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }*/

    /*fun citytypes(state:String){
        if (Appconstants.net_status(this)) {
            citynmarr.clear()
            ctyidarr.clear()
            val call:Call<Resp_trip>
            if(frm=="customer"){
                call = ApproveUtils.Get.getcities(state)
            }
            else{
                call = ApproveUtils.Get.getexptype()

            }
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>,response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {

                            if(frm=="customer"){
                                citynmarr.add("Select City")
                            }
                            else{
                                //nmarr.add("Expense Type")

                            }
                            ctyidarr.add("0")
                            //  textView23.visibility= View.GONE
                            var otpval=example.getResponse()
                            for(i in 0 until otpval!!.size) {
                                val id=otpval[i].id.toString()
                                val type=otpval[i].city_name.toString()
                                citynmarr.add(type)
                                ctyidarr.add(id)

                            }
                            val spin=ArrayAdapter(this@Profile_update,R.layout.support_simple_spinner_dropdown_item,citynmarr)
                            cityspin.adapter=spin
                            pDialog.dismiss()

                        } else {
                            *//*Toast.makeText(this@Profile_update, example.getStatus(), Toast.LENGTH_SHORT)
                                .show()*//*
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Profile_update,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Profile_update,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }*/

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
                            Toast.makeText(this@Profile_update, example.status, Toast.LENGTH_SHORT)
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
                            this@Profile_update,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Profile_update,
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

    fun getImgPath(uri:Uri?):String? {
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

                        //imageone!!.setImageBitmap(resizeBitmap)
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
                       // imageone!!.setImageBitmap(resizeBitmap)
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
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
                        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        saveImage(thumbnail)
                      //  imageone!!.setImageBitmap(thumbnail)
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
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
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
                        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                        byteArray1 = stream.toByteArray()
                        imagecode1 = Base64.encodeToString(byteArray1, Base64.DEFAULT)
                        saveImage(thumbnail)
                       // imageView10!!.setImageBitmap(thumbnail)
                    }

                }
            }
        }
        catch(e:Exception){
        }
    }


}