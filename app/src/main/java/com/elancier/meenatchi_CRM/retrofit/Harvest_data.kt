package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Harvest_data {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("fish")
    @Expose
    var fish: List<Fish>? = null
    @SerializedName("plant")
    @Expose
    var plant: List<Plant>? = null

}