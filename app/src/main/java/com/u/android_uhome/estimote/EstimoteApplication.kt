package com.u.android_uhome.estimote

import android.app.Application
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.u.android_uhome.notification.EstimoteNotification

class EstimoteApplication : Application() {

    val cloudCredentials = EstimoteCloudCredentials(
        "uhome-g7u",
        "edeae45dd50b1d0ff0f4efbe7f165a91"
    )

    fun enableBeaconNotifications(token: String) {
        val notificationsManager = EstimoteNotification(this)
        notificationsManager.startObserver(token)
    }
}