package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import retrofit2.Response

class Example {


    @SerializedName("Status")
    @Expose
    var status: String? = null
    @SerializedName("Response")
    @Expose
    var response: Response<*>? = null
}