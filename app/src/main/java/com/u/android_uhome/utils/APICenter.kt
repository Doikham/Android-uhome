package com.u.android_uhome.utils

import com.u.android_uhome.device.DeviceModel
import com.u.android_uhome.estimote.EstimoteModel
import com.u.android_uhome.find.FindMyFamModel
import com.u.android_uhome.home.HomeModel
import com.u.android_uhome.record.RecordModel
import com.u.android_uhome.room.RoomModel
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
    fun startTimer(@Body request: RoomModel.RequestStartTimer): Call<RoomModel.ResponseStartTimer>

    @POST("/api/stoptimer")
    fun stopTimer(@Body request: HomeModel.RequestStopTimer): Call<HomeModel.ResponseStopTimer>

    @GET("/status")
    fun getServerStatus(): Call<HomeModel.ResponseServerStatus>

    @POST("/user/getHome")
    fun getHome(@Body request: HomeModel.Request): Call<HomeModel.ResponseHomeMessage>

    @POST("/getRoom")
    fun getRoom(@Body request: RoomModel.Request): Call<RoomModel.ResponseMessage>

    @POST("/user/getDevices/Hue")
    fun getDevices(@Body request: DeviceModel.Request): Call<DeviceModel.ResponseMessage>

    @POST("/setLight")
    fun changeColor(@Body request: DeviceModel.RequestChangeColor): Call<DeviceModel.Response>

    @POST("/getEstimoteKey")
    fun getEstimoteApp(@Body request: EstimoteModel.RequestApp): Call<EstimoteModel.ResponseAppSuccess>

    @POST("/notification/addRegis")
    fun addFcmToken(@Body request: HomeModel.RequestAddFcm): Call<HomeModel.ResponseGeneral>

    @POST("/api/getUserActivity")
    fun getUserActivity(@Body request: RecordModel.RequestRecord): Call<RecordModel.ResponseRecord>

    @POST("/home/gertUserLocations")
    fun findMember(@Body request: FindMyFamModel.Request): Call<FindMyFamModel.Response>
}