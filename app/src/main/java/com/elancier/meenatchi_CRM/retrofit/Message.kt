package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Message {

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

}