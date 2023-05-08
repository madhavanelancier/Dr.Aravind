package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Fish {

    @SerializedName("name")
    @Expose
    var name: String? = null

}