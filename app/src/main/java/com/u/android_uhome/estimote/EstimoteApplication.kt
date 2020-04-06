package com.u.android_uhome.estimote

import android.app.Application
import android.content.SharedPreferences

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EstimoteApplication : Application() {

    fun enableBeaconNotifications(token: String, shared: SharedPreferences) {
        val notificationsManager =
            EstimoteNotification(this)
        notificationsManager.startObserver(token, shared)
    }

}