package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Response_trip {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("tid")
    @Expose
    var tid: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("state_name")
    @Expose
    var state_name: String? = null

    @SerializedName("meeting_datetime")
    @Expose
    var meeting_datetime: String? = null

    @SerializedName("doctor")
    @Expose
    var doctor: String? = null

    @SerializedName("user")
    @Expose
    var user: String? = null
    @SerializedName("area")
    @Expose
    var area: String? = null
    @SerializedName("plan_date")
    @Expose
    var planDate: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("target_value")
    @Expose
    var targetValue: String? = null
    @SerializedName("from_date")
    @Expose
    var fromDate: String? = null
    @SerializedName("to_date")
    @Expose
    var toDate: String? = null
    @SerializedName("days")
    @Expose
    var days: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("clinic")
    @Expose
    var clinic: String? = null

    @SerializedName("location_pin")
    @Expose
    var location_pin: String? = null

    @SerializedName("open_stock")
    @Expose
    var open_stock: String? = null

    @SerializedName("stock")
    @Expose
    var stock: String? = null

    @SerializedName("amount")
    @Expose
    var amount: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("ex_customers")
    @Expose
    var exCustomers: String? = null
    @SerializedName("new_customers")
    @Expose
    var newCustomers: String? = null
    @SerializedName("budget")
    @Expose
    var budget: String? = null
    @SerializedName("customers")
    @Expose
    var customers: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("lat")
    @Expose
    var lat: String? = null

    @SerializedName("long")
    @Expose
    var long: String? = null

    @SerializedName("brand")
    @Expose
    var brand: String? = null

    @SerializedName("brand_name")
    @Expose
    var brand_nm: String? = null

    @SerializedName("approved_amount")
    @Expose
    var approved_amount: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null



    @SerializedName("specialization_name")
    @Expose
    var mobile: String? = null

    @SerializedName("specialization")
    @Expose
    var specialization: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("doctor_contact_number")
    @Expose
    var doctor_contact_number: String? = null


    @SerializedName("hospital_contact_number")
    @Expose
    var hospital_contact_number: String? = null



    @SerializedName("check_out")
    @Expose
    var check_out: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null


    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("wife_name")
    @Expose
    var wife_name: String? = null

    @SerializedName("mrd_number")
    @Expose
    var mrd_number: String? = null

    @SerializedName("doctor_name")
    @Expose
    var doctor_name: String? = null

    @SerializedName("hospital_name")
    @Expose
    var hospital_name: String? = null

    @SerializedName("stateName")
    @Expose
    var stateName: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("image1")
    @Expose
    var image1: String? = null



    @SerializedName("contact_person")
    @Expose
    var contact_person: String? = null

    @SerializedName("order_details")
    @Expose
    var order_details: String? = null

    @SerializedName("order_value")
    @Expose
    var order_value: String? = null

    @SerializedName("notes")
    @Expose
    var notes: String? = null

    @SerializedName("cloud_name")
    @Expose
    var cloud_name: String? = null

    @SerializedName("customer_id")
    @Expose
    var customer_id: String? = null

    @SerializedName("api_key")
    @Expose
    var api_key: String? = null

    @SerializedName("api_secret")
    @Expose
    var api_secret: String? = null

    @SerializedName("trip_id")
    @Expose
    var trip_id : String?=null
    @SerializedName("city_name")
    @Expose
    var city_name : String?=null

    @SerializedName("clinic_photo")
    @Expose
    var clinic_photo : ArrayList<String>?=null

    /*@SerializedName("state")
    @Expose
    var state : String?=null

    @SerializedName("customer_id")
    @Expose
    var customer_id : String?=null*/
    @SerializedName("cname")
    @Expose
    var cname : String?=null
    @SerializedName("mmob")
    @Expose
    var mmob : String?=null
    @SerializedName("email")
    @Expose
    var email : String?=null
    @SerializedName("maddress")
    @Expose
    var maddress : String?=null
    @SerializedName("tval")
    @Expose
    var tval : String?=null
     @SerializedName("sval")
    @Expose
    var sval : String?=null
    @SerializedName("sq_val")
    @Expose
    var sq_val : String?=null
    @SerializedName("intime")
    @Expose
    var intime : String?=null
    @SerializedName("outtime")
    @Expose
    var outtime : String?=null
    @SerializedName("magcapacity")
    @Expose
    var magcapacity : String?=null
    @SerializedName("turnover")
    @Expose
    var turnover : String?=null
    @SerializedName("ctype")
    @Expose
    var ctype : String?=null
    @SerializedName("cpro")
    @Expose
    var cpro : String?=null
    @SerializedName("scomm")
    @Expose
    var scomm : String?=null
    @SerializedName("plike")
    @Expose
    var plike : String?=null
    @SerializedName("pcomnt")
    @Expose
    var pcomnt : String?=null
    @SerializedName("exfeed")
    @Expose
    var exfeed : String?=null
    @SerializedName("roomloc")
    @Expose
    var roomloc : String?=null
    @SerializedName("spur")
    @Expose
    var spur : String?=null
    @SerializedName("persons")
    @Expose
    var persons : List<Response_trip>?=null
    @SerializedName("eod")
    @Expose
    var eod : String?=null
    @SerializedName("designation")
    @Expose
    var designation : String?=null











}