package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Resp_dup {

    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("message")
    @Expose
    private var message: Message_dup? = null

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getMessage(): Message_dup? {
        return message
    }

    fun setMessage(message: Message_dup) {
        this.message = message
    }
}
