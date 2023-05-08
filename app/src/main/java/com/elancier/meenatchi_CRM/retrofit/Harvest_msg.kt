package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Harvest_msg {

    @SerializedName("data")
    @Expose
    var data: List<Harvest_data>? = null

}