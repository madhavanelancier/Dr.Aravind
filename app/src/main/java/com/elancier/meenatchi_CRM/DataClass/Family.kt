package com.elancier.meenatchi_CRM.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Family {
    //login
    @SerializedName("s.no")
    @Expose
    var sno: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("relation")
    @Expose
    var relation: String? = null
    @SerializedName("age")
    @Expose
    var age: String? = null
    @SerializedName("occupation")
    @Expose
    var occupation: String? = null
    @SerializedName("monthly_income")
    @Expose
    var monthly_income: String? = null



    /*@SerializedName("total_amount")
    @Expose
    var total_amount: String? = null*/
}
