package com.elancier.meenatchi_CRM.retrofit;

import android.database.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface apiservice {

    @GET("api.php")
    fun hitCountCheck(@Query("action") action: String,
                      @Query("format") format: String,
                      @Query("list") list: String,
                      @Query("srsearch") srsearch: String):
            Observable<Model.Result>
}
