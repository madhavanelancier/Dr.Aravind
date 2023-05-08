package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class od_details {
    //login
    @SerializedName("week")
    @Expose
    var week: String? = null
    @SerializedName("d_date")
    @Expose
    var d_date: String? = null
    @SerializedName("amount_to_pay")
    @Expose
    var amount_to_pay: String? = null
    @SerializedName("od_recover")
    @Expose
    var od_recover: String? = null
}