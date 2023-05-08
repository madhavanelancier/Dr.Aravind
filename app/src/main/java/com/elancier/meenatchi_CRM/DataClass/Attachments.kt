package com.elancier.meenatchi_CRM.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Attachments {
    //login
    @SerializedName("member_image")
    @Expose
    var memberImage: String? = null
    @SerializedName("member_nominee_image")
    @Expose
    var memberNomineeImage: String? = null


    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
