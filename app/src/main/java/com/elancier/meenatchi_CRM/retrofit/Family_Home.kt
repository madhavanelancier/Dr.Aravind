package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Family_Home{
    @SerializedName("id")
    @Expose
     var id: String? = null
    @SerializedName("unit_code")
    @Expose
     var unitCode: String? = null
    @SerializedName("village_code")
    @Expose
     var villageCode: String? = null
    @SerializedName("village_name")
    @Expose
     var villageName: String? = null
    @SerializedName("familyhead_name")
    @Expose
     var familyheadName: String? = null
    @SerializedName("status")
    @Expose
     var status: String? = null


}