package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Message_dup {
    @SerializedName("data")
    @Expose
    private var data: String? = null

    fun getData(): String? {
        return data
    }

    fun setData(data: String) {
        this.data = data
    }

}