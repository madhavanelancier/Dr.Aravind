package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Resp_harvest {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: Harvest_msg? = null

}