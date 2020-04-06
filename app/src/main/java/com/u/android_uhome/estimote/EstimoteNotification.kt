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
import androidx.core.app.NotificationCompat
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.u.android_uhome.R
import com.u.android_uhome.dashboard.DashBoardActivity
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

    fun alertNotification(title: String, text: String) {
        notificationId = 3
        notificationManager.notify(
            notificationId!!,
            startBuildAlertNotification(title, text)
        )
    }

    private fun startBuildAlertNotification(title: String, text: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "Alert", "Alert Message", NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        return NotificationCompat.Builder(context, "Alert")
            .setSmallIcon(R.drawable.ic_warning_black_24dp)
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

    fun startObserver(token: String, shared: SharedPreferences) {
        val notificationId = 1
        var id = ""
        val dashBoard = DashBoardActivity()
        val proximityObserver =
            ProximityObserverBuilder(context, dashBoard.getEstimoteCredential(shared))
                .withBalancedPowerMode()
                .withScannerInForegroundService(
                    buildNotification(
                        "Warning!",
                        "You are not in the house"
                    )
                )
                .onError { }
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
                    buildNotification("Welcome!", "You have entered the $roomName")
                )
                val service = EstimoteService()
                if (shared.getString("start_timer_id", "").isNullOrEmpty()) {
                    id = service.callStartTimer(
                        token, roomId.toString(),
                        roomName.toString(), roomType.toString(), homeId.toString()
                    ).toString()
                    val editor: SharedPreferences.Editor = shared.edit()
                    editor.putString("start_timer_id", id)
                    editor.apply()
                }
            }
            .onExit {
                notificationManager.notify(
                    notificationId,
                    buildNotification("Warning!", "You are not in the house")
                )
                val service = EstimoteService()
                service.callStopTimer(token, id)
                shared.edit().remove("start_timer_id").apply()
            }
            .onContextChange { context ->
                val rooms = ArrayList<String>()
                for (context in context) {
                    rooms.add(context.attachments["Name"].toString())
                }
                if (!rooms.isNullOrEmpty())
                    notificationManager.notify(
                        notificationId,
                        buildNotification("Alert!", "You are in $rooms")
                    )
            }
            .build()
        proximityObserver.startObserving(tableZone)
    }
}