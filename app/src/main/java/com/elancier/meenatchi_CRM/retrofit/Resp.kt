package com.elancier.meenatchi_CRM.retrofit


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Resp {

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("message")
    @Expose
    private var message: Message? = null

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getMessage(): Message? {
        return message
    }

    fun setMessage(message: Message) {
        this.message = message
    }


}
