package com.elancier.meenatchi_CRM.retrofit


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Edit_Resp {

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var response: String? = null



    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("balance")
    @Expose
    var balance: Int? = null


}
