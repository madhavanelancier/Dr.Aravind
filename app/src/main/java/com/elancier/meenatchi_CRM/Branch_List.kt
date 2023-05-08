package com.elancier.meenatchi_CRM

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_trips_new.*


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Branch_List : AppCompatActivity() {
    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    internal lateinit var fname: Spinner
    internal lateinit var fcode: EditText
    internal lateinit var fage: EditText
    var CentresArrays = ArrayList<OrderDetail>()
    var CentresArraysdup = ArrayList<OrderDetail>()

    private val view_pager: ViewPager? = null
    private val tab_layout: TabLayout? = null

    lateinit var adp: MyBranchAdap
    lateinit var adp1: MyBranchAdap

    lateinit var click: MyBranchAdap.OnItemClickListener
    var pop: AlertDialog? = null

    internal lateinit var histlist: ListView
    internal var catidarr: MutableList<String> = ArrayList()
    internal var nmarr: MutableList<String> = ArrayList()
    internal var staffnmarrdup: MutableList<String> = ArrayList()
    internal var imgarr: MutableList<String> = ArrayList()
    internal lateinit var progbar: Dialog
    internal lateinit var checklist: MutableList<String>

    var update = ""
    var catid = ""
    var catnm = ""
    internal var cdarr: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips_new)


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        initToolbar()

        setupViewPager(Home_Page_View)

        Home_tabs.setupWithViewPager(Home_Page_View);


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
        //var type = pref.getString("id", "")


        click = object : MyBranchAdap.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }

        }

    }
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Trips")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter: SectionsPagerAdapter =
         SectionsPagerAdapter(
                supportFragmentManager
            )
        adapter.addFragment(FragmentCurrentTrips(), "Current Trips")
        adapter.addFragment(FragmentMusicSong(), "Upcoming Trips")


        viewPager.adapter = adapter
    }

    private class SectionsPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
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
        val toast = Toast.makeText(this@Branch_List, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun editarea(brcode: String, anm: String, acode: String, aid: String, short: String) {
        var brid = ""
        var nmarr = ArrayList<String>()
        var idarr = ArrayList<String>()
        nmarr.add("Select")
        idarr.add("0")

        val dialogBuilder = AlertDialog.Builder(this@Branch_List)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this@Branch_List.getLayoutInflater()
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
                progbar = Dialog(this@Branch_List)
                progbar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progbar.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progbar.setContentView(R.layout.save)
                progbar.setCancelable(false)
                progbar.show()
                update = ""

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

    }
}
