package com.u.android_uhome.estimote

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.CATEGORY_SERVICE
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.AvailableNetworkInfo.PRIORITY_HIGH
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.*
import com.u.android_uhome.R

class EstimoteActivity : AppCompatActivity() {

    private var proximityObserver: ProximityObserver? = null

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimote)

        val cloudCredentials = EstimoteCloudCredentials("uhome-g7u",
        "edeae45dd50b1d0ff0f4efbe7f165a91")

//        val notification = Notification.Builder(this)
//            .setSmallIcon(R.drawable.notification_icon_background)
//            .setContentTitle("Beacon scan")
//            .setContentText("Scan is running...")
//            .setPriority(Notification.PRIORITY_HIGH)
//            .build()

        val NOTIFICATION_CHANNEL_ID = "com.estimote.indoorapp"
        val channelName = "My Background Service"
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setContentTitle("Beacon scan")
            .setContentText("Scan is running...")
            .setVisibility(VISIBILITY_PUBLIC)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
            .setOnlyAlertOnce(true)
            .setPriority(PRIORITY_HIGH)
            .setCategory(CATEGORY_SERVICE)
            .build()

        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                onRequirementsFulfilled = {
                    Log.d("app", "requirements fulfilled")
                    startObserver(cloudCredentials,notification)
                },
                onRequirementsMissing = { requirements ->
                    Log.e("app", "requirements missing: $requirements")
                },

                onError = { throwable ->
                    Log.e("app", "requirements error: $throwable")
                })

//        val zone = ProximityZoneBuilder()
//            .forTag("table")
//            .inNearRange()
//            .onEnter { context ->
//                val tableOwner = context.attachments["table-owner"]
//                Log.d("app", "Welcome to $tableOwner's table")
//                null
//            }
//            .onContextChange {
//                fun invoke(contexts:Set<ProximityZoneContext>) {
//                    val deskOwners = arrayListOf<String>()
//                    for (context in contexts)
//                    {
//                        deskOwners.add(context.attachments["table-owner"].toString())
//                    }
//                    Log.d("app", "In range of desks: $deskOwners")
//                    null
//                }
//            }
//            .onExit {
//                Log.d("app", "Bye bye, come again!")
//                null
//            }
//            .build()

//        fun onDestroy() {
//            observationHandler?.stop()
//            super.onDestroy()
//        }

//        val triggerHandle = ProximityTriggerBuilder(applicationContext)
//            .displayNotificationWhenInProximity(notification)
//            .build()
//            .start()


    }

    private fun startObserver(cloudCredentials: EstimoteCloudCredentials, notification: Notification) {
        val proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
            .withBalancedPowerMode()
            .withScannerInForegroundService(notification)
            .onError { /* handle errors here */ }
            .build()

        val tableZone = ProximityZoneBuilder()
            .forTag("table")
            .inCustomRange(1.0)
            .onEnter { proximityContext ->
                val tableOwner = proximityContext.attachments["table-owner"]
                Log.d("app", "Welcome to $tableOwner's table")
                Toast.makeText(this,"Welcome",Toast.LENGTH_LONG).show()
            }
            .onExit {
                Toast.makeText(this,"Bye",Toast.LENGTH_LONG).show()
            }
            .onContextChange {/* do something here */}
            .build()

        proximityObserver.startObserving(tableZone)
    }
}
