package com.u.android_uhome.service

import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.firebase.jobdispatcher.*
import com.firebase.jobdispatcher.Trigger.executionWindow
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.u.android_uhome.MainActivity
import com.u.android_uhome.R
import com.u.android_uhome.estimote.EstimoteNotification
import com.u.android_uhome.user.UserActivity
import java.io.IOException
import java.net.URL


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "From: ${remoteMessage.data}")
        Log.d(TAG, "From: ${remoteMessage.data["body"]}")
//        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.line_ring)
        val notification = EstimoteNotification(this)
//        val vibrator: Vibrator =
//            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (remoteMessage.data["play"].equals("true")) {
//            startFindingPhone(mp, vibrator)
            notification.testNotification(
                remoteMessage.data["body"].toString(),
                remoteMessage.data["name"].toString()
            )
        } else {
            notification.alertNotification(
                remoteMessage.data["title"].toString(),
                remoteMessage.data["body"].toString()
            )
        }

    }

}