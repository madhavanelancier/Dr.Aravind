package com.elancier.meenatchi_CRM.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.elancier.meenatchi_CRM.R;
import com.elancier.meenatchi_CRM.retrofit.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Utils utils;

    private static final String TAG = "MyFirebaseMsgService";

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    public void onNewToken(String token) {
        Log.e("onNewToken", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom()+"_");

//        Log.e("111", "From: " + remoteMessage.toString()+"_");
//        Log.e("name", remoteMessage.getData().get("name")+"_");
//        Log.e("price", remoteMessage.getData().get("price")+"_");
//        Log.e("unit", remoteMessage.getData().get("unit")+"_");
//        Log.e("data", remoteMessage.getData().get("data")+"_");
//        Log.e("title", remoteMessage.getData().get("title")+"_");
//
//        Log.e("image", remoteMessage.getData().get("image")+"_");
//        Log.e("url", remoteMessage.getData().get("url")+"_");

        // Check if message contains a data payload.
        // if (remoteMessage.getNotification().size() > 0) {
        // Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        try{


            utils=new Utils(getApplicationContext());

            utils.savePreferences("noti","e");

            sendNotification( new JSONObject(remoteMessage.getData().get("data")).get("title").toString(),
                    new JSONObject(remoteMessage.getData().get("data")).get("image").toString());

//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    public void run() {
//                        new LoadNotification(MyFirebaseMessagingService.this, remoteMessage.getData().get("notification_message")).execute(remoteMessage.getData().get("notification_image"));
//                    }
//                });

        }catch (Exception e){
            e.printStackTrace();
        }
        //}

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            try{
//                final JSONObject jsonObject = new JSONObject(remoteMessage.getNotification().getBody());
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    public void run() {
//                        new LoadNotification(MyFirebaseMessagingService.this, jsonObject.optString("notification_message")).execute(jsonObject.optString("notification_image"));
//                    }
//                });
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]



    // [END on_new_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String url){    //, Bitmap bitmap) {
        Bitmap imageBitmap = null ;
        if (url != null && ! url.equalsIgnoreCase("") ) {

           /* try {
                imageBitmap =   Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .submit().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        /*Intent intent = new Intent(this, Tableview.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification", messageBody);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/

       /* NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setSummaryText(messageBody);
        bigPictureStyle.setBigContentTitle(getString(R.string.app_name));
        bigPictureStyle.bigPicture(bitmap);*/
        NotificationCompat.BigTextStyle bigPictureStyle = new NotificationCompat.BigTextStyle();
        bigPictureStyle.setSummaryText(getString(R.string.app_name));
        bigPictureStyle.setBigContentTitle(getString(R.string.app_name));
        // bigPictureStyle.bigPicture(bitmap);
        Notification builder = null;

        String channelId ="Notifications";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //notificationBuilder.setSmallIcon(R.mipmap.lo);
            //notificationBuilder.setColor(getResources().getColor(R.color.notification_color));
        } else {
            //notificationBuilder.setSmallIcon(R.mipmap.logo_vanithamarket);
        }
        notificationBuilder.setContentTitle(getString(R.string.app_name))
                        .setStyle(bigPictureStyle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        if(imageBitmap != null ) {
            notificationBuilder.setLargeIcon(imageBitmap);
        }
        builder = notificationBuilder.build();

        notificationManager.notify(Config.NOTIFICATION_ID, builder);
    }

    private class LoadNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;

        public LoadNotification(Context context, String message) {
            super();
            this.ctx = context;
            this.message = message;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                // sendNotification(message, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
