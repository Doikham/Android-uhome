package com.u.android_uhome

import com.u.android_uhome.device.DeviceModel
import com.u.android_uhome.home.HomeModel
import com.u.android_uhome.room.RoomModel
import io.reactivex.Observable
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APICenter {
    @GET("/api/device/{uid}")
    fun getDeviceDetail(@Path("uid") uid: Int?): Call<List<HomeModel.Response>>

    @POST("/api/device/get")
    fun getDeviceList(@Body request: HomeModel.Request): Call<List<HomeModel.Response>>

    @POST("/switchLight")
    fun toggleSwitch(@Body request: HomeModel.RequestSwitchLight): Call<HomeModel.ResponseSwitchLight>

    @POST("/api/starttimer")
    fun startTimer(@Body request: HomeModel.Request): Call<HomeModel.ResponseStartTimer>

    @POST("/api/stoptimer")
    fun stopTimer(@Body request: HomeModel.RequestStopTimer): Call<HomeModel.ResponseStopTimer>

    @GET("/status")
    fun getServerStatus(): Call<HomeModel.ResponseServerStatus>

    @POST("/user/getHome")
    fun getHome(@Body request: HomeModel.Request): Call<HomeModel.ResponseHomeMessage>

    @POST("/getRoom")
    fun getRoom(@Body request: RoomModel.Request): Call<RoomModel.ResponseMessage>

    @POST("/user/getDevices")
    fun getDevices(@Body request: DeviceModel.Request): Call<DeviceModel.ResponseMessage>
}