package com.elancier.meenatchi_CRM.DataClass

import org.json.JSONArray
import java.io.Serializable

class OrderDetail:Serializable {

    var id : String? = null
    var tid : String? = null
    var user : String? = null
    var area : String? = null
    var plan_date : String? = null
    var target_value:String?=null
    var from_date:String?=null
    var to_date:String?=null
    var days:String?=null
    var ex_customers:String?=null
    var new_customers:String?=null
    var budget : String? = null
    var customers : String? = null
    var customer_id : String? = null
    var status : String? = null
    var created_at : String? = null
    var updated_at : String? = null
    var find : String? = null
   var customer_name:String?=null
   var mobile:String?=null
   var address:String?=null
   var city:String?=null
   var state:String?=null
   var image:String?=null
   var image1:String?=null
   var contact_person:String?=null

    var dateandtime : String? = null
    var details : JSONArray? = null


    var shopnm : String? = null
    var shoploc : String? = null
    var adrs_type : String? = null
    var coupon_code : String? = null
    var adrs : String? = null
    var tnx : String? = null

    var payment_mode : String? = null
}
