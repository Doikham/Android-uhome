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
import com.u.android_uhome.estimote.EstimoteApplication
import com.u.android_uhome.estimote.EstimoteService
import com.u.android_uhome.home.HomeActivity

class EstimoteNotification(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    lateinit var service: EstimoteService

    private fun buildNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        return NotificationCompat.Builder(context, "content_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(
                PendingIntent.getActivity(
                    context, 0,
                    Intent(context, HomeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    fun startObserver(token: String) {
        val notificationId = 1

        val proximityObserver =
            ProximityObserverBuilder(context, (context as EstimoteApplication).cloudCredentials)
                .withBalancedPowerMode()
                .withScannerInForegroundService(
                    buildNotification(
                        "Operate!",
                        "Searching for beacons"
                    )
                )
                .onError { /* handle errors here */ }
                .build()

        val tableZone = ProximityZoneBuilder()
            .forTag("uHome")
            .inNearRange()
            .onEnter { proximityContext ->
                val room = proximityContext.attachments["room"]
                Log.d("app", "Welcome to $room")
                notificationManager.notify(
                    notificationId,
                    buildNotification("Hey!", "You have entered the room")
                )
                service.callStartTimer(token)
            }
            .onExit {
                Log.d("app", "Stop timer")
                notificationManager.notify(
                    notificationId,
                    buildNotification("Bye!", "You have exited the room")
                )
            }
            .onContextChange { context ->
                val rooms = ArrayList<String>()
                for (context in context) {
                    rooms.add(context.attachments["room"].toString())
                }
                notificationManager.notify(
                    notificationId,
                    buildNotification("Oh!", "You are near $rooms")
                )
            }
            .build()

        proximityObserver.startObserving(tableZone)
    }
}