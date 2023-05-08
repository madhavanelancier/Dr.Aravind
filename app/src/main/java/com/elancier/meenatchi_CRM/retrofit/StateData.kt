package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StateData {
    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("response")
    val response: List<StateResponse>? = null

    inner class StateResponse {
        @SerializedName("id")
        @Expose
        val id: String? = null

        @SerializedName("state_name")
        @Expose
        val state_name: String? = null

        @SerializedName("created_at")
        @Expose
        val created_at: String? = null

        @SerializedName("updated_at")
        @Expose
        val updated_at: String? = null
    }
}
