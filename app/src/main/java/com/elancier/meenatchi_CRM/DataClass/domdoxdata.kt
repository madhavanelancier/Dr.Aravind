package com.elancier.meenatchi_CRM.DataClass


import com.elancier.meenatchi_CRM.retrofit.ObjDesc
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class domdoxdata {
    //login
    @SerializedName("site_header")
    @Expose
    var site_header: String? = null

    @SerializedName("site_logo")
    @Expose
    var site_logo: String? = null

    @SerializedName("caste_id")
    @Expose
    var caste_id: String? = null



    @SerializedName("account")
    @Expose
    var account: List<otpdata>? = null

    @SerializedName("grooms")
    @Expose
    var Grooms: List<otpdata>? = null

    @SerializedName("brides")
    @Expose
    var Brides: List<otpdata>? = null



    @SerializedName("main_logo")
    @Expose
    var main_logo: String? = null

    @SerializedName("whatsapp")
    @Expose
    var whatsapp: String? = null

    @SerializedName("call_us")
    @Expose
    var call_us: String? = null

    @SerializedName("email")
    @Expose
    var mail: String? = null

    @SerializedName("total_caste")
    @Expose
    var total_caste: String? = null

    @SerializedName("total_grooms")
    @Expose
    var total_grooms: String? = null

    @SerializedName("total_brides")
    @Expose
    var total_brides: String? = null



    @SerializedName("innam_1")
    @Expose
    var innam_1: String? = null

    @SerializedName("innam_2")
    @Expose
    var innam_2: String? = null

    @SerializedName("innam_3")
    @Expose
    var innam_3: String? = null



    @SerializedName("outstation")
    @Expose
    var outstation: String? = null

    @SerializedName("local_station")
    @Expose
    var local_station: String? = null

    @SerializedName("member_range")
    @Expose
    var member_range: List<domdoxdata1>? = null

    @SerializedName("details")
    @Expose
    var details: List<domdoxdata1>? = null



    @SerializedName("otp")
    @Expose
    var otp: String? = null

    @SerializedName("allowed_date")
    @Expose
    var allowed_date: String? = null

     @SerializedName("extra_time")
    @Expose
    var extra_time: String? = null

    //Today centre
    @SerializedName("centre_id")
    @Expose
    var centre_id: String? = null
    @SerializedName("centre_name")
    @Expose
    var centre_name: String? = null
    @SerializedName("total_amount")
    @Expose
    var total_amount: String? = null
    @SerializedName("total_members")
    @Expose
    var total_members: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("start_time")
    @Expose
    var start_time: String? = null
    @SerializedName("end_time")
    @Expose
    var end_time: String? = null

    //Toaday member
    @SerializedName("member_id")
    @Expose
    var member_id: String? = null
    @SerializedName("member_name")
    @Expose
    var member_name: String? = null
    @SerializedName("member_mobile")
    @Expose
    var member_mobile: String? = null
    @SerializedName("member_image")
    @Expose
    var member_image: String? = null

    @SerializedName("staff_name")
    @Expose
    var staff_name: String? = null

    @SerializedName("staff_code")
    @Expose
    var staff_code: String? = null

    @SerializedName("bank_id")
    @Expose
    var bank_id: String? = null

    @SerializedName("bank_name")
    @Expose
    var bank_name: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("week")
    @Expose
    var week: String? = null

    @SerializedName("credit")
    @Expose
    var credit: String? = null

    @SerializedName("debit")
    @Expose
    var debit: String? = null

    @SerializedName("deposit")
    @Expose
    var deposit: String? = null

    @SerializedName("withdraw")
    @Expose
    var withdraw: String? = null

    @SerializedName("balance")
    @Expose
    var balance: String? = null


    @SerializedName("description")
    @Expose
    var descrip: ObjDesc? = null

    @SerializedName("centre")
    @Expose
    var centre: List<domdoxdata>? = null





    @SerializedName("d_date")
    @Expose
    var d_date: String? = null

    @SerializedName("leave_for")
    @Expose
    var leave_for: String? = null

    @SerializedName("loan")
    @Expose
    var loan: String? = null

    @SerializedName("savings")
    @Expose
    var savings: String? = null



    @SerializedName("loan_amt")
    @Expose
    var loan_amt: String? = null
    @SerializedName("member_code")
    @Expose
    var member_code: String? = null
    @SerializedName("member_position")
    @Expose
    var member_position: String? = null

    //My members
    @SerializedName("collection_day")
    @Expose
    var collection_day: String? = null

    //verification
    @SerializedName("account_no")
    @Expose
    var account_no: String? = null

    //Group_code
    @SerializedName("id")
    @Expose
    var id: String? = null



    @SerializedName("centre_code")
    @Expose
    var centre_code: String? = null

    //Area_code
    @SerializedName("area_code")
    @Expose
    var area_code: String? = null

    //Leave
    /*{"Status":"Success","Message":"Success","Response":[{"staff_id":"17","d_date":"2019-10-05","leave_for":"Reason","status":"Pending"}]}*/


    //Notification
    /*{"Status":"Success","Message":"Success","Response":[{"notification_id":"1","title":"Leave","message":"Tomorrow","date_time":1571157695}]}*/
    @SerializedName("notification_id")
    @Expose
    var notification_id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("holidays")
    @Expose
    var holidays: String? = null

    @SerializedName("alternate_date")
    @Expose
    var alternate_date: String? = null

    @SerializedName("total_days")
    @Expose
    var total_days: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("date_time")
    @Expose
    var date_time: String? = null

    //Feedback
    @SerializedName("reason")
    @Expose
    var reason: String? = null

    //Dashboard
    @SerializedName("total_centre")
    @Expose
    var total_centre: String? = null

    @SerializedName("completed_centre")
    @Expose
    var completed_centre: String? = null

    @SerializedName("pending_centre")
    @Expose
    var pending_centre: String? = null

    @SerializedName("incentive")
    @Expose
    var incentive: String? = null

    @SerializedName("total_od")
    @Expose
    var total_od: String? = null

    @SerializedName("branch_id")
    @Expose
    var branch_id: String? = null

    @SerializedName("branch_name")
    @Expose
    var branch_name: String? = null

    @SerializedName("branch")
    @Expose
    var branch: String? = null

    @SerializedName("ifsc")
    @Expose
    var ifsc: String? = null

    @SerializedName("micr")
    @Expose
    var micr: String? = null

    @SerializedName("ac_type")
    @Expose
    var ac_type: String? = null



    @SerializedName("open_date")
    @Expose
    var open_date: String? = null

    @SerializedName("branch_code")
    @Expose
    var branch_code: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("state")
    @Expose
    var state: String? = null
    @SerializedName("address1")
    @Expose
    var address1: String? = null
    @SerializedName("address2")
    @Expose
    var address2: String? = null
    @SerializedName("pincode")
    @Expose
    var pincode: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("landline")
    @Expose
    var landline: String? = null

    @SerializedName("assigned_staff")
    @Expose
    var assigned_staff: String? = null

    @SerializedName("device_id")
    @Expose
    var device_id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("assigned_day")
    @Expose
    var assigned_day: String? = null

    @SerializedName("station")
    @Expose
    var station: String? = null

    @SerializedName("from_time")
    @Expose
    var from_time: String? = null

    @SerializedName("to_time")
    @Expose
    var to_time: String? = null














    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
