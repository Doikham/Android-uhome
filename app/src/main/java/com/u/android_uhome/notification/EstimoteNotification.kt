package com.u.android_uhome.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.u.android_uhome.estimote.EstimoteActivity
import com.u.android_uhome.estimote.EstimoteApplication

class EstimoteNotification(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val helloNotification = buildNotification("Hello", "You're near your beacon")
    private val goodbyeNotification = buildNotification("Bye bye", "You've left the proximity of your beacon")

    private fun buildNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH)
            )
        }
        return NotificationCompat.Builder(context, "content_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(PendingIntent.getActivity(context, 0,
                Intent(context, EstimoteActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

     fun startObserver() {
         val notificationId = 1
         val proximityObserver = ProximityObserverBuilder(context, (context as EstimoteApplication).cloudCredentials)
            .withBalancedPowerMode()
//            .withScannerInForegroundService()
            .onError { /* handle errors here */ }
            .build()
        val tableZone = ProximityZoneBuilder()
            .forTag("table")
            .inCustomRange(2.0)
            .onEnter { proximityContext ->
                val tableOwner = proximityContext.attachments["table-owner"]
                Log.d("app", "Welcome to $tableOwner's table")
                notificationManager.notify(notificationId, helloNotification)
            }
            .onExit {
                notificationManager.notify(notificationId, goodbyeNotification)
            }
            .onContextChange {/* do something here */}
            .build()

        proximityObserver.startObserving(tableZone)
    }
}