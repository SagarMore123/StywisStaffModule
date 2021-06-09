package com.astrika.stywis_staff.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.view.dashboard.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object{
        private const val TAG = "MyFirebaseMsgService"
    }
    private var header = ""
    private var messageBody = ""
    private var data = ""

    //    private String header,messageBody,data;
    override fun onNewToken(s: String) {
        super.onNewToken(s);
        Log.e(TAG, "Refreshed Token: $s")
//        sendNotificationToServer();
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("Get From Count", "From: " + remoteMessage.from.toString() + "")
        Log.e("Notification Count", remoteMessage.data.size.toString() + "")
        if (remoteMessage.data.isNotEmpty()) {
            val hashMap: Map<String, String> =
                remoteMessage.data
            val jsonObject = JSONObject(hashMap)
            Log.e("firebaseObj", "" + jsonObject);
            try {
                header = jsonObject.getString("Header");
                messageBody = jsonObject.getString("MessageBody");
                data = jsonObject.getString("Data");
            } catch (e: JSONException) {
            }
            val messageBody: String
            val header: String
            val data = hashMap["message"]
            Log.e("Notification Data", remoteMessage.getData().toString() + "")
//            sendNotification(header, messageBody, data);
            header = remoteMessage.getData().get("Header") ?: ""
            messageBody = remoteMessage.getData().get("MessageBody") ?: ""
            sendNotification(header, messageBody)
        } else {

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.notification?.body)
            sendNotification(
                remoteMessage.notification?.title.toString(),
                remoteMessage.notification?.body.toString()
            )
        }

    }

    private fun sendNotification(
        header: String,
        messageBody: String
    ) {
//        Log.d(MyFirebaseMessagingService.TAG, "sendNotification: $messageBody")
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean("Notification", true)
        intent.putExtras(bundle)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId: String = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSmallIcon(R.drawable.ic_stywis_staff_72_x_72)
                .setContentTitle("Stywis $header")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 , notificationBuilder.build())
    }

}