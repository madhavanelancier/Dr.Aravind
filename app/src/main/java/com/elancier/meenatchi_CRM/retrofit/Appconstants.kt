package com.elancier.meenatchi_CRM.retrofit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


object Appconstants {

    var Domin = "https://app.draravindsivf.com/referral/api/"//"http://teamdev.co.in/domdox/api/"
    //  var Domin = "https://meenatchienterprises.com/admin_app/api/"//"http://teamdev.co.in/domdox/api/"
    var img = "https://arsmatrimony.com/"
    var LOGIN = Domin + "login"
    var Areacodes = Domin + "area/"
    var Centrecodes = Domin + "center/"
    var memverify = Domin + "member_verify_view/"
    var state = "http://domdox.com/dom/api/state"
    var areacode = Domin + "get_area"
    var staffcode = Domin + "get_staff"
    var Member = Domin + "add_family"
    var Memberharvest = Domin + "add_harvest"
    var EditMember = Domin + "edit_family"
    var Membersfam = Domin + "add_familymem"
    var MemberFamily = Domin + "addfamily"
    var Centres = Domin + "centre_list/"
    var Members = Domin + "member_list/"
    var MembersPay = Domin + "member_pay"
    var Denomination = Domin + "denomination"
    var LeaveApply = Domin + "leave"
    var Leave = Domin + "leave_list"
    var Centre_OD = Domin + "od"
    var MemberList = Domin + "members"
    var Notification = Domin + "notification"
    var add_eod = Domin + "eod"
    var eod_edit = Domin + "eod_edit"
    var eodList = Domin + "eod_list"
    var Image = "https://www.domdox.com/dom/api/uploads/"//"http://teamdev.co.in/domdox/api/uploads/"
    var dashboard = "dashboard/"
    var lateTime = 15 * 60 * 1000
    var appstarttime = "07:00"
    var appendtime = "16:00"
    var NET = 143
    lateinit var alert : AlertDialog

    fun net_status(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var connected=false
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true
        } else {
            //Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show()
            connected = false
        }
        return connected
    }

    fun toast(msg:String,context:Context){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun isValidtime(cn : Context): Boolean {
        val utils = Utils(cn)
        val today = SimpleDateFormat("dd-MM-yyyy").format(Date())
        if (utils.allowed_date() == today && utils.extra_time() == "on"){
            println("Today No Logout Timing")
            return true
        }else {
            val from = SimpleDateFormat("dd/MM/yyyy $appstarttime").format(Date())
            val to = SimpleDateFormat("dd/MM/yyyy $appendtime").format(Date())
            val fr = SimpleDateFormat("dd/MM/yyyy hh:mm").parse(from).time / 1000
            val t = SimpleDateFormat("dd/MM/yyyy hh:mm").parse(to).time / 1000
            val current = System.currentTimeMillis() / 1000
            println("fr : " + fr)
            println("t : " + t)
            println("current : " + current)
            if (fr <= current && t >= current) {
                return true
            } else {
                return false
            }
        }
        //return if (ccMonth.compareTo(SimpleDateFormat("MMyyyy").format(Date())) < 1) false else true
    }

    fun maintenance(activity: Activity){
        val call2 = ApproveUtils.Get.getMaintenance()
        call2.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("maintenance responce", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.getStatus() == "Success"){

                    }else {
                        Utils(activity).setMaintenance("")
                        if (::alert.isInitialized){
                            if (alert.isShowing) {
                                alert.dismiss()
                            }
                        }
                        //Toast.makeText(this@Dashboard, example.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                Log.e("maince Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (t.toString().contains("Unable to resolve host")) {
                    if (Utils(activity).Maintenance()!!.isNotEmpty()) {
                        Utils(activity).Maintenance()?.let { maintenancePopup(activity, it) }
                    } else {
                        if ((::alert.isInitialized)) {
                            if (alert.isShowing) {
                                alert.dismiss()
                            }
                        }
                    }
                }
            }
        })
    }

    fun maintenancePopup(activity: Activity,message:String){
        if (!(::alert.isInitialized)){
            val alert11 = AlertDialog.Builder(activity)
            alert11.setCancelable(false)
            //alert11.setTitle("Maintenance")
            alert11.setMessage(message)
            alert11.setPositiveButton(
                "OK",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.dismiss()
                        activity.finish()
                        //activity.finishAffinity();
                        /*val intent = Intent(activity, Login::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        activity.startActivity(intent)*/
                    }
                })
            alert = alert11.create()
            alert.show()
        }else{
            if (!alert.isShowing) {
                val alert11 = AlertDialog.Builder(activity)
                alert11.setCancelable(false)
                //alert11.setTitle("Maintenance")
                alert11.setMessage(message)
                alert11.setPositiveButton(
                    "OK",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()
                            activity.finish()
                        }
                    })
                alert = alert11.create()
                alert.show()
            }
        }
    }
}
/*fun net_status():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var connected=false
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true
        } else {
            Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show()
            connected = false
        }
        return connected
    }
*/


/* val call = ApproveUtils.Get.getMem("area/$userid")
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("getLogin responce", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success"){
                        val mem = example.response!!

                    }else {
                        Toast.makeText(this@GroupMembers, example.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                Log.e("getLogin Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        this@GroupMembers,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })*/

/*
Device and Employee is manualy inserted into the table,

Device ID Check
{
	"device_id":"device_id",
	"type":"Device ID"
}
Response:
{
   if(checked == "Checked"){
   "Status": "Failure",
   "Response": "Device ID Already Exits"
   }
   if(checked == "Unchecked"){
   "Status": "Success",
   "Response": [
       {
           "brid": "1",
           "days": "6"
       }
   ]
   and set checked = "Checked", Days = "0"

   }
}

Branch
{
	"device_name":"Redmi note 5 pro",
	"device_id":"989d9a4ab1687b11",
	"days":"6",
	"checked":"Unchecked",
	"brid":"1",
}
Employee
{
	"ename":"saravana",
	"emobile":"9894940560",
	"password":"1234567",
	"designation":"Admin",
}

*/
