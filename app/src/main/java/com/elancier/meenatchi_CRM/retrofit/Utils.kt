package com.elancier.meenatchi_CRM.retrofit

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager

class Utils(internal var _context: Context) {
    internal var sharedPreferences: SharedPreferences

    val isConnectingToInternet: Boolean
        get() {
            val connectivity =
                _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info.indices)
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }

            }
            return false
        }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
    }

    fun savePreferences(string: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(string, value)
        editor.commit()
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }


    fun Staff_id(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("staff_id", "")
    }

    fun getLocation(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("lat", "")
    }
    fun getLocation1(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("lang", "")
    }


    fun date(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("date", "")
    }

    fun today(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("login", "")
    }

    fun settoday(t :String) {
        val editor = sharedPreferences.edit()
        editor.putString("login", t)
        editor.commit()
    }

    fun del():Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("del", false)
    }

    fun login():Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("log", false)
    }
    fun setLogin(t : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("log", t)
        editor.commit()
    }

    fun logout():Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("logout", false)
    }
    fun setLogout(t : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("logout", t)
        editor.commit()
    }

    fun setDel(t : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("del", t)
        editor.commit()
    }

    fun setToken(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", t)
        editor.commit()
    }

    fun getToken(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("token", "")
    }

    fun getValue(str: String): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString(str, "")
    }

    fun setMaintenance(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("maintenance", t)
        editor.commit()
    }
    fun Maintenance():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("maintenance", "")
    }

    fun allowed_date():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("allowed_date", "")
    }
    fun extra_time():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("extra_time", "")
    }

}
