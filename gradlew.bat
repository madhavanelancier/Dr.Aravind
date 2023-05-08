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
                Toast.makeText(activity!!,"Device Attached", Toa