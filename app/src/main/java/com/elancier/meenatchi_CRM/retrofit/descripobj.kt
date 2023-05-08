package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ObjDesc {

    @SerializedName("bank_name")
    @Expose
    var bank_name: String? = null

    @SerializedName("staff_code")
    @Expose
    var staff_code: String? = null

    @SerializedName("particular")
    @Expose
    var particular: String? = null

    @SerializedName("cheque_num")
    @Expose
    var cheque_num: String? = null

    @SerializedName("week")
    @Expose
    var week: String? = null

    @SerializedName("area_code")
    @Expose
    var area_code: String? = null

    @SerializedName("centre_code")
    @Expose
    var centre_code: String? = null

    @SerializedName("leader_name")
    @Expose
    var leader_name: String? = null

    @SerializedName("member_code")
    @Expose
    var member_code: String? = null

    @SerializedName("member_name")
    @Expose
    var member_name: String? = null

    @SerializedName("paid_amount")
    @Expose
    var paid_amount: String? = null

    @SerializedName("types")
    @Expose
    var types: String? = null






}