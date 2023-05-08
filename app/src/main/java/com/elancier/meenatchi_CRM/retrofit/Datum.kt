package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Datum {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("unit_code")
    @Expose
     val unitCode: String? = null

    @SerializedName("familyhead_name")
    @Expose
    val familyheadName: String? = null

    @SerializedName("village_name")
    @Expose
    val village_name: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("village_code")
    @Expose
    var villageCode: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("total_harvest")
    @Expose
     val totalHarvest: String? = null

    @SerializedName("plant_harvest")
    @Expose
    val plant_harvest: List<Plant_harv>? = null

    @SerializedName("fish_harvest")
    @Expose
    val fish_harvest: List<Plant_harv>? = null

}