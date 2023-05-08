package com.elancier.meenatchi_CRM.Harvest

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.Connection
import com.elancier.meenatchi_CRM.retrofit.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_family__main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Harvest_Main : AppCompatActivity() {
    internal var detailsFragment: MainFragment? = null
    internal var authenticationFragment: PicuresFragment? = null
    internal var viewPager: com.elancier.meenatchi_CRM.Family.Customviewpager? = null
    internal var tabLayout: TabLayout? = null
    internal var adapter: ViewPagerAdapter? = null
    internal lateinit var pDialog : SweetAlertDialog

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
        setContentView(R.layout.activity_harvest__main)

        viewPager = findViewById(R.id.viewpager) as com.elancier.meenatchi_CRM.Family.Customviewpager
        viewPager!!.offscreenPageLimit = 2

        tabLayout = findViewById(R.id.tabs) as TabLayout

        setSupportActionBar(toolbar)
        //toolbar.title = "Application Form"
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        //toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        //}
        val ab=supportActionBar
        ab!!.setTitle("Harvesting Details");
        ab!!.title="Harvesting Details"
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)
        /*tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);*/
        createTabIcons()

        setupViewPager(viewPager!!)

        try{
            val intent=intent.extras
            println("inten"+intent!!.getString("id").toString())
            detailsFragment!!.fid=intent!!.getString("id").toString()
            detailsFragment!!.vid=intent!!.getString("vid").toString()

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


    fun save(){

        var check=""
        println("plantseedarr"+detailsFragment!!.plantseedarr.toString())
        println("plantharvestarr"+detailsFragment!!.plantharvestarr.toString())
        println("typeplant"+detailsFragment!!.typeplant.toString())
        println("qtyplant"+detailsFragment!!.qtyplant.toString())
        println("plantharvestarrfish"+detailsFragment!!.plantharvestarrfish.toString())
        println("seedfish"+detailsFragment!!.seedfish.toString())
        println("qtyfish"+detailsFragment!!.qtyfish.toString())

        if(((detailsFragment!!.plantseedarr!!.isNotEmpty()&&(!detailsFragment!!.plantseedarr!!.contains("")))&&
            (detailsFragment!!.plantharvestarr!!.isNotEmpty()&&(!detailsFragment!!.plantharvestarr!!.contains("")))&&
            (detailsFragment!!.typeplant!!.isNotEmpty()&&(!detailsFragment!!.typeplant!!.contains("")))&&
            (detailsFragment!!.qtyplant!!.isNotEmpty()&&(!detailsFragment!!.qtyplant!!.contains(""))))||
            ((detailsFragment!!.plantharvestarrfish!!.isNotEmpty()&&(!detailsFragment!!.plantharvestarrfish!!.contains("")))&&
            (detailsFragment!!.seedfish!!.isNotEmpty()&&(!detailsFragment!!.seedfish!!.contains("")))&&
            (detailsFragment!!.qtyfish!!.isNotEmpty()&&(!detailsFragment!!.qtyfish!!.contains(""))&&authenticationFragment!!.harvestimgarr_code.isNotEmpty()))
          )
        {

                pDialog = SweetAlertDialog(this@Harvest_Main!!, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFFFCA18"));
                pDialog.setCancelable(false)
                pDialog.setTitleText("Saving...");
                pDialog.show();
            
                Handler().postDelayed({
                    VerificationSend().execute()

                }, 2000)
            }
        else{
            viewPager!!.setCurrentItem(0, true)
            tabLayout!!.getTabAt(0)!!.select()
            toast("Please fill all fields")
            if(authenticationFragment!!.harvestimgarr_code!!.isEmpty()){

                toast("Select or Capture Pictures")

            }


            

        }
    }



    private fun createTabIcons() {

        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabOne.text = "Harvest"
        tabOne.setTextColor(resources.getColor(R.color.white))
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_id_card, 0, 0)
        tabLayout!!.getTabAt(0)!!.customView = tabOne


        val tabTwo = LayoutInflater.from(this@Harvest_Main).inflate(R.layout.custom_tab, null) as TextView
        tabTwo.text = "Pictures"
        tabTwo.setTextColor(resources.getColor(R.color.white))
        //tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_authentication, 0, 0)
        tabLayout!!.getTabAt(1)!!.customView = tabTwo

        /*val tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabTwo.text = "Authentication"
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_authentication, 0, 0)
        tabLayout!!.getTabAt(1)!!.customView = tabTwo*/

        /*val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabThree.text = "Tab 3"
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_order, 0, 0)
        tabLayout!!.getTabAt(2)!!.customView = tabThree*/
    }

    fun set(){
        viewPager!!.setCurrentItem(1, true)
        tabLayout!!.getTabAt(1)!!.select()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        val bundle  = Bundle()
        detailsFragment = MainFragment()

        detailsFragment!!.arguments = bundle
        authenticationFragment = PicuresFragment()
        adapter!!.addFragment(detailsFragment!!, "Personal Details")
        adapter!!.addFragment(authenticationFragment!!, "Authentication")
        viewPager.adapter = adapter

    }
    fun toast(msg:String){
        val toast= Toast.makeText(this@Harvest_Main,msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }
    fun getmns(values: List<String>,values1: List<String>,values2: List<String>,values3: List<String>,pos:Int) {
        detailsFragment!!.plantseedarr   = values as ArrayList<String>
        detailsFragment!!.plantharvestarr=values1 as ArrayList<String>
        detailsFragment!!.typeplant=values2 as ArrayList<String>
        detailsFragment!!.qtyplant=values3 as ArrayList<String>


    }

    fun getmnsrem(pos:Int) {
        println("clik"+"click")
        detailsFragment!!.plantseedarr!!.removeAt(pos)
        detailsFragment!!.plantharvestarr!!.removeAt(pos)
        detailsFragment!!.typeplant!!.removeAt(pos)
        detailsFragment!!.qtyplant!!.removeAt(pos)
        val adap = Planet_adap(detailsFragment!!.villagearr!!,detailsFragment!!.plantseedarr!!,
            detailsFragment!!.plantharvestarr!!, detailsFragment!!.typeplant!!, detailsFragment!!.qtyplant!!, R.layout.incen_item, this!!
        )
        detailsFragment!!.plantlist.adapter = adap
        detailsFragment!!.plantlist.isExpanded=true
        adap.notifyDataSetChanged()

    }

    @SuppressLint("StaticFieldLeak")
    inner class VerificationSend : AsyncTask<String, Void, String>() {
        internal lateinit var pDialo : ProgressDialog

        internal lateinit var account_no : String

        override fun onPreExecute() {

        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg para: String): String? {
            var result: String? = null
            val con = Connection()

            try {
                runOnUiThread {
                    val jobj = JSONObject()
                    //val staff_id = utils.getValue("staff_id")
                    jobj.put(
                        "village_id",
                        detailsFragment!!.vid
                    )
                    jobj.put("family_id", detailsFragment!!.fid)

                    val plantjarr = JSONArray()

                    for (i in 0 until detailsFragment!!.plantseedarr!!.size){
                        val famobj = JSONObject()
                        famobj.put("plant_type", detailsFragment!!.typeplant!![i])
                        famobj.put("seeded_date", detailsFragment!!.plantseedarr!![i])
                        famobj.put("harvest_date", detailsFragment!!.plantharvestarr!![i])
                        famobj.put("harvest_qty", detailsFragment!!.qtyplant!![i])
                        plantjarr.put(famobj)
                    }
                    jobj.put("plant",plantjarr)

                    val fishjarr = JSONArray()

                    for (i in 0 until detailsFragment!!.plantharvestarrfish!!.size){
                        val famobj = JSONObject()
                        famobj.put("fish_type", "Gift Tilapia")
                        famobj.put("seeded_date", detailsFragment!!.seedfish!![i])
                        famobj.put("harvest_date", detailsFragment!!.plantharvestarrfish!![i])
                        famobj.put("harvest_qty", detailsFragment!!.qtyfish!![i])
                        fishjarr.put(famobj)
                    }

                    jobj.put("fish",fishjarr)


                    jobj.put("harvest_image",authenticationFragment!!.harvestimgarr_code.toString().removePrefix("[").removeSuffix("]"))

                    Log.i("Member Input", Appconstants.Memberharvest + "    " + jobj.toString())
                    val result1 = con.sendHttpPostjson2(Appconstants.Memberharvest, jobj, "")
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

                            }
                            else{
                                Toast.makeText(this@Harvest_Main!!,"There is a problem of Member id",Toast.LENGTH_LONG).show()
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
                            pDialog = SweetAlertDialog(this@Harvest_Main!!, SweetAlertDialog.SUCCESS_TYPE)
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
                                    /* this@Harvest_Main!!.setResult(
                                         Activity.RESULT_OK, Intent(
                                             this@Harvest_Main!!,
                                             GroupMembers::class.java
                                         )
                                     )
                                     this@Harvest_Main!!.finish()*/
                                }
                            }).show();
                            //Toast.makeText(activity!!,obj1.getString("Message").toString().toString(),Toast.LENGTH_LONG).show()
                            // complete.isEnabled = true
                        } else {
                            //complete.isEnabled = true
                            pDialog.dismiss()
                            Toast.makeText(
                                this@Harvest_Main!!,
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
                        this@Harvest_Main!!,
                        "Oops! Something went wrong please try again.",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            } catch (e: Exception) {
                //complete.isEnabled=true
                //pDialog.dismiss()
                e.printStackTrace()
                Toast.makeText(
                    this@Harvest_Main!!,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    
    fun getnmsfish(values2: List<String>,values: List<String>,values1: List<String>,pos:Int) {
        detailsFragment!!.plantharvestarrfish   = values as ArrayList<String>
        detailsFragment!!.seedfish   = values2 as ArrayList<String>
        detailsFragment!!.qtyfish=values1 as ArrayList<String>



    }

    fun getnmsfishremove(pos:Int) {
        detailsFragment!!.plantharvestarrfish!!.removeAt(pos)
        detailsFragment!!.seedfish!!.removeAt(pos)
        detailsFragment!!.qtyfish!!.removeAt(pos)

        val adaps =Fish_adap(detailsFragment!!.seedfish!!,detailsFragment!!.plantharvestarrfish!!, detailsFragment!!.qtyfish!!, R.layout.incen_items, this@Harvest_Main)
        detailsFragment!!.fishlist.adapter = adaps
        detailsFragment!!.fishlist.isExpanded=true
        adaps.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.receipt, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
