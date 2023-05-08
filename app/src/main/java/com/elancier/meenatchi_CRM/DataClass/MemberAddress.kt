package com.elancier.meenatchi_CRM.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MemberAddress {
    //login
    @SerializedName("member_placement")
    @Expose
    var memberPlacement: String? = null
    @SerializedName("member_birthplace")
    @Expose
    var memberBirthplace: String? = null
    @SerializedName("member_address")
    @Expose
    var memberAddress: String? = null
    @SerializedName("member_communi_address")
    @Expose
    var memberCommuniAddress: String? = null

    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
