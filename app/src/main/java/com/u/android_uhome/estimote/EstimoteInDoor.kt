package com.u.android_uhome.estimote

import android.app.Application
import com.estimote.coresdk.observation.region.Region
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager
import java.util.*


class EstimoteInDoor: Application() {
//    private val monitoringRegion: Region = Region("region", UUID.fromString("my-UUID"), 1, 1)
//    private var beaconManager: BeaconManager? = null
//    private var notificationManager: NotificationManager? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        beaconManager = BeaconManager(this.applicationContext)
//        beaconManager!!.connect { startMonitoring() }
//        notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    }
//
//    private fun startMonitoring() {
//        beaconManager.setMonitoringListener(object : MonitoringListener() {
//            fun onEnteredRegion(
//                region: Region?,
//                list: List<Beacon?>?
//            ) { // invoke your action here
//            }
//
//            fun onExitedRegion(region: Region?) { // invoke your action here
//            }
//        })
//        beaconManager.startMonitoring(monitoringRegion)
//    }
//
//    var allBeaconsRegion: Region? =
//        Region(
//            "Beacons with default Estimote UUID",
//            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null
//        )
//
//    private var beaconManager: BeaconManager? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        beaconManager = BeaconManager(applicationContext)
//        beaconManager!!.connect {
//            // Ready to start scanning!
////            beaconManager.setMonitoringListener(object : MonitoringListener() {
////                fun onEnteredRegion(
////                    region: Region?,
////                    list: List<Beacon?>?
////                ) { // ...
////                }
////
////                fun onExitedRegion(region: Region?) { // ...
////                }
////            })
//            fun onServiceReady() {
//                // Ready to start scanning!
//            }
//
//            beaconManager!!.setRangingListener(BeaconManager.BeaconRangingListener {
//                fun onBeaconsDiscovered(region:Region, beacons:List<Beacon>) {
//                    if (beacons.size !== 0)
//                    {
//                        val beacon = beacons[0]
//                        // ...
//                    }
//                }
//            })
//        }
//    }


}