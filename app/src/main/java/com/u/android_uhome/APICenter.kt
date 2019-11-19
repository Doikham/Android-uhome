package com.u.android_uhome

import com.u.android_uhome.home.HomeModel
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

    @POST("/api/toggleswitch")
    fun toggleSwitch(@Body request: HomeModel.RequestToggle): Call<HomeModel.ResponseToggle>

    @POST("/api/toggleswitch")
    fun startTimer(@Body request: HomeModel.Request): Call<HomeModel.ResponseStartTimer>
}