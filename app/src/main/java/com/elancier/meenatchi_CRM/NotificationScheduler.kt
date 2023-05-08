package com.elanciers.bringszodelivery.Common

import android.content.Intent
import android.media.RingtoneManager
import android.app.*
import android.content.Context.ALARM_SERVICE
import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import com.elancier.meenatchi_CRM.R

import java.util.*


object NotificationScheduler {
    val DAILY_REMINDER_REQUEST_CODE = 100
    val TAG = "NotificationScheduler"

    fun setReminder(context: Context, cls: Class<*>, hour: Int, min: Int) {
        val calendar = Calendar.getInstance()

        val setcalendar = Calendar.getInstance()
        setcalendar.set(Calendar.HOUR_OF_DAY, hour)
        setcalendar.set(Calendar.MINUTE, min)
        setcalendar.set(Calendar.SECOND, 0)

        // cancel already scheduled reminders
        cancelReminder(context, cls)

        if (setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE, 1)

        // Enable a receiver

        val receiver = ComponentName(context, cls)
        val pm = context.getPackageManager()

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)


        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        //am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),1200*1000 /*bc.toLong()*60000*/, pendingIntent)


    }

    fun cancelReminder(context: Context, cls: Class<*>) {
        // Disable a receiver

        val receiver = ComponentName(context, cls)
        val pm = context.getPackageManager()

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)

        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun showNotification(context: Context, cls: Class<*>, title: String, content: String) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationIntent = Intent(context, cls)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(cls)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)

        /*val channelid="Notification"
        val builder = NotificationCompat.Builder(context,channelid,)

        val notification = builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent).build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification)*/

        val builder:Notification
        val channelId = "Notifications"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                //.setStyle(bigPictureStyle)
                .setContentText("New Notification")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(channelId,
                    "Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        /*if (imageBitmap != null)
        {
            notificationBuilder.setLargeIcon(imageBitmap)
        }*/
        builder = notificationBuilder.build()
        notificationManager.notify(100, builder)

        if (net_status(context) ==true){
           /* if (Utils(context).login()==false){

            }else{

            }*/

        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = ""//getString(R.string.channel_name)
            val descriptionText = ""//getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }*/

    }
    fun net_status(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var connected=false
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true
        } else {
            //Toast.makeText(context,"No Network",Toast.LENGTH_LONG).show()
            connected = false
        }
        return connected
    }

    /*private fun sendNotification(messageBody:String, url:String) { //, Bitmap bitmap) {
        val imageBitmap:Bitmap = null
        if (url != null && !url.equals("", ignoreCase = true))
        {
            try
            {
                imageBitmap = Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .submit().get()
            }
            catch (e:ExecutionException) {
                e.printStackTrace()
            }
            catch (e:InterruptedException) {
                e.printStackTrace()
            }
        }
        val intent = Intent(this, JewelHome::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 / Request, , code / , intent,
                PendingIntent.FLAG_ONE_SHOT)
        *//* NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
       bigPictureStyle.setSummaryText(messageBody);
       bigPictureStyle.setBigContentTitle(getString(R.string.app_name));
       bigPictureStyle.bigPicture(bitmap);*//*
        val bigPictureStyle = NotificationCompat.BigTextStyle()
        bigPictureStyle.setSummaryText(getString(R.string.app_name))
        bigPictureStyle.setBigContentTitle(getString(R.string.app_name))
        // bigPictureStyle.bigPicture(bitmap);
        val builder:Notification
        val channelId = "Notifications"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Tea Shop")
                .setStyle(bigPictureStyle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(channelId,
                    "Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        if (imageBitmap != null)
        {
            notificationBuilder.setLargeIcon(imageBitmap)
        }
        builder = notificationBuilder.build()
        notificationManager.notify(100, builder)
    }*/
}