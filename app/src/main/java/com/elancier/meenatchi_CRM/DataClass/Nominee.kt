package com.elancier.meenatchi_CRM.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Nominee {
    //login
    @SerializedName("nominee_name")
    @Expose
    var nomineeName: String? = null
    @SerializedName("nominee_relationship")
    @Expose
    var nomineeRelationship: String? = null
    @SerializedName("nominee_adhaar")
    @Expose
    var nomineeAdhaar: String? = null
    @SerializedName("nominee_voterid")
    @Expose
    var nomineeVoterid: String? = null


    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
