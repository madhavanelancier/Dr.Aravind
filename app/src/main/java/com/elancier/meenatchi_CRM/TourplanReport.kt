package com.elancier.meenatchi_CRM


import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.elancier.meenatchi_CRM.Adapers.ExecutiveAdapter
import com.elancier.meenatchi_CRM.Adapers.TourReportAdapter
import com.elancier.meenatchi_CRM.DataClass.TourReportData
import com.elancier.meenatchi_CRM.retrofit.*
import kotlinx.android.synthetic.main.activity_tour_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList
import android.os.Build

import android.os.AsyncTask

import android.widget.Toast

import androidx.core.content.FileProvider
import com.elancier.meenatchi_CRM.service.FileDownloader.downloadFile
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import org.json.JSONObject
import java.lang.Exception


class TourplanReport : AppCompatActivity(),TourReportAdapter.OnItemClickListener{
    val activity=this
    val yr =
        arrayOf("Select", "2020-2021",
            "2021-2022",
            "2022-2023")
    val sesn= arrayOf("Select", "Off Season",
    "Mid Season",
    "Season")

    var states = ArrayList<String>()
    var exes = ExecutiveData()
    var exesdisp = ExecutiveData()
    var user =""
    var usermobile =""

    internal  var pref: SharedPreferences?=null
    internal  var editor: SharedPreferences.Editor?=null

    lateinit var pdialog:ProgressDialog

    private val TAG = "TourReport"
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_report)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.title = "Tour Plan Report"

        pdialog=ProgressDialog(this)

        pref = this.getSharedPreferences("MyPref", 0)
        editor = pref!!.edit()

        usermobile=pref!!.getString("mobile","").toString()

        
        getWindow().setSoftInputMode(
            WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        cloudinary()

        val adp1 = ArrayAdapter<String>(this, R.layout.dropdown_item, yr)
        adp1.setDropDownViewResource(R.layout.dropdown_item)
        year.setAdapter(adp1)

        val adp2 = ArrayAdapter<String>(this, R.layout.dropdown_item, sesn)
        adp2.setDropDownViewResource(R.layout.dropdown_item)
        season.setAdapter(adp2)


        submitbutton.setOnClickListener { Submit() }

        /*state.onItemClickListener=object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                println("state : "+states[position])
                state.setText(states[position])
                //val adps = StateAdapter(activity, states)
                //adp1.setDropDownViewResource(R.layout.dropdown_item)
                val adps = ArrayAdapter<String>(activity, R.layout.dropdown_item, states)
                adps.setDropDownViewResource(R.layout.dropdown_item)
                state.setAdapter(adps)
            }


        }*/
        state.setTokenizer(CommaTokenizer())
        executive.onItemClickListener=object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val e = exesdisp[position]
                user= e.id.toString()
                println("exe : "+e.first_name+" "+e.last_name+"-"+e.mobile)
                executive.setText(e.first_name+" "+e.last_name+"-"+e.mobile)
                val adpe = ExecutiveAdapter(activity, exesdisp)
                //adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                executive.setAdapter(adpe)
            }


        }
        swiperefresh.setOnRefreshListener {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout")

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            Submit()
        }

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


    fun cloudinary(){
        if (Appconstants.net_status(this)) {
            val call = ApproveUtils.Get.getState()
            call.enqueue(object : Callback<StateData> {
                override fun onResponse(call: Call<StateData>, response: Response<StateData>) {
                    Log.e("state responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as StateData
                        println(example)
                        if (example.status == "Success") {
                            //states = example.response as ArrayList<StateData.StateResponse>
                            example.response!!.forEach { y->
                                states.add(y.state_name.toString())
                            }
                            val adps = ArrayAdapter<String>(activity, R.layout.dropdown_item, states)
                            adps.setDropDownViewResource(R.layout.dropdown_item)
                           /* val adp1 = StateAdapter(activity,
                                states
                            )*/
                            //adp1.setDropDownViewResource(R.layout.dropdown_item)
                            state.setAdapter(adps)

                        } else {
                       
                        }
                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<StateData>, t: Throwable) {
                    Log.e("state Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            val call2 = ApproveUtils.Get.getExecutives()
            call2.enqueue(object : Callback<ExecutiveData> {
                override fun onResponse(call: Call<ExecutiveData>, response: Response<ExecutiveData>) {
                    Log.e("exe responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as ExecutiveData
                        println(example)
                        exes = example
                        for (j in 0 until exes.size){
                            if(exes[j].mobile==usermobile) {
                                user=exes[j].id.toString()
                                var data=ExecutiveDatum()
                                data.first_name=exes[j].first_name
                                data.last_name=exes[j].last_name
                                data.mobile=exes[j].mobile
                                data.id=exes[j].id
                                exesdisp.add(data)
                                val adp1 = ExecutiveAdapter(activity, exesdisp)
                                //adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                executive.setAdapter(adp1)
                                executive.setText(exes[j].first_name+" "+exes[j].last_name+"-"+exes[j].mobile)
                                val adpe = ExecutiveAdapter(activity, exesdisp)
                                //adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                executive.setAdapter(adpe)
                                Submit()


                            }
                        }


                    }
                    else{

                    }
                }

                override fun onFailure(call: Call<ExecutiveData>, t: Throwable) {
                    Log.e("exe Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
        else{
            Toast.makeText(
                activity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()

        }
    }




    fun toast(msg:String){
        val toast= Toast.makeText(this,msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,0)
        toast.show()
    }



    override fun onResume() {
        super.onResume()

    }


    fun Submit(){
        if (Appconstants.net_status(this)) {
            val pro = ProgressDialog(activity)
            pro.setCancelable(false)
            pro.setTitle("Loading")
            pro.show()

            var years=""
            var seasons=""
            var states=""
            if(year.text.toString().isNullOrEmpty()){
                years=""
            }
            else{
                years=year.text.toString()
            }

            if(season.text.toString().isNullOrEmpty()){
                seasons=""
            }
            else{
                seasons=season.text.toString()
            }

            if(state.text.toString().isNullOrEmpty()){
                states=""
            }
            else{
                states=state.text.toString()
            }


            val ob = JSONObject()
            ob.put("year",years)
            ob.put("user",user)
            ob.put("season_type",seasons)
            ob.put("state",states)
            println("tour : "+ob)
            val call = ApproveUtils.Get.gettourreport(year.text.toString(),user,season.text.toString(),state.text.toString())
            call.enqueue(object : Callback<TourReportData> {
                override fun onResponse(call: Call<TourReportData>, response: Response<TourReportData>) {
                    Log.e("report resp", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as TourReportData
                        println(example)
                        val adpt = TourReportAdapter(activity,example,activity)
                        reportlist.adapter=adpt
                    }
                    pro.dismiss()
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<TourReportData>, t: Throwable) {
                    Log.e("report Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    pro.dismiss()
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            Toast.makeText(
                activity,
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            //shimmer_view_container.visibility=View.GONE
            swiperefresh.isRefreshing=false
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun OnItemPDFClick(view: View, position: Int,file: String, viewType: Int) {
        download(view,file)
        pdialog!!.setMessage("Downloading...")
        pdialog!!.show()
    }

    fun request(view: View?) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 112)
    }

    fun view(file: String?) {
        Log.v(TAG, "view() Method invoked ")
        if (!hasPermissions(activity, *PERMISSIONS)) {
            Log.v(TAG, "download() Method DON'T HAVE PERMISSIONS ")
            val t = Toast.makeText(
                applicationContext,
                "You don't have read access !",
                Toast.LENGTH_LONG
            )
            t.show()
        } else {
            var pathname: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pathname =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath.replace(
                        "file://",
                        ""
                    )
            }
            val file = File(pathname!!.replace("//", "/") + "/Vanitha/" + file)
            Log.i("utilsssspathh", file.absolutePath + "         " + file.path)


            val target = Intent(Intent.ACTION_VIEW)
            // target.setDataAndType(Uri.fromFile(file),"application/pdf");
            //target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            // target.setDataAndType(Uri.fromFile(file),"application/pdf");
            //target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            target.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            val apkURI = FileProvider.getUriForFile(
                this,
                applicationContext
                    .packageName + ".provider", file
            )
            target.setDataAndType(apkURI, "application/pdf")
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val intent = Intent.createChooser(target, "Open File")
            try {
                startActivity(intent)
            } catch (e: Exception) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
        Log.v(TAG, "view() Method completed ")
    }

    fun download(view: View?,f:String) {
        Log.v(TAG, "download() Method invoked ")
        if (!hasPermissions(activity, *PERMISSIONS)) {
            Log.v(TAG, "download() Method DON'T HAVE PERMISSIONS ")
            val t = Toast.makeText(
                applicationContext,
                "You don't have write access !",
                Toast.LENGTH_LONG
            )
            t.show()
        } else {
            Log.v(TAG, "download() Method HAVE PERMISSIONS ")

            //new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");
            DownloadFile().execute("https://teamdev.co.in/vanitha/"+f, ""+(System.currentTimeMillis()/1000)+".pdf")
        }
        Log.v(TAG, "download() Method completed ")
    }

    inner class DownloadFile :
        AsyncTask<String?, Void?, String?>() {


        protected override fun doInBackground(vararg strings: String?): String? {

            Log.v(TAG, "doInBackground() Method invoked ")
            val fileUrl = strings[0] // -> http://maven.apache.org/maven-1.x/maven.pdf
            val fileName = strings[1] // -> maven.pdf
            val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
            val folder =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/Vanitha")

            //If File is not present create directory
            if (!folder.exists()) {
                folder.mkdirs()
                Log.e("File", "Directory Created.")
            }

            val pdfFile = File(folder, fileName)
            Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.absolutePath)
            Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.absoluteFile)
            try {
                pdfFile.createNewFile()
                Log.v(TAG, "doInBackground() file created$pdfFile")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "doInBackground() error" + e.message)
                Log.e(TAG, "doInBackground() error" + e.stackTrace)
            }
            if (fileUrl != null) {
                if(pdfFile.exists()) {
                    downloadFile(fileUrl, pdfFile)
                }
                else{
                    val folder1 =
                        File(Environment.getExternalStorageDirectory().toString()+"/Vanitha")

                    //If File is not present create directory
                    if (!folder1.exists()) {
                        folder1.mkdirs()
                        Log.e("File", "Directory Created.")
                    }

                    val pdfFile1 = File(folder, fileName)
                    Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.absolutePath)
                    Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.absoluteFile)
                    try {
                        pdfFile1.createNewFile()
                        Log.v(TAG, "doInBackground() file created$pdfFile1")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.e(TAG, "doInBackground() error" + e.message)
                        Log.e(TAG, "doInBackground() error" + e.stackTrace)
                    }

                    if(pdfFile1.exists()) {
                        downloadFile(fileUrl, pdfFile)
                    }
                }
            }
            Log.v(TAG, "doInBackground() file download completed")

            return fileName
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            view(result)
            pdialog.dismiss()

            Toast.makeText(activity,"File Downloaded in Downloads Folder", Toast.LENGTH_SHORT).show()

        }
    }
}
/*[
    {
        "date": "04-01-2022",
        "tripId": "V00106",
        "name": "SEKAR P",
        "area": "Tamil Nadu",
        "from": "04-01-2022",
        "to": "08-01-2022",
        "target": "100000",
        "budget": "12000",
        "download": "public/tour_plan.php?trip=131"
    }
]
*/

