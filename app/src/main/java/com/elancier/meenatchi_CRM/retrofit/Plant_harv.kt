package com.elancier.meenatchi_CRM.retrofit



import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Plant_harv {

    @SerializedName("sno")
    @Expose
    var id: String? = null
    @SerializedName("plant_type")
    @Expose
    val plant_type: String? = null

    @SerializedName("fish_type")
    @Expose
    val fish_type: String? = null

    @SerializedName("seeded_date")
    @Expose
    val seeded_date: String? = null

    @SerializedName("harvest_date")
    @Expose
    val harvest_date: String? = null

    @SerializedName("harvest_qty")
    @Expose
    var harvest_qty: String? = null


}