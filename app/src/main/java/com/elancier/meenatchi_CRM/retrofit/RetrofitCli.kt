package com.elancier.meenatchi_CRM.retrofit


import com.google.gson.GsonBuilder

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCli private constructor() {
    private val retrofit: Retrofit
    internal var gson = GsonBuilder()
        .setLenient()
        .create()

    val api: usr
        get() = retrofit.create(usr::class.java)

    init {
        val client = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.MINUTES)
            .connectTimeout(15, TimeUnit.MINUTES)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    companion object {
        private val BASE_URL = Appconstants.Domin
        private var mInstance: RetrofitCli? = null

        val instance: RetrofitCli
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = RetrofitCli()
                }
                return mInstance!!
            }
    }
}
