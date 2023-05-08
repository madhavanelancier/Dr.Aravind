package com.elancier.domdox.FormFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.elancier.domdox.BuildConfig
import com.elancier.domdox.Common.*
import com.elancier.domdox.DataClasses.VerificationData
import com.elancier.domdox.GroupMembers
import com.elancier.domdox.R
import com.elancier.domdox.retrofit.ApproveUtils
import com.elancier.domdox.retrofit.Resp
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class AuthenticationFragment : Fragment() {
    lateinit var verificationData: VerificationData
    lateinit var handler : Handler
    var USBstatus=0
    internal val USBATTACH: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                //showDevices();
                USBstatus=1
                println("device attached")
                Toast.makeText(activity!!,"Device Attached", Toast.LENGTH_LONG).show()
                /*mHoinPrinter= USBClass(this@Dashboard, this@Dashboard)
                mHoinPrinter.connect(this@Dashboard)*/
                //Fingerprint().scan(this@Dashboard, Handler())
                Fingerprint().scan(activity!!,handler)
                println("status : "+ Fingerprint().status)
            }

        }
    }
    internal val USBDETACH: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                val device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice
                println("devide detached")
                Toast.makeText(activity!!,"Device Detached", Toast.LENGTH_LONG).show()
                USBstatus=0
                if (device != null) {
                    // call your method that cleans up and closes communication with the device
                    /* val binder = mHashMap.get(device)
                     if (binder != null) {
                         binder!!.onDestroy()
                         mHashMap.remove(device)
                     }*/
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        //printerManager.registerReceivers()
        activity!!.unregisterReceiver(USBATTACH)
        activity!!.unregisterReceiver(USBDETACH)

    }
    val RequestPermissionCode = 7
    var which=0
    lateinit var fi: File
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

    lateinit var rootView:View
    var fingersingleimagecode = ""
    var fingercoupleimagecode = ""

    lateinit var single_image : ImageView
    lateinit var couple_image : ImageView
    lateinit var ap_finger : ImageView
    lateinit var ap_verified : ImageView
    lateinit var co_finger : ImageView
    lateinit var co_verified : ImageView
    lateinit var single_camera : ImageButton
    lateinit var couple_camera : ImageButton
    lateinit var complete : Button
    lateinit var utils : Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.authentication_fragment, container, false)
        utils = Utils(activity!!)
        verificationData = VerificationData()
        single_image = rootView.findViewById(R.id.single_image)
        coupl