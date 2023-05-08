package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

typealias ExecutiveData = ArrayList<ExecutiveDatum>

data class ExecutiveDatum (
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("first_name")
    @Expose
    var first_name: String? = null,
    @SerializedName("last_name")
    @Expose
    var last_name: String? = null,
    @SerializedName("mobile")
    @Expose
    var mobile: String? = null
)