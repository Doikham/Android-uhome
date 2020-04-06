package com.u.android_uhome.service

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.u.android_uhome.estimote.EstimoteNotification

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notification = EstimoteNotification(this)
        if (remoteMessage.data["play"].equals("true")) {
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