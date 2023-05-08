package com.elancier.meenatchi_CRM.retrofit


import com.elancier.meenatchi_CRM.DataClass.otpdata
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Resp_otps {

    @SerializedName("status")
    @Expose
    var status:String? = null

    @SerializedName("message")
    @Expose
    var message:String? = null

    @SerializedName("response")
    @Expose
    var response:otpdata? = null


}
