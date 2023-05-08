package com.elancier.meenatchi_CRM.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Kyc {
    //login
    @SerializedName("member_aadhar")
    @Expose
    var memberAadhar: String? = null
    @SerializedName("member_voterid")
    @Expose
    var memberVoterid: String? = null
    @SerializedName("member_rationcard")
    @Expose
    var memberRationcard: String? = null

    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
