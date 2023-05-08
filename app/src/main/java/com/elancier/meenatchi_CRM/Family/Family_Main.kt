package com.elancier.meenatchi_CRM.Family

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.elancier.meenatchi_CRM.Adapers.*
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.DataClasses.FamilyMember
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_family__list.shimmer_view_container
import kotlinx.android.synthetic.main.activity_family__main.*
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
import kotlin.collections.ArrayList


class Family_Main : AppCompatActivity() {
    internal var detailsFragment: DetailsFragment? = null
    internal var authenticationFragment: AuthenticationFragment? = null
    internal var viewPager: com.elancier.meenatchi_CRM.Family.Customviewpager? = null
    internal var tabLayout: TabLayout? = null
    internal lateinit var pDialog : SweetAlertDialog
    internal var adapter: ViewPagerAdapter? = null
    internal lateinit var byteArray: ByteArray
    lateinit var adp : MyImageAdap
    var CentresArrays = java.util.ArrayList<CentresData>()
    var checkplant=ArrayList<String>()

    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null

    lateinit var click : MyImageAdap.OnItemClickListener

    var imagecode=""
    var utrimagecode=""
    internal lateinit var fi: File

    var vid=""


    companion object{
        lateinit var act: Context
    }
    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("message")
            if (message=="clearall"){
                //clearAll()
                //setTab()
            }
            Log.d("receiver", "Got message: $message")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family__main)

        viewPager = findViewById(R.id.viewpager) as com.elancier.meenatchi_CRM.Family.Customviewpager
        viewPager!!.offscreenPageLimit = 2

        tabLayout = findViewById(R.id.tabs) as TabLayout

        setSupportActionBar(toolbar)
        //toolbar.title = "Application Form"
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        //}

        val ab=supportActionBar
        ab!!.setTitle("Add Family");
        ab!!.title="Add Family"
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)
        /*tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);*/
        createTabIcons()

        setupViewPager(viewPager!!)

        try{
            var intent=intent.extras
            var id=intent!!.getString("id")
            if(!id.isNullOrEmpty()){
                editarea(id)
            }
        }
        catch (e:Exception){

        }



        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.setCurrentItem(tab.position, true)
                tabLayout!!.getTabAt(tab.position)!!.select()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })



    }
    private fun createTabIcons() {

        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabOne.text = "Personal Details"
        tabOne.setTextColor(resources.getColor(R.color.white))
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_id_card, 0, 0)
        tabLayout!!.getTabAt(0)!!.customView = tabOne


        val tabTwo = LayoutInflater.from(this@Family_Main).inflate(R.layout.custom_tab, null) as TextView
        tabTwo.text = "Pictures"
        tabTwo.setTextColor(resources.getColor(R.color.white))
        //tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_authentication, 0, 0)
        tabLayout!!.getTabAt(1)!!.customView = tabTwo

    }

    fun set(){
        viewPager!!.setCurrentItem(1, true)
        tabLayout!!.getTabAt(1)!!.select()
    }

    fun save(){

        var check=""
        if(detailsFragment!!.chstem.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.chstem.text.toString())
        }
        if(detailsFragment!!.chtom.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.chtom.text.toString())

        }
        if(detailsFragment!!.chcum.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.chcum.text.toString())

        }
        if(detailsFragment!!.cspain.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.cspain.text.toString())

        }
        if(detailsFragment!!.chcaps.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.chcaps.text.toString())

        }
        if(detailsFragment!!.cschilli.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.cschilli.text.toString())

        }
        if(detailsFragment!!.chmust.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.chmust.text.toString())

        }
        if(detailsFragment!!.csflower.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.csflower.text.toString())

        }
        if(detailsFragment!!.chbitt.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.chbitt.text.toString())

        }
        if(detailsFragment!!.csgreen.isChecked==true){
            check="true"
            checkplant.add(detailsFragment!!.csgreen.text.toString())

        }

        if(detailsFragment!!.acode.selectedItemPosition!=0&&
            detailsFragment!!.mname.text.toString().trim().isNotEmpty()&&
            detailsFragment!!.mrmtype.selectedItemPosition!=0&&
            detailsFragment!!.comrmrs.text.toString().trim().isNotEmpty()&&
            detailsFragment!!.nname.text.toString().trim().isNotEmpty()&&
            authenticationFragment!!.familyimgarr_code.isNotEmpty()&&
            authenticationFragment!!.houseimgarr_code.isNotEmpty()&&
            authenticationFragment!!.unitimgarr_code.isNotEmpty()&&
            check.isNotEmpty())
        {


            if(detailsFragment!!.vid.isEmpty()) {
                pDialog = SweetAlertDialog(this@Family_Main!!, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFFFCA18"));
                pDialog.setCancelable(false)
                pDialog.setTitleText("Creating Family...");
                pDialog.show();
                Handler().postDelayed({
                    VerificationSend().execute()

                }, 2000)
            }
            else if(detailsFragment!!.vid.isNotEmpty()){
                pDialog = SweetAlertDialog(this@Family_Main!!, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFFFCA18"));
                pDialog.setCancelable(false)
                pDialog.setTitleText("Saving Changes...");
                pDialog.show();
                Handler().postDelayed({
                    EditSend().execute()

                }, 2000)
            }


        }
        else{
            viewPager!!.setCurrentItem(0, true)
            tabLayout!!.getTabAt(0)!!.select()
            toast("Please fill required fields")

            detailsFragment!!.member_details.visibility=View.VISIBLE
            detailsFragment!!.co_applicant_details.visibility=View.VISIBLE

            if(detailsFragment!!.mname.text.toString().trim().isEmpty()){
                detailsFragment!!.mname.setError("Enter Name")
            }

            if(detailsFragment!!.acode.selectedItemPosition==0){
                toast("Select Vilage")
            }

            if(detailsFragment!!.mrmtype.selectedItemPosition==0){
                detailsFragment!!.occuperr.visibility= View.VISIBLE

            }

            if(detailsFragment!!.comrmrs.text.toString().trim().isEmpty()){
                detailsFragment!!.comrmrs.setError("Enter Unit Code")

            }
            if(detailsFragment!!.nname.text.toString().trim().isEmpty()){
                detailsFragment!!.nname.setError("Select Date")

            }
            if(authenticationFragment!!.familyimgarr_code.isEmpty()){
                toast("Take family image")

            }
            if(authenticationFragment!!.houseimgarr_code.isEmpty()){
                toast("Take house image")

            }
            if(authenticationFragment!!.unitimgarr_code.isEmpty()){
                toast("Take unit install image")

            }
            if(check.isEmpty()){
                toast("Select Plants")
            }

        }
    }



    private fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        val bundle  = Bundle()
        detailsFragment = DetailsFragment()

        detailsFragment!!.arguments = bundle
        authenticationFragment = AuthenticationFragment()
        adapter!!.addFragment(detailsFragment!!, "Personal Details")
        adapter!!.addFragment(authenticationFragment!!, "Authentication")
        viewPager.adapter = adapter



    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
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


    @SuppressLint("StaticFieldLeak")
    inner class VerificationSend : AsyncTask<String,Void,String>() {
        internal lateinit var pDialo : ProgressDialog

        internal lateinit var account_no : String

        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            /*pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Loading....");
            pDialo.setIndeterminate(false);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(false);
            //pDialo.setMax(3)
            pDialo.show()*/


            //Log.i("singleimagecode", singleimagecode)
            //Log.i("coupleimagecode", coupleimagecode)
            //Log.i("fingersingleimagecode", fingersingleimagecode)
            //Log.i("fingercoupleimagecode", fingercoupleimagecode)
            //Log.i("VerificationSend", "started")
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg para: String): String? {
            var result: String? = null
            val con = Connection()

            try {
                runOnUiThread {
                    val jobj = JSONObject()

                    var income=""
                    if(detailsFragment!!.mgender!!.selectedItemPosition==0){
                        income="Nil"
                    }
                    else{
                        income=detailsFragment!!.mgender.selectedItem.toString()

                    }

                    var salary=""
                    if(detailsFragment!!.mrationcard!!.text.toString().trim().isEmpty()){
                            salary="Nil"
                    }
                    else{
                        salary=detailsFragment!!.mrationcard.text.toString().trim()

                    }

                    //val staff_id = utils.getValue("staff_id")
                    jobj.put(
                        "village_id",
                        detailsFragment!!.villagearr.get(detailsFragment!!.acode.selectedItemPosition).id
                    )
                    jobj.put("familyhead_name", detailsFragment!!.mname.text.toString().trim())
                    jobj.put("familyhead_age", detailsFragment!!.msurname.text.toString().trim())
                    jobj.put("familyhead_occupation", detailsFragment!!.mrmtype.selectedItem.toString())
                    jobj.put("familyhead_income", income)
                    jobj.put("familyhead_salary", salary)
                    jobj.put("other_work", detailsFragment!!.otherrcc.text.toString().trim())
                    jobj.put("health_issue",  detailsFragment!!.mmarital.selectedItem.toString())
                    jobj.put("healthissue_description", detailsFragment!!.mvoterid.text.toString())
                    jobj.put("house",  detailsFragment!!.mhousesit.selectedItem.toString())
                    jobj.put("house_condition", detailsFragment!!.housecondition.text.toString())
                    jobj.put("house_image",authenticationFragment!!.houseimgarr_code.toString().removePrefix("[").removeSuffix("]"))
                    jobj.put("unit_code",detailsFragment!!.comrmrs.text.toString().trim())
                    jobj.put("date_installed",detailsFragment!!.nname.text.toString().trim())
                    jobj.put("fish",detailsFragment!!.fishtype.text.toString().trim())
                    val plantjarr = JSONArray()

                    for (i in 0 until checkplant.size){
                        val famobj = JSONObject()
                        famobj.put("name", checkplant[i])
                        plantjarr.put(famobj)
                    }
                    jobj.put("plant_seeded",plantjarr)

                    val familyjarr = JSONArray()

                    jobj.put("family_image",authenticationFragment!!.familyimgarr_code.toString().removePrefix("[").removeSuffix("]"))
                    jobj.put("unitinstall_image",authenticationFragment!!.unitimgarr_code.toString().removePrefix("[").removeSuffix("]"))


                    Log.i("Member Input", Appconstants.Member + "    " + jobj.toString())
                    val result1 = con.sendHttpPostjson2(Appconstants.Member, jobj, "")
                    println("result1 : "+result1)
                    val obj1 = JSONObject(result1)
                    if (obj1.getString("status") == "Success") {

                        try {
                            val obj2 = obj1.getJSONObject("message")
                            val objarr = obj2.getJSONArray("data")
                            val villageidobj = objarr.getJSONObject(0)
                            val vid = villageidobj.get("village_id").toString()
                            val fid = villageidobj.get("family_id").toString()

                            if (!fid.isNullOrEmpty()) {
                                val jobje = JSONObject()
                                //val staff_id = utils.getValue("staff_id")
                                jobje.put("village_id", vid)
                                jobje.put("family_id", fid)
                                val famjarr = JSONArray()
                                for (i in 0 until detailsFragment!!.Family!!.size) {
                                    val famobj = JSONObject()
                                    famobj.put("name", detailsFragment!!.Family!![i].Name)
                                    famobj.put("age", detailsFragment!!.Family!![i].Age)
                                    famobj.put("relation", detailsFragment!!.Family!![i].Relation)
                                    famobj.put("school", detailsFragment!!.Family!![i].school)
                                    famobj.put("occupation",detailsFragment!!.Family!![i].Occuption)
                                    famobj.put("monthly_income", detailsFragment!!.Family!![i].Income)
                                    famobj.put("health_issue", detailsFragment!!.Family!![i].health)
                                    famobj.put("health_issue_description", detailsFragment!!.Family[i]!!.healthdesc)
                                    famobj.put("future_goal", detailsFragment!!.Family!![i].feature)
                                    famjarr.put(famobj)
                                }

                                jobje.put("family", famjarr)

                                Log.i(
                                    "Member Input",
                                    Appconstants.Membersfam + "    " + jobje.toString()
                                )

                                val result1 =
                                    con.sendHttpPostjson2(Appconstants.Membersfam, jobje, "")

                                val obj1 = JSONObject(result1)

                                if (obj1.getString("status") == "Success") {
                                    try {
                                        toast("Family Added Successfully")
                                        pDialog.dismiss()

                                        val obj2 = obj1.getJSONObject("message")
                                        val objarr = obj2.getJSONArray("data")

                                }
                                    catch (e:Exception){
                                        pDialog.dismiss()

                                    }
                                }
                            }
                            else{
                                Toast.makeText(this@Family_Main!!,"There is a problem of Member id",Toast.LENGTH_LONG).show()
                                //complete.isEnabled=true
                                pDialog.dismiss()
                            }

                            println("vid, fid " + vid + " , " + fid)
                            finish()
                        }
                        catch (e:Exception){
                            pDialog.dismiss()
                            val obj2 = obj1.getJSONObject("message")

                            val objarr = obj2.get("data").toString()

                            toast(objarr)
                        }

                    }
                    else{
                        pDialog.dismiss()
                        val obj2 = obj1.getJSONObject("message")
                        val objarr = obj2.get("data").toString()
                        toast(objarr)
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(resp:String?) {
            try {

                if (resp != null) {
                    if (resp.isNotEmpty()) {
                        Log.i("Verification resp", resp!! + "")
                        val json = JSONArray("[" + resp + "]")
                        val obj1 = json.getJSONObject(0)
                        val sts = obj1.getString("Status").toString()
                        val jarr = obj1.getString("Response")
                        if (sts == "Success") {
                            /*val intent = Intent("clearall")
                        intent.putExtra("message", "clearall")
                        LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)*/
                            pDialog.dismiss()
                            pDialog = SweetAlertDialog(this@Family_Main!!, SweetAlertDialog.SUCCESS_TYPE)
                            pDialog.setTitleText("Account Has Been Created")
                            pDialog.setCancelable(false)
                            pDialog.setContentText("Your A/c no : $account_no")
                            pDialog.setConfirmText("Done")
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFFFCA18"));
                            pDialog.getProgressHelper().setRimColor(Color.parseColor("#FFFFCA18"));
                            pDialog.setConfirmClickListener(object :
                                SweetAlertDialog.OnSweetClickListener {
                                override fun onClick(sDialog: SweetAlertDialog?) {
                                    // reuse previous dialog instance
                                    sDialog!!.dismiss()
                                   /* this@Family_Main!!.setResult(
                                        Activity.RESULT_OK, Intent(
                                            this@Family_Main!!,
                                            GroupMembers::class.java
                                        )
                                    )
                                    this@Family_Main!!.finish()*/
                                }
                            }).show();
                            //Toast.makeText(activity!!,obj1.getString("Message").toString().toString(),Toast.LENGTH_LONG).show()
                           // complete.isEnabled = true
                        } else {
                            //complete.isEnabled = true
                            pDialog.dismiss()
                            Toast.makeText(
                                this@Family_Main!!,
                                obj1.getString("Message").toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else{
                        //complete.isEnabled = true
                        pDialog.dismiss()
                    }
                } else {
                   // complete.isEnabled=true
                    /*pDialog.dismiss()
                    Toast.makeText(
                        this@Family_Main!!,
                        "Oops! Something went wrong please try again.",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            } catch (e: Exception) {
                //complete.isEnabled=true
                //pDialog.dismiss()
                e.printStackTrace()
                Toast.makeText(
                    this@Family_Main!!,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class EditSend : AsyncTask<String,Void,String>() {
        internal lateinit var pDialo : ProgressDialog

        internal lateinit var account_no : String

        override fun onPreExecute() {
            //progbar.setVisibility(View.VISIBLE);
            /*pDialo = ProgressDialog(activity!!);
            pDialo.setMessage("Loading....");
            pDialo.setIndeterminate(false);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(false);
            //pDialo.setMax(3)
            pDialo.show()*/


            //Log.i("singleimagecode", singleimagecode)
            //Log.i("coupleimagecode", coupleimagecode)
            //Log.i("fingersingleimagecode", fingersingleimagecode)
            //Log.i("fingercoupleimagecode", fingercoupleimagecode)
            //Log.i("VerificationSend", "started")
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg para: String): String? {
            var result: String? = null
            val con = Connection()

            try {
                runOnUiThread {
                    val jobj = JSONObject()

                    var income=""
                    if(detailsFragment!!.mgender!!.selectedItemPosition==0){
                        income="Nil"
                    }
                    else{
                        income=detailsFragment!!.mgender.selectedItem.toString()

                    }

                    var salary=""
                    if(detailsFragment!!.mrationcard!!.text.toString().trim().isEmpty()){
                        salary="Nil"
                    }
                    else{
                        salary=detailsFragment!!.mrationcard.text.toString().trim()

                    }

                    //val staff_id = utils.getValue("staff_id")
                    jobj.put(
                        "village_id",
                        detailsFragment!!.villagearr.get(detailsFragment!!.acode.selectedItemPosition).id
                    )
                    jobj.put(
                        "family_id",
                        detailsFragment!!.fid
                    )
                    jobj.put("familyhead_name", detailsFragment!!.mname.text.toString().trim())
                    jobj.put("familyhead_age", detailsFragment!!.msurname.text.toString().trim())
                    jobj.put("familyhead_occupation", detailsFragment!!.mrmtype.selectedItem.toString())
                    jobj.put("familyhead_income", income)
                    jobj.put("familyhead_salary", salary)
                    jobj.put("other_work", detailsFragment!!.otherrcc.text.toString().trim())
                    jobj.put("health_issue",  detailsFragment!!.mmarital.selectedItem.toString())
                    jobj.put("healthissue_description", detailsFragment!!.mvoterid.text.toString())
                    jobj.put("house",  detailsFragment!!.mhousesit.selectedItem.toString())
                    jobj.put("house_condition", detailsFragment!!.housecondition.text.toString())
                    jobj.put("house_image",authenticationFragment!!.houseimgarr_code.toString().removePrefix("[").removeSuffix("]"))
                    jobj.put("unit_code",detailsFragment!!.comrmrs.text.toString().trim())
                    jobj.put("date_installed",detailsFragment!!.nname.text.toString().trim())
                    jobj.put("fish",detailsFragment!!.fishtype.text.toString().trim())
                    val plantjarr = JSONArray()

                    for (i in 0 until checkplant.size){
                        val famobj = JSONObject()
                        famobj.put("name", checkplant[i])
                        plantjarr.put(famobj)
                    }
                    jobj.put("plant_seeded",plantjarr)

                    val familyjarr = JSONArray()

                    jobj.put("family_image",authenticationFragment!!.familyimgarr_code.toString().removePrefix("[").removeSuffix("]"))
                    jobj.put("unitinstall_image",authenticationFragment!!.unitimgarr_code.toString().removePrefix("[").removeSuffix("]"))

                    val famjarr = JSONArray()
                    for (i in 0 until detailsFragment!!.Family!!.size) {
                        val famobj = JSONObject()
                        famobj.put("name", detailsFragment!!.Family!![i].Name)
                        famobj.put("age", detailsFragment!!.Family!![i].Age)
                        famobj.put("relation", detailsFragment!!.Family!![i].Relation)
                        famobj.put("school", detailsFragment!!.Family!![i].school)
                        famobj.put("occupation",detailsFragment!!.Family!![i].Occuption)
                        famobj.put("monthly_income", detailsFragment!!.Family!![i].Income)
                        famobj.put("health_issue", detailsFragment!!.Family!![i].health)
                        famobj.put("health_issue_description", detailsFragment!!.Family[i]!!.healthdesc)
                        famobj.put("future_goal", detailsFragment!!.Family!![i].feature)
                        famjarr.put(famobj)
                    }

                    jobj.put("family", famjarr)

                    Log.i("Member Input", Appconstants.EditMember + "    " + jobj.toString())
                    val result1 = con.sendHttpPostjson2(Appconstants.EditMember, jobj, "")
                    println("result1 : "+result1)
                    val obj1 = JSONObject(result1)
                    if (obj1.getString("status") == "Success") {

                        try {
                            val obj2 = obj1.getJSONObject("message")
                            val objarr = obj2.getJSONArray("data")
                            val villageidobj = objarr.getJSONObject(0)
                            val vid = villageidobj.get("village_id").toString()
                            val fid = villageidobj.get("family_id").toString()


                            println("vid, fid " + vid + " , " + fid)
                            finish()
                        }
                        catch (e:Exception){
                            pDialog.dismiss()
                            val obj2 = obj1.getJSONObject("message")

                            val objarr = obj2.get("data").toString()

                            toast(objarr)
                            finish()

                        }

                    }
                    else{
                        pDialog.dismiss()
                        val obj2 = obj1.getJSONObject("message")
                        val objarr = obj2.get("data").toString()
                        toast(objarr)
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(resp:String?) {
            try {

                if (resp != null) {
                    if (resp.isNotEmpty()) {
                        Log.i("Verification resp", resp!! + "")
                        val json = JSONArray("[" + resp + "]")
                        val obj1 = json.getJSONObject(0)
                        val sts = obj1.getString("Status").toString()
                        val jarr = obj1.getString("Response")
                        if (sts == "Success") {
                            /*val intent = Intent("clearall")
                        intent.putExtra("message", "clearall")
                        LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)*/
                            pDialog.dismiss()
                            pDialog = SweetAlertDialog(this@Family_Main!!, SweetAlertDialog.SUCCESS_TYPE)
                            pDialog.setTitleText("Account Has Been Created")
                            pDialog.setCancelable(false)
                            pDialog.setContentText("Your A/c no : $account_no")
                            pDialog.setConfirmText("Done")
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFFFCA18"));
                            pDialog.getProgressHelper().setRimColor(Color.parseColor("#FFFFCA18"));
                            pDialog.setConfirmClickListener(object :
                                SweetAlertDialog.OnSweetClickListener {
                                override fun onClick(sDialog: SweetAlertDialog?) {
                                    // reuse previous dialog instance
                                    sDialog!!.dismiss()
                                    /* this@Family_Main!!.setResult(
                                         Activity.RESULT_OK, Intent(
                                             this@Family_Main!!,
                                             GroupMembers::class.java
                                         )
                                     )
                                     this@Family_Main!!.finish()*/
                                }
                            }).show();
                            //Toast.makeText(activity!!,obj1.getString("Message").toString().toString(),Toast.LENGTH_LONG).show()
                            // complete.isEnabled = true
                        } else {
                            //complete.isEnabled = true
                            pDialog.dismiss()
                            Toast.makeText(
                                this@Family_Main!!,
                                obj1.getString("Message").toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else{
                        //complete.isEnabled = true
                        pDialog.dismiss()
                    }
                } else {
                    // complete.isEnabled=true
                    /*pDialog.dismiss()
                    Toast.makeText(
                        this@Family_Main!!,
                        "Oops! Something went wrong please try again.",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            } catch (e: Exception) {
                //complete.isEnabled=true
                //pDialog.dismiss()
                e.printStackTrace()
                Toast.makeText(
                    this@Family_Main!!,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun editarea(id:String){

        if (Appconstants.net_status(this)) {

            val call = ApproveUtils.Get.getMemedit(Appconstants.Domin+"/"+"editview_family/"+id)
            call.enqueue(object : Callback<Resp_Edit> {
                override fun onResponse(call: Call<Resp_Edit>, response: Response<Resp_Edit>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_Edit
                        println(example)
                        if (example.status == "Success") {
                            val objarr=example.message!!.data
                            var ucode= objarr!![0].unitCode
                            var vid = objarr!![0].villageId
                            detailsFragment!!.fid=id
                            detailsFragment!!.vid=vid.toString()
                            var familyheadName= objarr!![0].familyheadName
                            var familyheadAge= objarr!![0].familyheadAge
                            var familyheadOccupation= objarr!![0].familyheadOccupation
                            var familyheadIncome= objarr!![0].familyheadIncome
                            var familyheadSalary= objarr!![0].familyheadSalary
                            var healthIssue= objarr!![0].healthIssue
                            var healthissueDescription= objarr!![0].healthissueDescription
                            var house= objarr!![0].house
                            var houseCondition= objarr!![0].houseCondition
                            val houseImage= objarr!![0].houseImage
                            val dateInstalled= objarr!![0].dateInstalled
                            val fish= objarr!![0].fish
                            val plantSeeded= objarr!![0].plantSeeded
                            val familyImage= objarr!![0].familyImage
                            val unitinstallImage= objarr!![0].unitinstallImage
                            val familly= objarr!![0].familly
                            val status= objarr!![0].status
                            val others= objarr!![0].other_work

                            detailsFragment!!.villageidval=vid.toString()
                            detailsFragment!!.comrmrs.setText(ucode)
                            detailsFragment!!.mname.setText(familyheadName)
                            detailsFragment!!.msurname.setText(familyheadAge)
                            detailsFragment!!.mrationcard.setText(familyheadSalary)
                            detailsFragment!!.nname.setText(dateInstalled)
                            detailsFragment!!.fishtype.setText(fish)
                            detailsFragment!!.mvoterid.setText(healthissueDescription)
                            detailsFragment!!.mrmtypevalue=familyheadOccupation.toString()
                            detailsFragment!!.mgendervalue=familyheadIncome.toString()
                            detailsFragment!!.mmaritalvalue=healthIssue.toString()
                            detailsFragment!!.mhousesitvalue=house.toString()
                            detailsFragment!!.otherrcc.setText(others.toString())
                            detailsFragment!!.add_family.visibility=View.INVISIBLE

                            for(i in 0 until plantSeeded!!.size){
                                val obj=plantSeeded[i].name
                                if(obj.equals(detailsFragment!!.chstem.text.toString())){
                                    detailsFragment!!.chstem.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.chtom.text.toString())){
                                    detailsFragment!!.chtom.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.chcum.text.toString())){
                                    detailsFragment!!.chcum.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.cspain.text.toString())){
                                    detailsFragment!!.cspain.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.chcaps.text.toString())){
                                    detailsFragment!!.chcaps.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.cschilli.text.toString())){
                                    detailsFragment!!.cschilli.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.chmust.text.toString())){
                                    detailsFragment!!.chmust.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.csflower.text.toString())){
                                    detailsFragment!!.csflower.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.chbitt.text.toString())){
                                    detailsFragment!!.chbitt.isChecked=true
                                }
                                if(obj.equals(detailsFragment!!.csgreen.text.toString())){
                                    detailsFragment!!.csgreen.isChecked=true
                                }
                            }


                            try {
                                for (i in 0 until familly!!.size) {
                                    val data = FamilyMember()
                                    data.Fid = i.toString()
                                    data.Name = familly[i].name
                                    if (familly[i].school.toString().equals("")) {
                                        data.school = "Nil"

                                    } else {
                                        data.school = familly[i].school
                                    }

                                    if (familly[i].healthIssue.toString().equals("-SELECT-")) {
                                        data.health = "Nil"

                                    } else {
                                        data.health = familly[i].healthIssue.toString()
                                    }

                                    if (familly[i].healthIssueDescription.toString().equals("")) {
                                        data.healthdesc = "Nil"

                                    } else {
                                        data.healthdesc =
                                            familly[i].healthIssueDescription.toString()
                                    }

                                    if (familly[i].futureGoal.toString().equals("")) {
                                        data.feature = "Nil"

                                    } else {
                                        data.feature = familly[i].futureGoal.toString()
                                    }


                                    data.Relation = familly[i].relation.toString()
                                    data.Age = familly[i].age.toString()
                                    if (familly[i].occupation.toString() == "-SELECT-") {
                                        data.Occuption = "Nil"
                                    } else {
                                        data.Occuption = familly[i].occupation.toString()

                                    }
                                    if (familly[i].monthlyIncome.toString() == "") {
                                        data.Income = "Nil"
                                    } else {
                                        data.Income = familly[i].monthlyIncome.toString()
                                    }

                                    detailsFragment!!.Family.add(data)
                                    detailsFragment!!.family_list.adapter =
                                        FamilyAdapter(this@Family_Main!!, detailsFragment!!.Family)
                                    Helper.setDynamicHeight(detailsFragment!!.family_list)
                                }
                            }
                            catch (e:Exception){

                            }

                            detailsFragment!!.occuparr.add("-SELECT-")
                            detailsFragment!!.occuparr.add("Plumber")
                            detailsFragment!!.occuparr.add("Carpenter")
                            detailsFragment!!.occuparr.add("Mechanic")
                            detailsFragment!!.occuparr.add("Centring")
                            detailsFragment!!.occuparr.add("Housewife")
                            detailsFragment!!.occuparr.add("Electrician")
                            detailsFragment!!.occuparr.add("Shepard")
                            detailsFragment!!.occuparr.add("Others")

                            detailsFragment!!.incomearr1.add("-SELECT-")
                            detailsFragment!!.incomearr1.add("Daily wages")
                            detailsFragment!!.incomearr1.add("Monthly")
                            detailsFragment!!.incomearr1.add("Yearly")
                            detailsFragment!!.incomearr1.add("Season")

                            detailsFragment!!.healtharr2.add("-SELECT-")
                            detailsFragment!!.healtharr2.add("Injuries")
                            detailsFragment!!.healtharr2.add("Disabled")
                            detailsFragment!!.healtharr2.add("Surgery")
                            detailsFragment!!.healtharr2.add("Illness")

                            detailsFragment!!.housearr3.add("-SELECT-")
                            detailsFragment!!.housearr3.add("Own")
                            detailsFragment!!.housearr3.add("Rent")
                            detailsFragment!!.housearr3.add("Condition")

                            detailsFragment!!.housecondition.setText(houseCondition)



                            for(i in 0 until detailsFragment!!.occuparr.size){
                                if(detailsFragment!!.occuparr[i].equals(familyheadOccupation.toString())){
                                    detailsFragment!!.mrmtype.setSelection(i)
                                    if(detailsFragment!!.mrmtype.selectedItem.toString().equals("Others")){
                                        detailsFragment!!.otherrcc.visibility=View.VISIBLE
                                    }
                                }
                            }
                            for(i in 0 until detailsFragment!!.incomearr1.size){
                                if(detailsFragment!!.incomearr1[i].equals(familyheadIncome.toString())){
                                    detailsFragment!!.mgender.setSelection(i)

                                }
                            }
                            for(i in 0 until detailsFragment!!.healtharr2.size){
                                if(detailsFragment!!.healtharr2[i].equals(healthIssue.toString())){
                                    detailsFragment!!.mmarital.setSelection(i)

                                }
                            }
                            for(i in 0 until detailsFragment!!.housearr3.size){
                                if(detailsFragment!!.housearr3[i].equals(house.toString())){
                                    detailsFragment!!.mhousesit.setSelection(i)

                                }
                            }

                            var w = 200
                            var h = 200
                            var conf = Bitmap.Config.ARGB_8888 // see other conf types
                            var bmp = Bitmap.createBitmap(w, h, conf) // this creates a MUTABLE bitmap
                            var canvas = Canvas(bmp)


                            for(h in 0 until houseImage!!.size){

                             authenticationFragment!!.houseimgarr_code.add(houseImage[h].image.toString())
                             authenticationFragment!!.houseimgarr.add(bmp)
                                authenticationFragment!!.adp1edit = MyhouseAdap_Edit(authenticationFragment!!.houseimgarr_code, this@Family_Main!!, authenticationFragment!!.click1edit)
                                authenticationFragment!!.houselist.adapter = authenticationFragment!!.adp1edit
                                authenticationFragment!!.adp1edit.notifyDataSetChanged()


                            }
                            for(f in 0 until familyImage!!.size){
                             authenticationFragment!!.familyimgarr_code.add(familyImage[f].image.toString())
                             authenticationFragment!!.familyimgarr.add(bmp)
                                println("urlfamily"+authenticationFragment!!.familyimgarr_code.toString())
                                authenticationFragment!!.adpedit = MyImageAdap_Edit(authenticationFragment!!.familyimgarr_code, this@Family_Main!!, authenticationFragment!!.clickedit)
                                authenticationFragment!!.familylist.adapter = authenticationFragment!!.adpedit
                                authenticationFragment!!.adpedit.notifyDataSetChanged()

                            }
                            for(u in 0 until unitinstallImage!!.size){
                            authenticationFragment!!.unitimgarr_code.add(unitinstallImage[u].image.toString())
                            authenticationFragment!!.unitimgarr.add(bmp)
                                authenticationFragment!!.adp2edit = MyUnitAdap_Edit(authenticationFragment!!.unitimgarr_code, this@Family_Main!!, authenticationFragment!!.click2edit)
                                authenticationFragment!!.unitlist.adapter = authenticationFragment!!.adp2edit
                                authenticationFragment!!.adp2edit.notifyDataSetChanged()

                                pref = this@Family_Main.getSharedPreferences("MyPref", 0)
                                editor = pref!!.edit()
                                editor!!.putString("unitlist","true")
                                editor!!.commit()

                            }
                            authenticationFragment!!.set()

                        }
                        }

                }

                override fun onFailure(call: Call<Resp_Edit>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            this@Family_Main,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container.visibility=View.GONE
                        shimmer_view_container.stopShimmer()


                    }
                }
            })
        }
        else{
            Toast.makeText(
                this@Family_Main,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container.visibility=View.GONE

        }

    }




    fun getPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.getContentResolver().query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
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
                this,
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

    fun toast(msg:String){
        val toast= Toast.makeText(this@Family_Main,msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }

    override fun onBackPressed() {
        if(viewPager!!.currentItem==1){
            viewPager!!.setCurrentItem(0, true)
            tabLayout!!.getTabAt(0)!!.select()

        }
        else if(viewPager!!.currentItem==0){
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->

                    finish() }
                .setNegativeButton("No", null)
                .show()
        }
    }


}
