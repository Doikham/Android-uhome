package com.u.android_uhome.estimote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.estimote.coresdk.cloud.model.DeviceFirmware
import com.estimote.coresdk.cloud.model.NearableMode
import com.estimote.coresdk.common.config.EstimoteSDK
import com.estimote.coresdk.common.exception.EstimoteException
import com.estimote.coresdk.recognition.packets.ConfigurableDevice
import com.estimote.coresdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.internal.Version
import com.estimote.coresdk.service.BeaconManager
import com.estimote.mgmtsdk.common.exceptions.DeviceConnectionException
import com.estimote.mgmtsdk.connection.api.DeviceConnection
import com.estimote.mgmtsdk.connection.api.DeviceConnectionCallback
import com.estimote.mgmtsdk.connection.api.DeviceConnectionProvider
import com.estimote.mgmtsdk.feature.bulk_updater.BulkUpdater
import com.estimote.mgmtsdk.feature.bulk_updater.BulkUpdaterBuilder
import com.estimote.mgmtsdk.feature.settings.SettingCallback
import com.u.android_uhome.R
import java.util.concurrent.TimeUnit

class EstimoteActivity : AppCompatActivity() {


//    private val beaconManager = BeaconManager(this)
//    private val connectionProvider = DeviceConnectionProvider(this)
//    private var bulkUpdater: BulkUpdater? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_estimote)
//
//        EstimoteSDK.initialize(this,"uhome-g7u",
//            "edeae45dd50b1d0ff0f4efbe7f165a91")
//
//        val context = applicationContext
//        bulkUpdater = BulkUpdaterBuilder(context)
//            .withFirmwareUpdate()
//            .withCloudFetchInterval(1, TimeUnit.HOURS)
//            .withTimeout(0)
//            .withRetryCount(3)
//            .build()
//
//        start()
//
//        connectionProvider.connectToService {
//            // Handle your actions here. You are now connected to connection service.
//            // For example: you can create DeviceConnection object here from connectionProvider.
//        }
//
//        beaconManager.setForegroundScanPeriod(2000, 2000)
//
//        start2()
//
//        stop()
//
//        val connection = connectionProvider.getConnection(device)
//        connection.connect(object: DeviceConnectionCallback {
//            override fun onConnected() {
//                // Do something with your connection.
//                // You can for example read device settings, or make an firmware update.
//                Log.d("DeviceConnection", "onConnected")
//            }
//            override fun onDisconnected() {
//                // Every time your device gets disconnected, you can handle that here.
//                // For example: in this state you can try reconnecting to your device.
//                Log.d("DeviceConnection", "onDisconnected")
//            }
//            override fun onConnectionFailed(exception:DeviceConnectionException) {
//                // Handle every connection error here.
//                Log.d("DeviceConnection", "onConnectionFailed")
//            }
//        })
//
//        connection.settings.deviceInfo.firmware().get(object:SettingCallback<Version>() {
//            override fun onSuccess(value: Version) {
//                // Handle read data here.
//                // For example: display them in UI. This callback will be called in the same thread as connection was created (not opened).
//                // You can use your activity method runOnUIThread(Runnable runnable) to handle that.
//                Log.d("DeviceRead", "Read firmware version: $value")
//            }
//            override fun onFailure(exception:DeviceConnectionException) {
//                // Handle exceptions here.
//                Log.d("DeviceRead", "Reading firmware version failed.")
//            }
//        })
//
//        connection.checkForFirmwareUpdate(object : DeviceConnection.CheckFirmwareCallback {
//            override fun onDeviceUpToDate(firmware: DeviceFirmware) { // If device is up to date, handle that case here. Firmware object contains info about current version.
//                Log.d("DeviceFirmwareUpdate", "Device firmware is up to date.")
//            }
//
//            override fun onDeviceNeedsUpdate(firmware: DeviceFirmware) { // Handle device update here. Firmware object contains info about latest version.
//                Log.d("DeviceFirmwareUpdate", "Device needs firmware update.")
//            }
//
//            override fun onError(exception: DeviceConnectionException) { // Handle errors here
//                Log.d(
//                    "DeviceFirmwareUpdate",
//                    "Error checking device firmware: " + exception.message
//                )
//            }
//        })
//
//        connection.updateDevice(object : DeviceConnection.FirmwareUpdateCallback {
//            override fun onSuccess() { // Handle success
//                Log.d("DeviceFirmwareUpdate", "Device firmware updated.")
//            }
//
//            override fun onProgress(
//                progress: Float,
//                message: String
//            ) { // Handle progress - range is 0.0 - 1.0
//                Log.d(
//                    "DeviceFirmwareUpdate",
//                    "Device firmware update progress: $progress Message: $message"
//                )
//            }
//
//            override fun onFailure(e: DeviceConnectionException) { // Handle failure. Don't worry about device state - upon failure, it resets back to its old version.
//                Log.d(
//                    "DeviceFirmwareUpdate",
//                    "Device firmware update failure: " + e.message
//                )
//            }
//        })
//
//        connection.settings.estimote.nearable.broadcastingScheme()[NearableMode.IBEACON] =
//            object : SettingCallback<NearableMode?> {
//                override fun onSuccess(value: NearableMode?) { // Handle success
//                    Log.d(
//                        "BroadcastingScheme",
//                        "Changed nearable broadcastung scheme. "
//                    )
//                }
//
//                override fun onFailure(exception: DeviceConnectionException) { // Handle errors here
//                    Log.d(
//                        "Broadcasting Scheme",
//                        "Error setting broadcasting scheme: " + exception.message
//                    )
//                }
//            }
//    }
//
//    override fun onDestroy() {
//        connectionProvider.destroy()
//        super.onDestroy()
//    }
//
//    private fun start(){
//        bulkUpdater?.start(object: BulkUpdater.BulkUpdaterCallback {
//            override fun onFinished() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDeviceStatusChange(device:ConfigurableDevice,
//                                              newStatus:BulkUpdater.Status, message:String) {
////                Log.d("BulkUpdater", device.deviceId + ": " + newStatus)
//            }
//
//            override fun onError(e: EstimoteException?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            fun onFinished(updatedCount:Int, failedCount:Int) {
//                Log.d("BulkUpdater", ("Finished. Updated: " +
//                        updatedCount + ", Failed: " + failedCount))
//            }
//            fun onError(e:DeviceConnectionException) {
//                Log.d("BulkUpdater", "Error: $e")
//            }
//        })
//    }
//
//    private fun start2(){
//        beaconManager.connect {
//            // add listener for ConfigurableDevice objects
//            beaconManager.setConfigurableDevicesListener {
//                // handle the configurable device here. You can use it to acquire connection from DeviceConnectionProvider
//                    configurableDevices ->
//                bulkUpdater?.onDevicesFound(configurableDevices)
//            }
//            beaconManager.startConfigurableDevicesDiscovery()
//        }
//    }
//
//    fun stop(){
//        beaconManager.stopConfigurableDevicesDiscovery()
//        bulkUpdater?.stop()
//        bulkUpdater?.destroy()
//    }
}
