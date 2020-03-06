package com.u.android_uhome.estimote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.u.android_uhome.R
import com.u.android_uhome.room.RoomActivity
import com.u.android_uhome.user.UserActivity

class EstimoteNotification(private val context: Context) {

    private var notificationId: Int? = null
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
                    Intent(context, UserActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    fun testNotification(title: String, text: String) {
        notificationId = 2
        notificationManager.notify(
            notificationId!!,
            startBuildNotification(title, text)
        )
    }

    private fun startBuildNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "content_channel", "So", NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        val pattern = longArrayOf(0, 200, 0)
        val notification: Notification = NotificationCompat.Builder(context, "content_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(
                PendingIntent.getActivity(
                    context, 0,
                    Intent(context, UserActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setSound(
                Uri.parse("android.resource://" + context.packageName + "/" + R.raw.line_ring)
            )
            .setVibrate(pattern)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notification.flags = Notification.FLAG_INSISTENT
        return notification
    }

    fun startObserver(token: String, shared: SharedPreferences) {
        val notificationId = 1
        var id = ""
        val room = RoomActivity()
        val proximityObserver =
            ProximityObserverBuilder(context, room.getEstimoteCredential(shared))
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
                val roomId = proximityContext.attachments["RoomID"]
                val roomName = proximityContext.attachments["Name"]
                val roomType = proximityContext.attachments["Type"]
                val homeId = proximityContext.attachments["HomeID"]
                notificationManager.notify(
                    notificationId,
                    buildNotification("Hey!", "You have entered the $roomName")
                )
                val service = EstimoteService()
                id = service.callStartTimer(
                    token, roomId.toString(),
                    roomName.toString(), roomType.toString(), homeId.toString()
                ).toString()
                // Check for user properties
            }
            .onExit {
                notificationManager.notify(
                    notificationId,
                    buildNotification("Bye!", "You have exited the room")
                )
                val service = EstimoteService()
                service.callStopTimer(token, id)
            }
            .onContextChange { context ->
                val rooms = ArrayList<String>()
                for (context in context) {
                    rooms.add(context.attachments["Name"].toString())
                }
                if (!rooms.isNullOrEmpty())
                    notificationManager.notify(
                        notificationId,
                        buildNotification("Oh!", "You are near $rooms")
                    )
            }
            .build()
        proximityObserver.startObserving(tableZone)
    }
}