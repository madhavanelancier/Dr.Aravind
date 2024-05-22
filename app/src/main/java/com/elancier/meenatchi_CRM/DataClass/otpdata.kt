package com.elancier.meenatchi_CRM.DataClass

import com.elancier.meenatchi_CRM.retrofit.amount

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class otpdata {
    //login
    @SerializedName("first_name")
    @Expose
    var  first_name: String?  = null

    @SerializedName("last_name")
    @Expose
    var  last_name: String?  = null
    @SerializedName("email")
    @Expose
    var  email: String?  = null

    @SerializedName("crm_att")
    @Expose
    var  crm_att: String?  = null

    @SerializedName("official_mobile")
    @Expose
    var  mobile: String?  = null

    @SerializedName("hrm_id")
    @Expose
    var  hrm_id: String?  = null

    @SerializedName("employment_id")
    @Expose
    var  member_code: String?  = null

    @SerializedName("state")
    @Expose
    var  state: String?  = null

    @SerializedName("role_id")
    @Expose
    var  role_id: String?  = null

    @SerializedName("height")
    @Expose
    var  height: String?  = null

    @SerializedName("birthday")
    @Expose
    var  birthday: String?  = null

    @SerializedName("caste")
    @Expose
    var  caste: String?  = null

    @SerializedName("doctors")
    @Expose
    var  doctors: Int?  = null

    @SerializedName("meetings")
    @Expose
    var  meetings: Int?  = null

    @SerializedName("leadpatients")
    @Expose
    var  leadpatients: Int?  = null

    @SerializedName("patients")
    @Expose
    var  patients: Int?  = null


    @SerializedName("partial_image")
    @Expose
    var  partial_image: String?  = null

    @SerializedName("member_name")
    @Expose
    var  member_name: String?  = null

    @SerializedName("amount")
    @Expose
    var  amount: amount?  = null

    @SerializedName("bank_logo")
    @Expose
    var bank_logo: String? = null

    @SerializedName("ac_num")
    @Expose
    var ac_num: String? = null

    @SerializedName("ac_name")
    @Expose
    var ac_name: String? = null


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

    @SerializedName("staff")
    @Expose
    var  staff: String?  = null

    @SerializedName("bank_name")
    @Expose
    var bank_name: String? = null

    @SerializedName("members")
    @Expose
    var  members: String?  = null

    @SerializedName("accounts")
    @Expose
    var  accounts: String?  = null

    @SerializedName("centres")
    @Expose
    var  centres: String?  = null

    @SerializedName("leave_request")
    @Expose
    var  leave_request: String?  = null

    @SerializedName("enroll_date")
    @Expose
    var  enrollDate: String? = null
    @SerializedName("name")
    @Expose
    var  name: String?  = null
    @SerializedName("dob")
    @Expose
    var  dob: String?  = null

    @SerializedName("gender")
    @Expose
    var  gender: String?  = null

    @SerializedName("age")
    @Expose
    var  age: String?  = null

    @SerializedName("id")
    @Expose
    var  id: String?  = null

    @SerializedName("locations")
    @Expose
    var  locations: String?  = null

    @SerializedName("camp_location")
    @Expose
    var  camp_location: String?  = null

    @SerializedName("camp")
    @Expose
    var  camp: String?  = null

    @SerializedName("wife_name")
    @Expose
    var  wife_name: String?  = null

    @SerializedName("husband_name")
    @Expose
    var  husband_name: String?  = null

    @SerializedName("wife_no")
    @Expose
    var  wife_no: String?  = null

    @SerializedName("husband_no")
    @Expose
    var  husband_no: String?  = null

    @SerializedName("wife_age")
    @Expose
    var  wife_age: String?  = null

    @SerializedName("husband_age")
    @Expose
    var  husband_age: String?  = null





    @SerializedName("community")
    @Expose
    var  community: String?  = null
    @SerializedName("religion")
    @Expose
    var  religion: String?  = null
    @SerializedName("member_mobile")
    @Expose
    var  memberMobile: String?  = null
    @SerializedName("kyc")
    @Expose
    var  kyc: Kyc? = null
    @SerializedName("nominee")
    @Expose
    var  nominee: Nominee? = null
    @SerializedName("business")
    @Expose
    var  business: String?  = null
    @SerializedName("business_year")
    @Expose
    var  businessYear: String?  = null


    @SerializedName("BUS")
    @Expose
    var  bus: String?  = null

    @SerializedName("TRAIN")
    @Expose
    var  train: String?  = null

    @SerializedName("AUTO")
    @Expose
    var  auto: String?  = null

    @SerializedName("CAB")
    @Expose
    var  cab: String?  = null

    @SerializedName("BREAKFAST")
    @Expose
    var  breakfast: String?  = null

    @SerializedName("LUNCH")
    @Expose
    var  lunch: String?  = null

    @SerializedName("DINNER")
    @Expose
    var  dinner: String?  = null


    @SerializedName("Stay")
    @Expose
    var  stay: String?  = null

    @SerializedName("Others")
    @Expose
    var  others: String?  = null

    @SerializedName("member_family")
    @Expose
    var  memberFamily: Family?  = null
    @SerializedName("member_address")
    @Expose
    var  memberAddress: MemberAddress? = null
    @SerializedName("attachments")
    @Expose
    var  attachments: Attachments? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("loan")
    @Expose
    var loan: String? = null

    @SerializedName("cashinhand")
    @Expose
    var cashinhand: String? = null

    @SerializedName("nooforders")
    @Expose
    var nooforders: String? = null

    @SerializedName("orders")
    @Expose
    var orders: String? = null

    @SerializedName("check_out")
    @Expose
    var check_out: Int? = null


    @SerializedName("balance")
    @Expose
    var balance: String? = null




    @SerializedName("ordervalue")
    @Expose
    var ordervalue: String? = null

    @SerializedName("tid")
    @Expose
    var tid: String? = null

    @SerializedName("area")
    @Expose
    var area: String? = null

    @SerializedName("od")
    @Expose
    var od: String? = null
    @SerializedName("part_od")
    @Expose
    var partOd: String? = null
    @SerializedName("savings")
    @Expose
    var savings: String? = null

    @SerializedName("saving")
    @Expose
    var saving: String? = null

    @SerializedName("transaction_date")
    @Expose
    var transaction_date: String? = null

    @SerializedName("branches")
    @Expose
    var branches: String? = null

    @SerializedName("closed")
    @Expose
    var closed: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("state_name")
    @Expose
    var state_name: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null


    @SerializedName("income")
    @Expose
    var income: String? = null

    @SerializedName("handicap")
    @Expose
    var handicap: String? = null

    @SerializedName("education")
    @Expose
    var education: String? = null

    @SerializedName("place")
    @Expose
    var place: String? = null

    @SerializedName("zodiac")
    @Expose
    var zodiac: String? = null

    @SerializedName("natcha")
    @Expose
    var natcha: String? = null

    @SerializedName("current_loan")
    @Expose
    var currentLoan: String? = null
    @SerializedName("previous_loan")
    @Expose
    var previousLoan: String? = null
    @SerializedName("week")
    @Expose
    var week: String? = null

    @SerializedName("prefer_location")
    @Expose
    var prefer_location: String? = null



    @SerializedName("weeks")
    @Expose
    var weeks: String? = null

    @SerializedName("due")
    @Expose
    var due: String? = null
    @SerializedName("od_recover")
    @Expose
    var od_recover: String? = null
    @SerializedName("recover_date")
    @Expose
    var recover_date: String? = null
    @SerializedName("paid")
    @Expose
    var paid: String? = null
    @SerializedName("week_due")
    @Expose
    var week_due: String? = null
    @SerializedName("acc_no")
    @Expose
    var acc_no: String? = null

    @SerializedName("account_no")
    @Expose
    var account_no: String? = null

    @SerializedName("close_method")
    @Expose
    var close_method: String? = null

    @SerializedName("loan_due")
    @Expose
    var loan_due: String? = null



    @SerializedName("acc_type")
    @Expose
    var acc_type: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("api_key")
    @Expose
    var api_key: String? = null

    @SerializedName("api_secret")
    @Expose
    var api_secret: String? = null

    @SerializedName("cloud_name")
    @Expose
    var cloud_name: String? = null


    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
