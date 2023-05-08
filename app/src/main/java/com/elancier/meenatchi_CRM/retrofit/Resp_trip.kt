package com.elancier.meenatchi_CRM.retrofit


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Resp_trip {

    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("remainAmount")
    @Expose
    private var remainAmount: String? = null


    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("response")
    @Expose
    private var response: List<Response_trip>? = null

    @SerializedName("currentTrip")
    @Expose
    private var currentTrip: List<Response_trip>? = null

    fun getStatus(): String? {
        return status
    }
    fun getremainAmount(): String? {
        return remainAmount
    }


    fun setStatus(status: String) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getResponse(): List<Response_trip>? {
        return response
    }
    fun getcurrenttrip(): List<Response_trip>? {
        return currentTrip
    }

    fun setResponse(response: List<Response_trip>) {
        this.response = response
    }


}
