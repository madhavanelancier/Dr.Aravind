package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Datum_Home {

    @SerializedName("family")
    @Expose
    var family: String? = null

    @SerializedName("aquaponics")
    @Expose
    var aquaponics: String? = null

    @SerializedName("families")
    @Expose
    var families: List<Family_Home>? = null


}