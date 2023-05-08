package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Customers {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null

    @SerializedName("comment")
    @Expose
    var comment: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("image1")
    @Expose
    var image1: String? = null

    @SerializedName("image2")
    @Expose
    var image2: String? = null

    @SerializedName("image3")
    @Expose
    var image3: String? = null

    @SerializedName("image4")
    @Expose
    var image4: String? = null

    @SerializedName("image5")
    @Expose
    var image5: String? = null
}