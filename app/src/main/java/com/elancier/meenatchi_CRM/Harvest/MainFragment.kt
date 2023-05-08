package com.elancier.meenatchi_CRM.Harvest

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList
import android.widget.RadioButton
import android.widget.Toast
import android.widget.RadioGroup

import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elancier.meenatchi_CRM.DataClasses.FamilyMember
import com.elancier.meenatchi_CRM.DataClasses.SpinnerPojo
import com.elancier.meenatchi_CRM.DataClasses.VerificationData
import com.elancier.meenatchi_CRM.ExpandableHeightGridView
import com.elancier.meenatchi_CRM.Family.SpinAdapter
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_harvest
import com.elancier.meenatchi_CRM.retrofit.Utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {
    lateinit var verificationData: VerificationData
    lateinit var utils: Utils
    val RequestPermissionCode = 7
    lateinit var rootView:View
    lateinit var member: TextView
    lateinit var qr: TextView
    lateinit var member_details:LinearLayout
    lateinit var co_applicant:TextView
    lateinit var co_applicant_details:LinearLayout
    lateinit var other:TextView
    lateinit var other_details:LinearLayout
    lateinit var family:TextView
    lateinit var family_details:LinearLayout
    lateinit var family_list:ListView

    lateinit var add_family:FloatingActionButton
    lateinit var scroll:ScrollView
    lateinit var acode:Spinner
    lateinit var gcode:Spinner
    lateinit var gname:EditText
    lateinit var mname:EditText
    lateinit var mmob:EditText
    lateinit var mdob:TextView
    lateinit var mrationcard:EditText
    lateinit var mvoterid:EditText
    lateinit var maadhaar:EditText
    lateinit var mrelign:Spinner
    lateinit var mrmrs:Spinner
    lateinit var msurname:EditText
    lateinit var minit:EditText
    lateinit var mrmtype:Spinner
    lateinit var mgender:Spinner
    lateinit var mmarital:Spinner
    lateinit var mlbknm:EditText
    lateinit var mlbkac:EditText
    lateinit var mlbkbr:EditText
    lateinit var mlifsc:EditText
    lateinit var expamnt:EditText
    lateinit var mcomunity:Spinner
    lateinit var mlnamnt:EditText
    lateinit var incomeamnt:EditText
    lateinit var nname:EditText
    lateinit var nmob:EditText
    lateinit var nrelation:Spinner
    lateinit var ndob:TextView
    lateinit var nvoterid:EditText
    lateinit var naadhaar:EditText
    lateinit var nbusi:EditText
    lateinit var nyerofbusi:EditText
    lateinit var mhouse:EditText
    lateinit var mbirthplc:EditText
    lateinit var maddress:EditText
    lateinit var mcaddress:EditText
    lateinit var comrmrs:EditText
    lateinit var cosurname:Spinner
    lateinit var coinit:EditText
    lateinit var cogender:Spinner
    lateinit var permst:Spinner
    lateinit var permpin:EditText
    lateinit var commst:Spinner
    lateinit var commpin:EditText
    lateinit var loancat:Spinner
    lateinit var loanpurpose:EditText
    lateinit var relerrorstate:TextView
    lateinit var relerrorcomstate:TextView
    lateinit var relerrorlncat:TextView
    lateinit var relerrorlnpur:TextView
    lateinit var mscanaadhaar:CheckBox
    lateinit var coscanaadhaar:CheckBox
    lateinit var same_as:CheckBox
    lateinit var select:RadioGroup
    lateinit var leader:RadioButton
    lateinit var sleader:RadioButton
    lateinit var membe:RadioButton
    lateinit var acodeerror:TextView
    lateinit var gcodeerror:TextView
    lateinit var relerror:TextView
    lateinit var comerror:TextView
    lateinit var relerrormrmrs:TextView
    lateinit var relerrorgender:TextView
    lateinit var relerrormarital:TextView
    lateinit var relerrorcomrmrs:TextView
    lateinit var relerrorcorel:TextView
    lateinit var tt6:TextView
    lateinit var tt7:TextView
    lateinit var imageButton4:TextView
    lateinit var imageButtonfish:TextView
    lateinit var plantlist:ExpandableHeightGridView
    lateinit var fishlist:ExpandableHeightGridView
    internal var villagearr= java.util.ArrayList<SpinnerPojo>()

    lateinit var relerrorcogender:TextView
    lateinit var next:CardView

    var fid=""
    var vid=""


    private var fromDatePickerDialog: DatePickerDialog? = null

    var Family = ArrayList<FamilyMember>()
    lateinit var areaAdapter: SpinAdapter
    lateinit var stateAdapter: SpinAdapter

    lateinit var spindata: ArrayList<SpinnerPojo>
    lateinit var spindatastate: ArrayList<SpinnerPojo>

    lateinit var grpdata : ArrayList<SpinnerPojo>
    var community= ArrayList<SpinnerPojo>()
    var religion = ArrayList<SpinnerPojo>()

    lateinit var otherslay:LinearLayout


    var mrmrsarr=ArrayList<String>()
    var mrmtypearr=ArrayList<String>()
    var mgenderarr=ArrayList<String>()
    var mmaritalarr=ArrayList<String>()
    var comrmrsarr=ArrayList<String>()
    var nerlationarr=ArrayList<String>()

    var plantseedarr:ArrayList<String>?= null
    var plantharvestarr:ArrayList<String>?= null
    var typeplant:ArrayList<String>?= null
    var qtyplant:ArrayList<String>?= null


    var plantharvestarrfish:ArrayList<String>?= null
    var qtyfish:ArrayList<String>?= null
    var seedfish:ArrayList<String>?= null


    var cogenderarr=ArrayList<String>()
    var permstarr=ArrayList<String>()
    var commstarr=ArrayList<String>()
    var loancatarr=ArrayList<String>()

    lateinit var context:Harvest_Main
    var lead = "Member"
    var which = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        utils = Utils(activity!!)
        context = Harvest_Main()
        verificationData = VerificationData()
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.family_details_two, container, false)
        scroll = rootView.findViewById(R.id.scroll)
        qr = rootView.findViewById(R.id.qr)
        member = rootView.findViewById(R.id.member)
        member_details = rootView.findViewById(R.id.plant_details)
        co_applicant = rootView.findViewById (R.id.co_applicant)
        co_applicant_details = rootView.findViewById(R.id.fish_details)
        other = rootView.findViewById(R.id.other)
        other_details = rootView.findViewById(R.id.other_details)
        family = rootView.findViewById(R.id.family)
        family_details = rootView.findViewById(R.id.family_details)
        add_family = rootView.findViewById(R.id.add_family)
        family_list = rootView.findViewById(R.id.family_list)
        imageButton4=rootView.findViewById(R.id.imageButton4)
        imageButtonfish=rootView.findViewById(R.id.imageButtonfish)
        acode = rootView.findViewById(R.id.acode)
        //gcode = rootView.findViewById(R.id.gcode)
        gname = rootView.findViewById(R.id.gname)
        mname = rootView.findViewById(R.id.mname)
        mmob = rootView.findViewById(R.id.mmob)
        mdob = rootView.findViewById(R.id.mdob)
        mrationcard = rootView.findViewById(R.id.mrationcard)
        otherslay=rootView.findViewById(R.id.otherslay)
        mvoterid = rootView.findViewById(R.id.mvoterid)
        fishlist=rootView.findViewById(R.id.fishlist)
        //maadhaar = rootView.findViewById(R.id.maadhaar)
        //mrelign = rootView.findViewById(R.id.mrelign)
        //mcomunity = rootView.findViewById(R.id.mcomunity)
        //mlnamnt = rootView.findViewById(R.id.mlnamnt)
        //incomeamnt = rootView.findViewById(R.id.incomeamnt)
        nname = rootView.findViewById(R.id.nname)
        //nmob = rootView.findViewById(R.id.nmob)
        //nrelation = rootView.findViewById(R.id.nrelation)
        //ndob = rootView.findViewById(R.id.ndob)
        //nvoterid = rootView.findViewById(R.id.nvoterid)
        //naadhaar = rootView.findViewById(R.id.naadhaar)
        //nbusi = rootView.findViewById(R.id.mbuis)
        //nyerofbusi = rootView.findViewById(R.id.myrbuis)
        mhouse = rootView.findViewById(R.id.mhouse)
        mbirthplc = rootView.findViewById(R.id.mbirthplc)
        maddress = rootView.findViewById(R.id.maddress)
        mcaddress = rootView.findViewById(R.id.mcaddress)
        same_as = rootView.findViewById(R.id.same_as)

        tt6= rootView.findViewById(R.id.textView6)
        tt7= rootView.findViewById(R.id.textView7)
        tt6.isSelected=true
        tt7.isSelected=true
        acodeerror = rootView.findViewById(R.id.acodeerror)

        relerrorgender=rootView.findViewById(R.id.relerrorgender)
        relerrormarital=rootView.findViewById(R.id.relerrormarital)
        plantlist=rootView.findViewById(R.id.plantlist)


        next = rootView.findViewById(R.id.next) as CardView


        //Newly added fields
        //mrmrs=rootView.findViewById(R.id.mrmrs) as Spinner
        msurname=rootView.findViewById(R.id.msurname) as EditText
        minit=rootView.findViewById(R.id.minit) as EditText
        mrmtype=rootView.findViewById(R.id.mrmtype) as Spinner
        mgender=rootView.findViewById(R.id.mgender) as Spinner
        mmarital=rootView.findViewById(R.id.mmarital) as Spinner
        //mlbknm=rootView.findViewById(R.id.mlbknm) as EditText
        //mlbkac=rootView.findViewById(R.id.mlbkac) as EditText
        //mlbkbr=rootView.findViewById(R.id.mlbkbr) as EditText
        //mlifsc=rootView.findViewById(R.id.mlifsc) as EditText
        //expamnt=rootView.findViewById(R.id.expamnt) as EditText

        comrmrs=rootView.findViewById(R.id.comrmrs) as EditText
        cosurname=rootView.findViewById(R.id.cosurname) as Spinner
        //coinit=rootView.findViewById(R.id.coinit) as EditText
        ///cogender=rootView.findViewById(R.id.cogender) as Spinner

        relerrorcomrmrs=rootView.findViewById(R.id.relerrorcomrmrs) as TextView
        //relerrorcorel=rootView.findViewById(R.id.relerrorcorel) as TextView
       // relerrorcogender=rootView.findViewById(R.id.relerrorcogender) as TextView
        permst=rootView.findViewById(R.id.permst) as Spinner
        permpin=rootView.findViewById(R.id.permpin) as EditText
        commst=rootView.findViewById(R.id.commst) as Spinner
        commpin=rootView.findViewById(R.id.commpin) as EditText
        loancat=rootView.findViewById(R.id.loancat) as Spinner
        loanpurpose=rootView.findViewById(R.id.loanpurpose) as EditText
        relerrorstate=rootView.findViewById(R.id.relerrorstate) as TextView
        relerrorcomstate=rootView.findViewById(R.id.relerrorcomstate) as TextView
        relerrorlncat=rootView.findViewById(R.id.relerrorlncat) as TextView
        relerrorlnpur=rootView.findViewById(R.id.relerrorlnpur) as TextView

        plantseedarr= ArrayList<String>()
        plantharvestarr= ArrayList<String>()
        typeplant= ArrayList<String>()
        qtyplant= ArrayList<String>()
        plantharvestarrfish= ArrayList<String>()
        qtyfish= ArrayList<String>()
         seedfish= ArrayList<String>()

        next.setOnClickListener {
           // (Verification()).setTab()

            (activity as Harvest_Main).set()
        }


        var com = SpinnerPojo()
        com.id = "0"
        com.name = "Select"
        community.add(com)
        com = SpinnerPojo()
        com.id = "1"
        com.name = "OC"
        community.add(com)
        com = SpinnerPojo()
        com.id = "2"
        com.name = "BC"
        community.add(com)
        com = SpinnerPojo()
        com.id = "3"
        com.name = "MBC"
        community.add(com)
        com = SpinnerPojo()
        com.id = "4"
        com.name = "SC"
        community.add(com)
        com = SpinnerPojo()
        com.id = "5"
        com.name = "ST"
        community.add(com)
        //mcomunity.adapter = SpinAdapter(activity!!,R.layout.tax_spinner_list_item,community)
        var rel = SpinnerPojo()
        rel.id = "0"
        rel.name = "Select"
        religion.add(rel)
        rel = SpinnerPojo()
        rel.id = "1"
        rel.name = "Hindu"
        religion.add(rel)
        rel = SpinnerPojo()
        rel.id = "2"
        rel.name = "Muslim"
        religion.add(rel)
        rel = SpinnerPojo()
        rel.id = "3"
        rel.name = "Christian"
        religion.add(rel)

        rel = SpinnerPojo()
        rel.id = "4"
        rel.name = "Sikh"
        religion.add(rel)

        rel = SpinnerPojo()
        rel.id = "5"
        rel.name = "Buddhist"
        religion.add(rel)

        rel = SpinnerPojo()
        rel.id = "6"
        rel.name = "Jain"
        religion.add(rel)

        rel = SpinnerPojo()
        rel.id = "7"
        rel.name = "Bahai"
        religion.add(rel)

        rel = SpinnerPojo()
        rel.id = "8"
        rel.name = "Others"
        religion.add(rel)

        rel = SpinnerPojo()
        rel.id = "9"
        rel.name = "Religion not stated"
        religion.add(rel)

        //mrelign.adapter = SpinAdapter(activity!!,R.layout.tax_spinner_list_item,religion)

        spindata = ArrayList<SpinnerPojo>()
        val bo = SpinnerPojo()
        bo.id = ""
        bo.name =""
        spindata.add(bo)
        //areaAdapter = SpinAdapter(activity!!, R.layout.tax_spinner_list_item, spindata)
        //acode.adapter = areaAdapter


        mrmrsarr.add("Select")
        mrmrsarr.add("Mr")
        mrmrsarr.add("Mrs")
        mrmrsarr.add("Ms")

        mrmtypearr.add("Select")
        mrmtypearr.add("Residence")
        mrmtypearr.add("Company")
        mrmtypearr.add("Mobile")
        mrmtypearr.add("Permanent")
        mrmtypearr.add("Other")
        mrmtypearr.add("Un tagged")

        mgenderarr.add("Select")
        mgenderarr.add("Male")
        mgenderarr.add("Female")

        cogenderarr.add("Select")
        cogenderarr.add("Male")
        cogenderarr.add("Female")

        mmaritalarr.add("Select")
        mmaritalarr.add("Married")
        mmaritalarr.add("Separated")
        mmaritalarr.add("Divorced")
        mmaritalarr.add("Widowed")
        mmaritalarr.add("Unmarried")
        mmaritalarr.add("Untagged")

        comrmrsarr.add("Select")
        comrmrsarr.add("Mr")
        comrmrsarr.add("Mrs")
        comrmrsarr.add("Ms")

        nerlationarr.add("Select")
        nerlationarr.add("Father")
        nerlationarr.add("Husband")
        nerlationarr.add("Mother")
        nerlationarr.add("Son")
        nerlationarr.add("Daughter")
        nerlationarr.add("Wife")
        nerlationarr.add("Brother")
        nerlationarr.add("Mother-In-law")
        nerlationarr.add("Father-In-law")
        nerlationarr.add("Daugther-In-law")
        nerlationarr.add("Sister-In-law")
        nerlationarr.add("Son-In-law")
        nerlationarr.add("Son-In-Brother-In-law")
        nerlationarr.add("Other")


        loancatarr.add("Select")
        loancatarr.add("JLG Group")
        loancatarr.add("JLG Individual")
        loancatarr.add("Individual")
        loancatarr.add("SHG")

        plantseedarr!!.add("")
        plantharvestarr!!.add("")
        typeplant!!.add("")
        qtyplant!!.add("")

        getVillage()




        plantharvestarrfish!!.add("")
        qtyfish!!.add("")
        seedfish!!.add("")

        val adaps =Fish_adap(seedfish!!,plantharvestarrfish!!,qtyfish!!, R.layout.incen_items, activity!!)
        fishlist.adapter = adaps
        fishlist.isExpanded=true
        adaps.notifyDataSetChanged()

        imageButton4.setOnClickListener {
            try {

                    plantseedarr!!.add("")
                    plantharvestarr!!.add("")
                    typeplant!!.add("")
                    qtyplant!!.add("")

                    val adap = Planet_adap(
                        villagearr!!, plantseedarr!!,
                        plantharvestarr!!, typeplant!!, qtyplant!!, R.layout.incen_item, activity!!
                    )
                    plantlist.adapter = adap
                    plantlist.isExpanded = true
                    adap.notifyDataSetChanged()



            }
            catch (e: java.lang.Exception){
                Toast.makeText(activity,"Unable to add",Toast.LENGTH_SHORT).show()
            }
        }


        imageButtonfish.setOnClickListener {
            try {

                seedfish!!.add("")
                plantharvestarrfish!!.add("")
                qtyfish!!.add("")
                    val adaps = Fish_adap(
                        seedfish!!,
                        plantharvestarrfish!!,
                        qtyfish!!,
                        R.layout.incen_items,
                        activity!!
                    )
                    fishlist.adapter = adaps
                    fishlist.isExpanded = true
                    adaps.notifyDataSetChanged()


            }
            catch (e: java.lang.Exception){
                Toast.makeText(activity,"Unable to add",Toast.LENGTH_SHORT).show()
            }
        }




        grpdata = ArrayList<SpinnerPojo>()
        val bo2 = SpinnerPojo()
        bo2.id = ""
        bo2.name = ""
        grpdata.add(bo2)
        //val groupAdapter = SpinAdapter(activity!!, R.layout.tax_spinner_list_item, grpdata)
        //gcode.adapter = groupAdapter


        //val mrAdapter = ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, mrmrsarr)
        //mrmrs.adapter = mrAdapter


        //val mgenderarradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, mgenderarr)
        //mgender.adapter = mgenderarradap

        //val mrmtypearradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, mrmtypearr)
        //mrmtype.adapter = mrmtypearradap

        //val cogenderarradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, cogenderarr)
        //cogender.adapter = cogenderarradap

        //val mmaritalarradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, mmaritalarr)
        //mmarital.adapter = mmaritalarradap



        //val nerlationarradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, nerlationarr)
        //nrelation.adapter = nerlationarradap

        //val loancatarradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, loancatarr)
        //loancat.adapter = loancatarradap








        //Areacodes().execute()

        acode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (spindata[acode.selectedItemPosition].id !="0"){
                    acodeerror.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        try {
            mrmtype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    println("position : " + position)
                    //Groupcodes().execute(spindata[position].id)
                    if (mrmtype.selectedItem=="Others") {
                        otherslay.visibility=View.VISIBLE
                    }
                    else{
                        otherslay.visibility=View.GONE

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
        catch (e:Exception){

        }

       /* try {
            mrelign.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    println("position : " + position)
                    //Groupcodes().execute(spindata[position].id)
                    if (mrelign.selectedItemPosition != 0) {
                        relerror.error = null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
        catch (e:Exception){

        }*/

       /* try {
            mcomunity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    println("position : " + position)
                    //Groupcodes().execute(spindata[position].id)
                    if (mcomunity.selectedItemPosition != 0) {
                        comerror.error = null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
        catch (e:Exception){

        }*/




        try{
        mgender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (mgender.selectedItemPosition!=0){
                    relerrorgender.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        }
        catch (e:Exception){

        }

        try{
        mmarital.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (mmarital.selectedItemPosition!=0){
                    relerrormarital.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        }
        catch (e:Exception){

        }


        /*try{
        comrmrs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (comrmrs.selectedItemPosition!=0){
                    relerrorcomrmrs.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        }
        catch (e:Exception){

        }*/

        try{
        cogender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (cogender.selectedItemPosition!=0){
                    relerrorcogender.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        }
        catch (e:Exception){

        }



        try{
        loancat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println("position : "+position)
                //Groupcodes().execute(spindata[position].id)
                if (loancat.selectedItemPosition!=0){
                    relerrorlncat.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        }
        catch (e:Exception){

        }

/*
        gcode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (grpdata[gcode.selectedItemPosition].id !="0"){
                    gcodeerror.error=null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
*/




        same_as.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                mcaddress.setText(maddress.text.toString())
                if (mcaddress.text.isEmpty()){
                    mcaddress.error = "Enter the field"
                }else{
                    mcaddress.error = null
                }
            }else{
                mcaddress.setText("")
            }
        }
        maddress.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (same_as.isChecked){
                    mcaddress.setText(maddress.text.toString())
                }
            }

        })

       /* mscanaadhaar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                if (CheckingPermissionIsEnabledOrNot()) {
                   // startActivityForResult(Intent(activity!!, QrScanner::class.java), 139)
                }else {
                    RequestMultiplePermission()
                }
            }
        }
        coscanaadhaar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                if (CheckingPermissionIsEnabledOrNot()) {
                   // startActivityForResult(Intent(activity!!, QrScanner::class.java), 149)
                }else {
                    RequestMultiplePermission()
                }
            }
        }

        qr.setOnClickListener {

        }*/



        member.setOnClickListener {view->
            if (member_details.visibility==View.VISIBLE){
                member_details()
                which = "me"
                member_details.visibility=View.GONE
                //member.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
            }else if (member_details.visibility==View.GONE){
                member_details.visibility=View.VISIBLE
                mname.requestFocus()
                scroll.scrollTo(0,0)
                if (co_applicant_details.visibility==View.VISIBLE){
                    coapplicant_details()
                    which = "co"
                }
                if (other_details.visibility==View.VISIBLE){
                    other_details()
                    which = "ot"
                }
                if (family_details.visibility==View.VISIBLE){
                    family_details()
                    which = "fam"
                }
                co_applicant_details.visibility=View.GONE
                other_details.visibility=View.GONE
                family_details.visibility=View.GONE

                //member.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24px, 0);
            }
        }
        co_applicant.setOnClickListener {
            if (co_applicant_details.visibility==View.VISIBLE){
                coapplicant_details()
                which = "co"
                co_applicant_details.visibility=View.GONE
                //co_applicant.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
            }else if (co_applicant_details.visibility==View.GONE){
                co_applicant_details.visibility=View.VISIBLE
                nname.requestFocus()
                scroll.scrollTo(0,0)
                if (member_details.visibility==View.VISIBLE){
                    member_details()
                    which = "me"
                }
                if (other_details.visibility==View.VISIBLE){
                    other_details()
                    which = "ot"
                }
                if (family_details.visibility==View.VISIBLE){
                    family_details()
                    which = "fam"
                }
                member_details.visibility=View.GONE
                other_details.visibility=View.GONE
                family_details.visibility=View.GONE
                //co_applicant.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24px, 0);
            }
        }

        family.setOnClickListener {
            if (family_details.visibility==View.VISIBLE){
                family_details()
                family_details.visibility=View.GONE
                //family.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
            }else if (family_details.visibility==View.GONE){
                family_details.visibility=View.VISIBLE
                if (member_details.visibility==View.VISIBLE){
                    member_details()
                    which = "me"
                }
                if (co_applicant_details.visibility==View.VISIBLE){
                    coapplicant_details()
                    which = "co"
                }
                if (other_details.visibility==View.VISIBLE){
                    other_details()
                    which = "ot"
                }
                member_details.visibility=View.GONE
                co_applicant_details.visibility=View.GONE
                other_details.visibility=View.GONE
                //family.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24px, 0);
            }
        }
       /* add_family.setOnClickListener {
            val openwith = android.app.AlertDialog.Builder(activity)
            val popUpView = layoutInflater.inflate(R.layout.familydetails_popup, null)
            val fname = popUpView.findViewById(R.id.fname) as EditText
            val frelation = popUpView.findViewById(R.id.frelation) as EditText
            val fage = popUpView.findViewById(R.id.fage) as EditText
            val foccuption = popUpView.findViewById(R.id.foccuption) as EditText
            val fincome = popUpView.findViewById(R.id.fincome) as EditText
            val button = popUpView.findViewById(R.id.button) as Button
            openwith.setView(popUpView)
            val family = openwith.create()
            fname.requestFocus()
            family.setCancelable(true)
            family.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            family.show()

            *//*val nerlationarradap= ArrayAdapter(activity!!, R.layout.support_simple_spinner_dropdown_item, nerlationarr)
            frelation.adapter = nerlationarradap*//*

            button.setOnClickListener{
                if (fname.text.toString().trim().isNotEmpty()&& frelation.text.toString().trim().isNotEmpty()&& fage.text.toString().trim().isNotEmpty()&& foccuption.text.toString().trim().isNotEmpty()&& fincome.text.toString().trim().isNotEmpty()) {
                    family.dismiss()
                    val data = FamilyMember()
                    data.Fid = (Family.size + 1).toString()
                    data.Name = fname.text.toString()

                    data.Age = fage.text.toString()
                    data.Occuption = foccuption.text.toString()
                    data.Income = fincome.text.toString()
                    Family.add(data)
                    family_list.adapter = com.elancier.bija.Family.FamilyAdapter(activity!!, Family)
                    com.elancier.bija.Family.Helper.setDynamicHeight(family_list)
                }else{
                    if (fname.text.toString().trim().isEmpty()){
                        fname.error = "Fill this Field"
                    }
                    if (frelation.text.toString().trim().isEmpty()){
                        Toast.makeText(activity,"Select relation",Toast.LENGTH_SHORT).show()
                    }
                    if (fage.text.toString().trim().isEmpty()){
                        fage.error = "Fill this Field"
                    }
                    if (foccuption.text.toString().trim().isEmpty()){
                        foccuption.error = "Fill this Field"
                    }
                    if (fincome.text.toString().trim().isEmpty()){
                        fincome.error = "Fill this Field"
                    }
                }
            }
        }*/

        return rootView
    }
    fun Show_Hide(){

    }
    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val FirstPermissionResult = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val SecondPermissionResult = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val ThirdPermissionResult = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.CAMERA)


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED
    }
    private fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(activity!!, arrayOf<String>(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA),RequestPermissionCode)

    }
    private fun getAge(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val ageInt = age

        return ageInt.toString()
    }



    fun spinner():Boolean{
        var sts = true
       /* if (spindata[acode.selectedItemPosition].id =="0"){
            acodeerror.error="Select Area code"
            sts = false
        }
        if (grpdata[gcode.selectedItemPosition].id =="0"){
            gcodeerror.error="Select Centre code"
            sts = false
        }*/

        return sts
    }

    fun getVillage(){

        //pDialog= Dialog(activity!!)
        //Appconstands.loading_show(activity!!, pDialog).show()

        var id=fid
        println("insvillage"+id)

        //Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
        val call = ApproveUtils.Get.gethar("http://elancier.com/bija/api/get_type/"+id)
        call.enqueue(object : Callback<Resp_harvest> {
            override fun onResponse(call: Call<Resp_harvest>, response: Response<Resp_harvest>) {

                if (response.isSuccessful()) {
                    val example = response.body() as Resp_harvest
                    println(example)
                    if (example.status == "Success") {
                        var arr=example.message
                        var otpval= arr!!.data!![0].plant
                        val data= SpinnerPojo()
                        data.name = "Select"
                        data.id = "0"
                        data.code = "Select"
                        data.status = "on"
                        villagearr.add(data)
                        println("printlnsize"+otpval!!.size.toString())
                        for(i in 0 until otpval!!.size) {
                            val data= SpinnerPojo()
                            data.name = otpval[i].name
                            data.id = ""
                            data.code =""
                            data.status = ""
                            villagearr.add(data)
                        }
                        val adap = Planet_adap(villagearr!!,plantseedarr!!,
                            plantharvestarr!!, typeplant!!, qtyplant!!, R.layout.incen_item, activity!!
                        )
                        plantlist.adapter = adap
                        plantlist.isExpanded=true

                        adap.notifyDataSetChanged()
                        //view!!.adapter = SpinAdapter(context!!,R.layout.spinner_list_harvest,villagearr)



                    } else {
                        Toast.makeText(
                            activity!!,
                            example.status,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else{
                    println("value"+response.toString())
                }

            }

            override fun onFailure(call: Call<Resp_harvest>, t: Throwable) {
                //Log.e("$tag Fail response", t.toString())
                println("fail"+t!!.toString())

                if (t.toString().contains("time")) {
                    Toast.makeText(
                        context!!,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //pDialog.dismiss()
                //loading_show(activity).dismiss()
            }
        })


    }

    fun member_details():Boolean{
        spinner()
        var sts = true
        if (mname.text.toString().trim().isEmpty()){
            mname.error="Enter the field"
            sts = false
        }
        if (mmob.text.toString().trim().isEmpty()){
            mmob.error="Enter the field"
            sts = false
        }else{
            if (mmob.text.length!=10){
                mmob.error="Enter valid Mobile number"
                sts = false
            }
        }
        if (mdob.text.toString().trim().isEmpty()){
            mdob.error="Enter the field"
            sts = false
        }
        if (mrationcard.text.toString().trim().isEmpty()){
            mrationcard.error="Enter the field"
            sts = false
        }
        if (mvoterid.text.toString().trim().isEmpty()){
            mvoterid.error="Enter the field"
            sts = false
        }
       /* if (maadhaar.text.toString().trim().isEmpty()){
            maadhaar.error="Enter the field"
            sts = false
        }else {
            if (maadhaar.text.length!=12) {
                maadhaar.error = "Enter Valid Aadhaar"
                sts = false
            }
        }*/
       /* if (mrelign.selectedItemPosition==0){
            sts = false
            relerror.error = "Select the field"
        }*/
        /*if (mcomunity.selectedItemPosition==0){
            sts = false
            comerror.error = "Select the field"
        }*/

        if(msurname.text.toString().trim().isEmpty()){
         msurname.error="Enter the field"
            sts=false
        }
        if(minit.text.toString().trim().isEmpty()){
          minit.error="Enter the field"
            sts=false
        }
        /*if(mrmrs.selectedItemPosition==0){
            relerrormrmrs.error="Select the field"
            sts=false

        }*/
        /*if(mgender.selectedItemPosition==0){
            relerrorgender.error="Select the field"
            sts=false

        }*/
        if(mmarital.selectedItemPosition==0){
            relerrormarital.error="Select the field"
            sts=false

        }
     /*   if(expamnt.text.toString().trim().isEmpty()){
          expamnt.error="Enter the field"
            sts=false
        }*/

        /*if (mrelign.text.isEmpty()){
            mrelign.error="Enter the field"
            sts = false
        }*/
        /*if (mcomunity.text.isEmpty()){
            mcomunity.error="Enter the field"
            sts = false
        }*/
       /* if (mlnamnt.text.toString().trim().isEmpty()){
            mlnamnt.error="Enter the field"
            sts = false
        }*/

        /*if (nbusi.text.toString().trim().isEmpty()){
            sts=false
            nbusi.error="Enter the field"
        }
        if (nyerofbusi.text.toString().trim().isEmpty()){
            sts=false
            nyerofbusi.error="Enter the field"
        }*/
        /*if (incomeamnt.text.toString().trim().isEmpty()){
            incomeamnt.error="Enter the field"
            sts = false
        }*/
        if (sts==false){
            member.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wrong, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }else{
            member.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }
        return sts
    }

    fun coapplicant_details():Boolean{
        spinner()
        var sts = true
        if (nname.text.toString().trim().isEmpty()){
            sts=false
            nname.error="Enter the field"
        }

       /* if (ndob.text.toString().trim().isEmpty()){
            sts=false
            ndob.error="Enter the field"
        }*/
        /*if (nmob.text.toString().trim().isEmpty()){
            sts=false
            nmob.error="Enter the field"
        }*/
        /*if (nvoterid.text.toString().trim().isEmpty()){
            sts=false
            nvoterid.error="Enter the field"
        }*/
        /*if (naadhaar.text.toString().trim().isEmpty()){
            sts=false
            naadhaar.error="Enter Aadhaar number"
        }else{
            if (naadhaar.text.length!=12) {
                naadhaar.error = "Enter Valid Aadhaar"
                sts = false
            }
        }
*/
        if(comrmrs.text.toString().trim().isEmpty()){
             sts=false
        }

        if(cosurname.selectedItemPosition==0){
            sts=false
            relerrorcomrmrs.error="Select the field"

        }

        if (sts==false){
            co_applicant.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wrong, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }else{
            co_applicant.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }
        return sts
    }



    fun other_details():Boolean{
        spinner()
        var sts =true
        if (mhouse.text.toString().trim().isEmpty()){
            mhouse.error="Enter the field"
            sts = false
        }
        if (mbirthplc.text.toString().trim().isEmpty()){
            mbirthplc.error="Enter the field"
            sts = false
        }
        if (maddress.text.toString().trim().isEmpty()){
            maddress.error="Enter the field"
            sts = false
        }
        if (mcaddress.text.toString().trim().isEmpty()){
            mcaddress.error="Enter the field"
            sts = false
        }

        if(permst.selectedItemPosition==0){
            sts=false
            relerrorstate.error="Select the field"
        }
        if(permpin.text.toString().trim().isEmpty()){
            sts=false
            permpin.error="Enter the field"

        }
        if(commst.selectedItemPosition==0){
            sts=false
            relerrorcomstate.error="Select the field"

        }
        if(commpin.text.toString().trim().isEmpty()){
           sts=false
            commpin.error="Enter the field"
        }
        if(loancat.selectedItemPosition==0){
            sts=false
            relerrorlncat.error="Select the field"

        }
        if(loanpurpose.text.toString().trim().isEmpty()){
            sts=false
            loanpurpose.error="Enter the field"

        }

        if (sts==false){
            other.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wrong, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }else{
            other.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }
        return sts

    }
    fun family_details():Boolean{
        spinner()
        var sts =true
        if (Family.isEmpty()){
            sts =false
            family.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wrong, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }else{
            sts =true
            family.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, R.drawable.ic_baseline_keyboard_arrow_down_24px, 0)
        }
        return sts
    }


    /*override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //inflater!!.inflate(R.menu.menu_calls_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/
}// Required empty public constructor