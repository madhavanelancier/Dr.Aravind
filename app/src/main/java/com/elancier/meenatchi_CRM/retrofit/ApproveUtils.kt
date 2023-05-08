package com.elancier.meenatchi_CRM.retrofit


object ApproveUtils {
    val BASE_URL = Appconstants.Domin

    val Get: usr
        //get() = RetrofitClient.getClient(BASE_URL).create(usr::class.java)
        get() = RetrofitCli.instance.api
    val Get2: usr
        //get() = RetrofitClient.getClient(BASE_URL).create(usr::class.java)
        get() = RetrofitjsonArray.instance.api
}
