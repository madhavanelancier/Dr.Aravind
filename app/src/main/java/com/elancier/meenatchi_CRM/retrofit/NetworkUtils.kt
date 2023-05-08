package com.elancier.meenatchi_CRM.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.net.wifi.WifiManager


object NetworkUtils {

    var TYPE_WIFI = 1
    var TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0


    fun getConnectivityStatus(context: Context): Int {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.getNetworkInfo(conn)
        var status: String? = null
        if (conn == TYPE_WIFI) {
            val wifiManager = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.getConnectionInfo();
            val speed = wifiInfo.getLinkSpeed()
            return "Wifi Network Connected"
            status = "Wifi Network Connected"
        } else if (conn == TYPE_MOBILE) {
            println("type NETWORK_TYPE_1xRTT : "+TelephonyManager.NETWORK_TYPE_1xRTT)
            println("type NETWORK_TYPE_CDMA : "+TelephonyManager.NETWORK_TYPE_CDMA)
            println("type NETWORK_TYPE_EDGE : "+TelephonyManager.NETWORK_TYPE_EDGE)
            println("type NETWORK_TYPE_EVDO_0 : "+TelephonyManager.NETWORK_TYPE_EVDO_0)
            println("type NETWORK_TYPE_EVDO_A : "+TelephonyManager.NETWORK_TYPE_EVDO_A)
            println("type NETWORK_TYPE_GPRS : "+TelephonyManager.NETWORK_TYPE_GPRS)
            println("type NETWORK_TYPE_HSDPA : "+TelephonyManager.NETWORK_TYPE_HSDPA)
            println("type NETWORK_TYPE_HSPA : "+TelephonyManager.NETWORK_TYPE_HSPA)
            println("type NETWORK_TYPE_HSUPA : "+TelephonyManager.NETWORK_TYPE_HSUPA)
            println("type NETWORK_TYPE_UMTS : "+TelephonyManager.NETWORK_TYPE_UMTS)
            println("type NETWORK_TYPE_EHRPD : "+TelephonyManager.NETWORK_TYPE_EHRPD)
            println("type NETWORK_TYPE_EVDO_B : "+TelephonyManager.NETWORK_TYPE_EVDO_B)
            println("type NETWORK_TYPE_HSPAP : "+TelephonyManager.NETWORK_TYPE_HSPAP)
            println("type NETWORK_TYPE_IDEN : "+TelephonyManager.NETWORK_TYPE_IDEN)
            println("type NETWORK_TYPE_LTE : "+TelephonyManager.NETWORK_TYPE_LTE)
            /*when (info.subtype) {

                TelephonyManager.NETWORK_TYPE_1xRTT -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_CDMA -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_EDGE -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_EVDO_A -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_GPRS -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_HSDPA -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_HSPA -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_HSUPA -> return "4G Network"
                TelephonyManager.NETWORK_TYPE_UMTS -> return "4G Network"
                *//*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 *//*
                TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                -> return "1-2 Mbps speed Connection"
                TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                -> return "5 Mbps speed Connection"
                TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                -> return "10-20 Mbps speed Connection"
                TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                -> return "25 kbps speed Connection"
                TelephonyManager.NETWORK_TYPE_LTE // API level 11
                -> return "4G Network"
                0 // API level 11
                -> return "4G Network"
                // Unknown
                //TelephonyManager.NETWORK_TYPE_UNKNOWN -> return ""
                //else -> return ""

            }*/

            when (info?.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return "2G Network"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return "3G Network"
                TelephonyManager.NETWORK_TYPE_LTE,0 -> return "4G Network"
                else -> return "unknown"
            }
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "No Network"
        }

        return status
    }


}