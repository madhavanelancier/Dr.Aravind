package com.elancier.meenatchi_CRM.Family

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.usb.UsbManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.elancier.meenatchi_CRM.Adapers.*
import com.elancier.meenatchi_CRM.Appconstands
import com.elancier.meenatchi_CRM.DataClasses.VerificationData
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.Utils
import com.elancier.domdox.Common.CommonFunctions


import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class AuthenticationFragment : Fragment() {
    lateinit var verificationData: VerificationData
    lateinit var handler: Handler
    var USBstatus = 0
    var changepos = ""

    internal lateinit var byteArray: ByteArray
    var imagecode=""
    var utrimagecode=""
    internal lateinit var fi: File

    internal val USBATTACH: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                //showDevices();
                USBstatus = 1
                println("device attached")
                Toast.makeText(activity!!, "Device Attached", Toast.LENGTH_LONG).show()
                /*mHoinPrinter= USBClass(this@Dashboard, this@Dashboard)
                mHoinPrinter.connect(this@Dashboard)*/
                //Fingerprint().scan(this@Dashboard, Handler())

            }

        }
    }
    /*internal val USBDETACH: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                val device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice
                println("devide detached")
                Toast.makeText(activity!!, "Device Detached", Toast.LENGTH_LONG).show()
                USBstatus = 0
                if (device != null) {
                    // call your method that cleans up and closes communication with the device
                    *//* val binder = mHashMap.get(device)
                     if (binder != null) {
                         binder!!.onDestroy()
                         mHashMap.remove(device)
                     }*//*
                }
            }
        }
    }*/



    val RequestPermissionCode = 7
    var which = 0

    var single: File? = null
    val RESULT_LOAD_IMAGE1 = 1
    val REQUEST_IMAGE_CAPTURE = 2
    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    internal var profimage = ""
    var singlePath = ""
    var singleimagecode = ""

    var couple: File? = null
    var couplePath = ""
    var coupleimagecode = ""

    lateinit var rootView: View
    var fingersingleimagecode = ""
    var fingercoupleimagecode = ""

    lateinit var single_image: ImageView
    lateinit var couple_image: ImageView
    lateinit var ap_finger: ImageView
    lateinit var ap_verified: ImageView
    lateinit var co_finger: ImageView
    lateinit var co_verified: ImageView
    lateinit var single_camera: ImageButton
    lateinit var couple_camera: ImageButton
    lateinit var complete: Button
    lateinit var utils: Utils
    lateinit var familyimg: Button
    lateinit var houseimg: Button
    lateinit var unitimg: Button

    lateinit var familylist:RecyclerView
    lateinit var houselist:RecyclerView
    lateinit var unitlist:RecyclerView

    val familyimgarr=ArrayList<Bitmap>()
    val familyimgarr_code=ArrayList<String>()

    val houseimgarr=ArrayList<Bitmap>()
    val houseimgarr_code=ArrayList<String>()

    val unitimgarr=ArrayList<Bitmap>()
    val unitimgarr_code=ArrayList<String>()


    lateinit var click : MyImageAdap.OnItemClickListener
    lateinit var adp : MyImageAdap

    lateinit var clickedit : MyImageAdap_Edit.OnItemClickListener
    lateinit var adpedit : MyImageAdap_Edit
    internal  var pref: SharedPreferences?=null

    internal  var editor: SharedPreferences.Editor?=null


    lateinit var click1 : MyhouseAdap.OnItemClickListener
    lateinit var adp1 : MyhouseAdap

    lateinit var click1edit : MyhouseAdap_Edit.OnItemClickListener
    lateinit var adp1edit : MyhouseAdap_Edit

    lateinit var click2 : MyUnitAdap.OnItemClickListener
    lateinit var adp2 : MyUnitAdap

    lateinit var click2edit : MyUnitAdap_Edit.OnItemClickListener
    lateinit var adp2edit : MyUnitAdap_Edit




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.authentication_fragment, container, false)
        utils = Utils(activity!!)
        verificationData = VerificationData()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            MediaManager.init(activity!!);
        }
        catch (e:Exception){

        }

        complete=rootView.findViewById(R.id.complete)
        familyimg=rootView.findViewById(R.id.button3)
        houseimg=rootView.findViewById(R.id.button4)
        unitimg=rootView.findViewById(R.id.button5)

        familylist=rootView.findViewById(R.id.familylist)
        houselist=rootView.findViewById(R.id.houselist)
        unitlist=rootView.findViewById(R.id.unitlist)

        complete.setOnClickListener {

            (activity as Family_Main).save()
        }



        click = object : MyImageAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }

        clickedit = object : MyImageAdap_Edit.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }

        click1edit = object : MyhouseAdap_Edit.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }

        click2edit = object : MyUnitAdap_Edit.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }

        unitlist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adp2edit = MyUnitAdap_Edit(unitimgarr_code, activity!!, click2edit)
        unitlist.adapter = adp2edit


        houselist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adp1edit = MyhouseAdap_Edit(houseimgarr_code, activity!!, click1edit)
        houselist.adapter = adp1edit

        familylist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adp = MyImageAdap(familyimgarr, activity!!, click)
        familylist.adapter = adp

        familylist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adpedit = MyImageAdap_Edit(familyimgarr_code, activity!!, clickedit)
        familylist.adapter = adpedit


        click1 = object : MyhouseAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }
        houselist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
        houselist.adapter = adp1


        click1 = object : MyhouseAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }
        houselist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
        houselist.adapter = adp1


        click2 = object : MyUnitAdap.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }


        }
        unitlist.setLayoutManager(LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false) as RecyclerView.LayoutManager?);
        adp2 = MyUnitAdap(unitimgarr, activity!!, click2)
        unitlist.adapter = adp2




        familyimg.setOnClickListener {


            if(CheckingPermissionIsEnabledOrNot(activity!!)){


                if(familyimgarr_code.size<5||familyimgarr.size<5) {
                    selectImage()

                }
                else{
                    Toast.makeText(activity,"Already 5 images picked",Toast.LENGTH_SHORT).show()
                }

            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }

        houseimg.setOnClickListener {


            if(CheckingPermissionIsEnabledOrNot(activity!!)){
                if(houseimgarr_code.size<5||houseimgarr.size<5) {
                    selectImages()

                }
                else{
                    Toast.makeText(activity,"Already 5 images picked",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }

        unitimg.setOnClickListener {


            if(CheckingPermissionIsEnabledOrNot(activity!!)){
                selectImages1()
            }
            else{
                RequestMultiplePermission(activity!!)
            }

        }
        pref = activity!!.getSharedPreferences("MyPref", 0)
        var unitval=pref!!.getString("unitlist","")

            if(unitval!!.isNotEmpty()){
                unitimg.visibility=View.INVISIBLE
            }
        else{
                unitimg.visibility=View.VISIBLE

            }

        set()

        return rootView
    }


    fun invis(){
        familyimg.visibility=View.INVISIBLE
    }

    fun set() {
        var fid = (activity as Family_Main).detailsFragment!!.fid

        if (fid.isNotEmpty()) {
            println("familyimgarr_code" + familyimgarr_code.size.toString())

            familyimg.setText("ADD IMAGES " + familyimgarr_code.size + " / " + "5")
            houseimg.setText("ADD IMAGES " + houseimgarr_code.size + " / " + "5")
            unitimg.setText("ADD IMAGES " + unitimgarr_code.size + " / " + "5")
        } else {
            println("size" + familyimgarr.size.toString())
            familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + "5")
            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + "5")
            unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + "5")


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

    public fun removeImageedit(pos:String) {
        familyimgarr_code.removeAt(pos.toInt())
        familyimgarr.removeAt(pos.toInt())
        adpedit = MyImageAdap_Edit(familyimgarr_code, activity!!, clickedit)
        familylist.adapter = adpedit

        if(familyimgarr_code.size<5){
            familyimg.visibility=View.VISIBLE
        }
        familyimg.setText("ADD IMAGES " + familyimgarr_code.size + " / " + "5")

        adpedit.notifyDataSetChanged()
    }
    public fun removeImageedit1(pos:String) {
        houseimgarr_code.removeAt(pos.toInt())
        houseimgarr.removeAt(pos.toInt())
        adp1edit = MyhouseAdap_Edit(houseimgarr_code, activity!!, click1edit)
        houselist.adapter = adp1edit
        if(houseimgarr_code.size<5){
            houseimg.visibility=View.VISIBLE
        }
        houseimg.setText("ADD IMAGES " + houseimgarr_code.size + " / " + "5")

        adp1edit.notifyDataSetChanged()
    }


        public fun selectImageedit(pos:String) {
        changepos=pos
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 110)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 10)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    public fun selectImageedit1(pos:String) {
        changepos=pos
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 111)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 11)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    public fun selectImages() {
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

    public fun selectImages1() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 104)
            } else if (options[item] == "Choose from Gallery") {
                val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(i, 3)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {

                Log.e("arraysize", familyimgarr.size.toString())

                if (requestCode == 1) {
                    if (resultCode == Activity.RESULT_OK && null != data &&(familyimgarr.size < 5 || familyimgarr_code.size < 5)) {
                        var picturePath: String? = null
                        var selectedImage = data!!.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        Log.i("original path1", picturePath + "")

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

                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        familyimgarr.add(resizeBitmap)

                        var fid=(activity as Family_Main).detailsFragment!!.fid

                        if(fid.isNotEmpty()){

                        }
                        else{
                            adp = MyImageAdap(familyimgarr, activity!!, click)
                            familylist.adapter = adp
                            adp.notifyDataSetChanged()
                        }

                        //var fid = (activity as Family_Main).detailsFragment!!.fid

                        if (fid.isNotEmpty()) {
                            println("familyimgarr_code" + familyimgarr_code.size.toString())

                            familyimg.setText("ADD IMAGES " + familyimgarr_code.size + " / " + "5")
                            houseimg.setText("ADD IMAGES " + houseimgarr_code.size + " / " + "5")
                            unitimg.setText("ADD IMAGES " + unitimgarr_code.size + " / " + "5")
                        } else {
                            println("size" + familyimgarr.size.toString())
                            familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + "5")
                            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + "5")
                            unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + "5")


                        }



                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        Log.e("imagecode", imagecode)

                        Get().execute()

                        //pDialo.setMax(3)

                        var result: String = ""


                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)

                        }

                    } else {
                        if (familyimgarr.size == 5) {
                            familyimg.visibility = View.INVISIBLE
                        }
                    }
                } else if (requestCode == 102) {
                    if (familyimgarr.size < 5 || familyimgarr_code.size < 5) {

                        try {

                            var selectedImageUri = data!!.data
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                            )
                            val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                            familyimgarr.add(resizeBitmap!!)

                            var fid=(activity as Family_Main).detailsFragment!!.fid

                            if(fid.isNotEmpty()){

                            }

                            else{
                                adp = MyImageAdap(familyimgarr, activity!!, click)
                                familylist.adapter = adp
                                adp.notifyDataSetChanged()
                            }

                            if (fid.isNotEmpty()) {
                                println("familyimgarr_code" + familyimgarr_code.size.toString())

                                familyimg.setText("ADD IMAGES " + familyimgarr_code.size + " / " + "5")
                                houseimg.setText("ADD IMAGES " + houseimgarr_code.size + " / " + "5")
                                unitimg.setText("ADD IMAGES " + unitimgarr_code.size + " / " + "5")
                            } else {
                                println("size" + familyimgarr.size.toString())
                                familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + "5")
                                houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + "5")
                                unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + "5")


                            }
                            //utrimageone.setImageBitmap(bitmap)
                            // utrimageone.setVisibility(View.VISIBLE)
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                            byteArray = stream.toByteArray()
                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            Get().execute()


                            // set()

                            //authenticationFragment!!.familyimg.setText("ADD IMAGES "+authenticationFragment!!.familyimgarr.size+" / "+"5")
                            val path = getImgPath(selectedImageUri!!)
                            //getBase64FromPath(path);
                            if (path != null) {
                                val f = File(path!!)

                                selectedImageUri = Uri.fromFile(f)

                            }


                        } catch (e: Exception) {
                            val thumbnail = data!!.extras!!.get("data") as Bitmap?
                            familyimgarr.add(thumbnail!!)


                            adp = MyImageAdap(familyimgarr, activity!!, click)
                            familylist.adapter = adp
                            adp.notifyDataSetChanged()

                            val stream = ByteArrayOutputStream()
                            thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                            byteArray = stream.toByteArray()

                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            Get().execute()


                            //cam.setVisibility(View.GONE);
                            //cam1.setVisibility(View.GONE);
                            //imagevalues = "selected";
                            saveImage(thumbnail)
                        }


                    } else {
                        /*Toast.makeText(getActivity(),"Error in Conversion",Toast.LENGTH_SHORT).show();*/
                        Log.e("Show Image 1", "Else_part")
                        if (familyimgarr_code.size == 5) {
                            familyimg.visibility = View.INVISIBLE

                        }
                    }
                }

                if (requestCode == 10) {
                    if (resultCode == Activity.RESULT_OK && null != data && (familyimgarr.size < 5 || familyimgarr_code.size < 5)) {
                        var picturePath: String? = null
                        var selectedImage = data.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        Log.i("original path1", picturePath + "")

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

                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        familyimgarr.set(changepos.toInt(),resizeBitmap)

                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        Log.e("imagecode", imagecode)



                        Getedit().execute()

                        //pDialo.setMax(3)

                        var result: String = ""


                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)

                        }

                    } else {
                        if (familyimgarr_code.size == 5) {
                            familyimg.visibility = View.INVISIBLE
                        }
                    }
                } else if (requestCode == 110) {
                    if (familyimgarr.size < 5 || familyimgarr_code.size < 5) {

                        try {

                            var selectedImageUri = data!!.data
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                            )
                            val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                            familyimgarr.set(changepos.toInt()!!,resizeBitmap!!)



                            //utrimageone.setImageBitmap(bitmap)
                            // utrimageone.setVisibility(View.VISIBLE)
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                            byteArray = stream.toByteArray()
                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            Getedit().execute()


                            // set()

                            //authenticationFragment!!.familyimg.setText("ADD IMAGES "+authenticationFragment!!.familyimgarr.size+" / "+"5")
                            val path = getImgPath(selectedImageUri!!)
                            //getBase64FromPath(path);
                            if (path != null) {
                                val f = File(path!!)

                                selectedImageUri = Uri.fromFile(f)

                            }


                        } catch (e: Exception) {
                            val thumbnail = data!!.extras!!.get("data") as Bitmap?
                            familyimgarr.add(thumbnail!!)


                            adp = MyImageAdap(familyimgarr, activity!!, click)
                            familylist.adapter = adp
                            adp.notifyDataSetChanged()

                            val stream = ByteArrayOutputStream()
                            thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                            byteArray = stream.toByteArray()

                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            Getedit().execute()


                            //cam.setVisibility(View.GONE);
                            //cam1.setVisibility(View.GONE);
                            //imagevalues = "selected";
                            saveImage(thumbnail)
                        }


                    } else {
                        /*Toast.makeText(getActivity(),"Error in Conversion",Toast.LENGTH_SHORT).show();*/
                        Log.e("Show Image 1", "Else_part")
                        if (familyimgarr_code.size == 5) {
                            familyimg.visibility = View.INVISIBLE

                        }
                    }
                }


                else if (requestCode == 11) {
                    if (resultCode == Activity.RESULT_OK && null != data && (houseimgarr.size < 5 || houseimgarr_code.size < 5)) {
                        var picturePath: String? = null
                        var selectedImage = data.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        Log.i("original path1", picturePath + "")

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
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        houseimgarr.set(changepos.toInt(),resizeBitmap)


                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        Log.e("imagecode", imagecode)

                        Get1edit().execute()


                        //   Toast.makeText(getApplicationContext(),""+imagecode,Toast.LENGTH_SHORT).show();
                        //houseimgarr_code.add(imagecode)
                        //houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + 5)

                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)


                        }

                    } else {
                        if (houseimgarr_code.size == 5) {
                            houseimg.visibility = View.INVISIBLE
                        }
                    }


                } else if (requestCode == 111) {

                    if (houseimgarr.size < 5 || houseimgarr_code.size < 5) {
                        try {

                            var selectedImageUri = data!!.data
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                            )
                            val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                            houseimgarr.set(changepos.toInt(),resizeBitmap!!)



                            //utrimageone.setImageBitmap(bitmap)
                            // utrimageone.setVisibility(View.VISIBLE)
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                            byteArray = stream.toByteArray()
                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                            Get1edit().execute()

                            /*houseimgarr_code.add(utrimagecode)
                            adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
                            houselist.adapter = adp1
                            adp1.notifyDataSetChanged()
                            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + 5)*/

                            // set()

                            //authenticationFragment!!.familyimg.setText("ADD IMAGES "+authenticationFragment!!.familyimgarr.size+" / "+"5")
                            val path = getImgPath(selectedImageUri!!)
                            //getBase64FromPath(path);
                            if (path != null) {
                                val f = File(path!!)

                                selectedImageUri = Uri.fromFile(f)

                            }


                        } catch (e: Exception) {
                            val thumbnail = data!!.extras!!.get("data") as Bitmap?
                            houseimgarr.add(thumbnail!!)




                            val stream = ByteArrayOutputStream()
                            thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                            byteArray = stream.toByteArray()

                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                            Get1().execute()

                            saveImage(thumbnail)
                        }

                    } else {
                        /*Toast.makeText(getActivity(),"Error in Conversion",Toast.LENGTH_SHORT).show();*/
                        Log.e("Show Image 1", "Else_part")
                        if (houseimgarr_code.size == 5) {
                            houseimg.visibility = View.INVISIBLE

                        }
                    }
                }




                else if (requestCode == 2) {
                    if (resultCode == Activity.RESULT_OK && null != data && (houseimgarr.size <5 || houseimgarr_code.size < 5)) {
                        var picturePath: String? = null
                        var selectedImage = data.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        Log.i("original path1", picturePath + "")

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
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        houseimgarr.add(resizeBitmap)
                        var fid=(activity as Family_Main).detailsFragment!!.fid

                        if(fid.isNotEmpty()){

                        }

                        else{
                            adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
                            houselist.adapter = adp1
                            adp1.notifyDataSetChanged()
                        }

                        if (fid.isNotEmpty()) {
                            println("familyimgarr_code" + familyimgarr_code.size.toString())

                            familyimg.setText("ADD IMAGES " + familyimgarr_code.size + " / " + "5")
                            houseimg.setText("ADD IMAGES " + houseimgarr_code.size + " / " + "5")
                            unitimg.setText("ADD IMAGES " + unitimgarr_code.size + " / " + "5")
                        } else {
                            println("size" + familyimgarr.size.toString())
                            familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + "5")
                            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + "5")
                            unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + "5")


                        }


                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        Log.e("imagecode", imagecode)

                        Get1().execute()


                        //   Toast.makeText(getApplicationContext(),""+imagecode,Toast.LENGTH_SHORT).show();
                            //houseimgarr_code.add(imagecode)
                            //houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + 5)

                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)


                        }

                    } else {
                        if (houseimgarr_code.size == 5) {
                            houseimg.visibility = View.INVISIBLE
                        }
                    }


                } else if (requestCode == 103) {

                    if (houseimgarr.size < 5 || houseimgarr_code.size < 5) {
                        try {

                            var selectedImageUri = data!!.data
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                            )
                            val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                            houseimgarr.add(resizeBitmap!!)

                            var fid=(activity as Family_Main).detailsFragment!!.fid

                            if(fid.isNotEmpty()){

                            }

                            else{
                                adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
                                houselist.adapter = adp1
                                adp1.notifyDataSetChanged()
                            }
                            //utrimageone.setImageBitmap(bitmap)
                            // utrimageone.setVisibility(View.VISIBLE)
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                            byteArray = stream.toByteArray()
                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)


                            if (fid.isNotEmpty()) {
                                println("familyimgarr_code" + familyimgarr_code.size.toString())

                                familyimg.setText("ADD IMAGES " + familyimgarr_code.size + " / " + "5")
                                houseimg.setText("ADD IMAGES " + houseimgarr_code.size + " / " + "5")
                                unitimg.setText("ADD IMAGES " + unitimgarr_code.size + " / " + "5")
                            } else {
                                println("size" + familyimgarr.size.toString())
                                familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + "5")
                                houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + "5")
                                unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + "5")


                            }

                            Get1().execute()

                            /*houseimgarr_code.add(utrimagecode)
                            adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
                            houselist.adapter = adp1
                            adp1.notifyDataSetChanged()
                            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + 5)*/

                            // set()

                            //authenticationFragment!!.familyimg.setText("ADD IMAGES "+authenticationFragment!!.familyimgarr.size+" / "+"5")
                            val path = getImgPath(selectedImageUri!!)
                            //getBase64FromPath(path);
                            if (path != null) {
                                val f = File(path!!)

                                selectedImageUri = Uri.fromFile(f)

                            }


                        } catch (e: Exception) {
                            val thumbnail = data!!.extras!!.get("data") as Bitmap?
                            houseimgarr.add(thumbnail!!)


                            adp1 = MyhouseAdap(houseimgarr, activity!!, click1)
                            houselist.adapter = adp1
                            adp1.notifyDataSetChanged()

                            val stream = ByteArrayOutputStream()
                            thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                            byteArray = stream.toByteArray()

                           imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                            Get1().execute()

                            saveImage(thumbnail)
                        }

                    } else {
                        /*Toast.makeText(getActivity(),"Error in Conversion",Toast.LENGTH_SHORT).show();*/
                        Log.e("Show Image 1", "Else_part")
                        if (houseimgarr_code.size == 5) {
                            houseimg.visibility = View.INVISIBLE

                        }
                    }
                } else if (requestCode == 3) {
                    if (resultCode == Activity.RESULT_OK && null != data && (unitimgarr.size < 5|| unitimgarr_code.size < 5)) {
                        var picturePath: String? = null
                        var selectedImage = data.data
                        picturePath = getImgPath(selectedImage)
                        fi = File(picturePath!!)

                        // user_img.setImageURI(selectedImage);
                        // Picasso.with(MyInfo.this).load(picturePath).placeholder(R.mipmap.userplaceholder).into(user_img);
                        val yourSelectedImage = CommonFunctions.decodeFile1(picturePath, 400, 200)

                        Log.i("original path1", picturePath + "")

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
                        val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                        unitimgarr.add(resizeBitmap)


                        adp2 = MyUnitAdap(unitimgarr, activity!!, click2)
                        unitlist.adapter = adp2
                        adp2.notifyDataSetChanged()

                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        byteArray = stream.toByteArray()
                        imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        Log.e("imagecode", imagecode)
                        //   Toast.makeText(getApplicationContext(),""+imagecode,Toast.LENGTH_SHORT).show();
                        //unitimgarr_code.add(imagecode)
                        //unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + 5)

                        Get2().execute()
                        val path = getImgPath(selectedImage!!)
                        //choose_files.setText("Remove Image")
                        if (path != null) {
                            val f = File(path!!)


                        }

                    } else {
                        if (unitimgarr.size == 5) {
                            unitimg.visibility = View.INVISIBLE
                        }
                    }

                } else if (requestCode == 104) {

                    if (unitimgarr.size < 5 || unitimgarr_code.size < 5) {
                        try {

                            var selectedImageUri = data!!.data
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.getBaseContext().getContentResolver(),
                                selectedImageUri
                            )

                            val resizeBitmap = resize(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                            unitimgarr.add(resizeBitmap!!)


                            adp2 = MyUnitAdap(unitimgarr, activity!!, click2)
                            unitlist.adapter = adp2
                            adp2.notifyDataSetChanged()
                            //utrimageone.setImageBitmap(bitmap)
                            // utrimageone.setVisibility(View.VISIBLE)
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                            byteArray = stream.toByteArray()
                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)

                            Get2().execute()

                            val path = getImgPath(selectedImageUri!!)
                            //getBase64FromPath(path);
                            if (path != null) {
                                val f = File(path!!)

                                selectedImageUri = Uri.fromFile(f)

                            }


                        } catch (e: Exception) {
                            val thumbnail = data!!.extras!!.get("data") as Bitmap?
                            unitimgarr.add(thumbnail!!)


                            adp2 = MyUnitAdap(unitimgarr, activity!!, click2)
                            unitlist.adapter = adp2
                            adp2.notifyDataSetChanged()

                            val stream = ByteArrayOutputStream()
                            thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                            byteArray = stream.toByteArray()

                            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
                            Get2().execute()


                            //cam.setVisibility(View.GONE);
                            //cam1.setVisibility(View.GONE);
                            //imagevalues = "selected";
                            saveImage(thumbnail)
                        }

                    } else {
                        /*Toast.makeText(getActivity(),"Error in Conversion",Toast.LENGTH_SHORT).show();*/
                        Log.e("Show Image 1", "Else_part")
                        if (unitimgarr.size == 5) {
                            unitimg.visibility = View.INVISIBLE

                        }
                    }

                }
            }
            } catch (e: Exception) {
                Log.e("FileSelectorActivity", "File select error", e)
            }


    }

    private fun resize(image:Bitmap, maxWidth:Int, maxHeight:Int):Bitmap {
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

    inner class Get : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo : ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);

            pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Uploading...");
            pDialo.setIndeterminate(true);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(true);
            pDialo.show()

            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val config = HashMap<Any,Any>();
            config.put("cloud_name", "elancier-solutions");
            config.put("api_key", "584714651824942");
            config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
            val cloudinary = Cloudinary(config);

            try {
                val config = HashMap<Any,Any>();
                config.put("cloud_name", "elancier-solutions");
                config.put("api_key", "584714651824942");
                config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
                val cloudinary = Cloudinary(config);
                var k=""
                try {
                    val fi=cloudinary.uploader().upload("data:image/png;base64"+","+imagecode,HashMap<Any,Any>());
                    k=fi.get("url").toString()
                    familyimgarr_code.add(k)

                    var fid=(activity as Family_Main).detailsFragment!!.fid

                    if(fid.isNotEmpty()){
                        activity!!.runOnUiThread {
                            adpedit = MyImageAdap_Edit(familyimgarr_code, activity!!, clickedit)
                            familylist.adapter = adpedit
                            adpedit.notifyDataSetChanged()
                        }
                    }
                    else{

                    }


                    Log.e("fival",k.toString());

                } catch (e:IOException) {
                    e.printStackTrace();
                }

            } catch (e:IOException ) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialo.dismiss()
            familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + 5)
            pDialo.dismiss()
            Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
            //Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()


        }
    }


    inner class Getedit : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo : ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);

            pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Uploading...");
            pDialo.setIndeterminate(true);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(true);
            pDialo.show()

            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val config = HashMap<Any,Any>();
            config.put("cloud_name", "elancier-solutions");
            config.put("api_key", "584714651824942");
            config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
            val cloudinary = Cloudinary(config);

            try {
                val config = HashMap<Any,Any>();
                config.put("cloud_name", "elancier-solutions");
                config.put("api_key", "584714651824942");
                config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
                val cloudinary = Cloudinary(config);
                var k=""

                try {
                    activity!!.runOnUiThread {

                        val fi = cloudinary.uploader()
                            .upload("data:image/png;base64" + "," + imagecode, HashMap<Any, Any>());
                        k = fi.get("url").toString()
                        familyimgarr_code.set(changepos.toInt(),k)
                        adpedit = MyImageAdap_Edit(familyimgarr_code, activity!!, clickedit)
                        familylist.adapter = adpedit
                        adpedit.notifyDataSetChanged()
                    }
                    Log.e("fival",k.toString());

                } catch (e:IOException) {
                    e.printStackTrace();
                }

            } catch (e:IOException ) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialo.dismiss()
            familyimg.setText("ADD IMAGES " + familyimgarr.size + " / " + 5)
            pDialo.dismiss()
            Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
            //Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()


        }
    }


    inner class Get1 : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo : ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);

            pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Uploading...");
            pDialo.setIndeterminate(true);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(true);
            pDialo.show()

            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val config = HashMap<Any,Any>();
            config.put("cloud_name", "elancier-solutions");
            config.put("api_key", "584714651824942");
            config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
            val cloudinary = Cloudinary(config);

            try {
                val config = HashMap<Any,Any>();
                config.put("cloud_name", "elancier-solutions");
                config.put("api_key", "584714651824942");
                config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
                val cloudinary = Cloudinary(config);
                var k=""
                try {
                    val fi=cloudinary.uploader().upload("data:image/png;base64"+","+imagecode,HashMap<Any,Any>());
                    k=fi.get("url").toString()
                    houseimgarr_code.add(k)

                    var fid=(activity as Family_Main).detailsFragment!!.fid

                    if(fid.isNotEmpty()){
                        activity!!.runOnUiThread {

                        adp1edit = MyhouseAdap_Edit(houseimgarr_code, activity!!, click1edit)
                        houselist.adapter = adp1edit
                        adp1edit.notifyDataSetChanged()

                        }
                    }

                    else{

                    }




                    Log.e("fival",k.toString());

                } catch (e:IOException) {
                    e.printStackTrace();
                }

            } catch (e:IOException ) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialo.dismiss()
            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + 5)
            pDialo.dismiss()
            Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
            //Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()


        }
    }

    inner class Get1edit : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo : ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);

            pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Uploading...");
            pDialo.setIndeterminate(true);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(true);
            pDialo.show()

            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val config = HashMap<Any,Any>();
            config.put("cloud_name", "elancier-solutions");
            config.put("api_key", "584714651824942");
            config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
            val cloudinary = Cloudinary(config);

            try {
                val config = HashMap<Any,Any>();
                config.put("cloud_name", "elancier-solutions");
                config.put("api_key", "584714651824942");
                config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
                val cloudinary = Cloudinary(config);
                var k=""
                try {
                    activity!!.runOnUiThread{
                        val fi = cloudinary.uploader()
                            .upload("data:image/png;base64" + "," + imagecode, HashMap<Any, Any>());
                        k = fi.get("url").toString()
                        houseimgarr_code.set(changepos.toInt(), k)


                        adp1edit = MyhouseAdap_Edit(houseimgarr_code, activity!!, click1edit)
                        houselist.adapter = adp1edit
                        adp1edit.notifyDataSetChanged()
                    }
                    Log.e("fival",k.toString());

                } catch (e:IOException) {
                    e.printStackTrace();
                }

            } catch (e:IOException ) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialo.dismiss()
            houseimg.setText("ADD IMAGES " + houseimgarr.size + " / " + 5)
            pDialo.dismiss()
            Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
            //Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()


        }
    }


    inner class Get2 : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo : ProgressDialog
        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);

            pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Uploading...");
            pDialo.setIndeterminate(true);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(true);
            pDialo.show()

            Log.i("LoginTask", "started")
        }

        override fun doInBackground(vararg param: String): String? {
            var result: String = ""
            val config = HashMap<Any,Any>();
            config.put("cloud_name", "elancier-solutions");
            config.put("api_key", "584714651824942");
            config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
            val cloudinary = Cloudinary(config);

            try {
                val config = HashMap<Any,Any>();
                config.put("cloud_name", "elancier-solutions");
                config.put("api_key", "584714651824942");
                config.put("api_secret", "0reEphuUag4hInPcxnAIDj-ji1o");
                val cloudinary = Cloudinary(config);
                var k=""
                try {
                    val fi=cloudinary.uploader().upload("data:image/png;base64"+","+imagecode,HashMap<Any,Any>());
                    k=fi.get("url").toString()
                    unitimgarr_code.add(k)

                    Log.e("fival",k.toString());

                } catch (e:IOException) {
                    e.printStackTrace();
                }

            } catch (e:IOException ) {
                e.printStackTrace();
            }

            return result
        }

        override fun onPostExecute(resp: String?) {
            pDialo.dismiss()
            unitimg.setText("ADD IMAGES " + unitimgarr.size + " / " + 5)
            pDialo.dismiss()
            Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()
            //Toast.makeText(activity,"Uploaded Successfully",Toast.LENGTH_SHORT).show()


        }
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
        Log.i("utilsresult", result!! + "")
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


    fun CheckingPermissionIsEnabledOrNot(context: Activity): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        val ACCESS_NETWORK_STATEt = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //val GPS_PROVIDER = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GPS_PROVIDER)
        //val NETWORK_PROVIDER = ContextCompat.checkSelfPermission(this, android.Manifest.permission.NETWORK_PROVIDER)
        //val PASSIVE_PROVIDER = ContextCompat.checkSelfPermission(this, android.Manifest.permission.PASSIVE_PROVIDER)
       // val CALL_PHONE = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
       // val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)


        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATEt == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED&&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED

    }

    override fun onResume() {
        super.onResume()
        set()
    }




    private fun RequestMultiplePermission(context: Activity) {

        ActivityCompat.requestPermissions(context, arrayOf<String>(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            //android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        ), Appconstands.RequestPermissionCode
        )

    }
}
