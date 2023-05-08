package com.elancier.meenatchi_CRM

import android.content.Intent
import android.app.Application

import android.util.Log


class TestApplication : Application() {


    override fun onCreate() {

        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { thread, ex -> handleUncaughtException(thread, ex) }

    }


    fun handleUncaughtException(thread: Thread, e: Throwable) {

        val stackTrace = Log.getStackTraceString(e)

        val message = e.message


        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "message/rfc822"

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("muthuelancier@gmail.com"))

        intent.putExtra(Intent.EXTRA_SUBJECT, "Vanitha  log file")

        intent.putExtra(Intent.EXTRA_TEXT, stackTrace)

        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // required when starting from Application


        startActivity(Intent.createChooser(intent, "Send error report to developer..."))


    }

}