package com.elancier.meenatchi_CRM.retrofit



import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class amount {

    @SerializedName("total_amount")
    @Expose
    var total_amount: String? = null

    @SerializedName("od_details")
    @Expose
    var staff_code: List<od_details>? = null








}