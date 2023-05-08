package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitjsonArray private constructor() {
    private val retrofit: Retrofit
    internal var gson = GsonBuilder()
            .setLenient()
            .create()

    val api: usr
        get() = retrofit.create(usr::class.java)

    init {
        val httpClient = OkHttpClient.Builder();
        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request();

                val request = original.newBuilder()
                        //.header("User-Agent", "Your-App-Name")
                        .header("Content-Type", "application/json")/*vnd.yourapi.v1.full+*/
                        .header("Accept", "application/json")/*vnd.yourapi.v1.full+*/
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        })

                val client = httpClient.readTimeout(15, TimeUnit.MINUTES)
                        .writeTimeout(15, TimeUnit.MINUTES)
                        .connectTimeout(15, TimeUnit.MINUTES).build();
        /*val client = OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES)
                .connectTimeout(15, TimeUnit.MINUTES)
                .build()*/
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    companion object {
        private val BASE_URL = Appconstants.Domin
        private var mInstance: RetrofitjsonArray? = null

        val instance: RetrofitjsonArray
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = RetrofitjsonArray()
                }
                return mInstance!!
            }
    }
}