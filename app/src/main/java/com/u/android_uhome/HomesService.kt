package com.u.android_uhome

import com.u.android_uhome.home.HomeModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HomesService {
    @GET("/api/device/{uid}")
    fun getDeviceDetail(@Path("uid") uid: Int?) : Call<List<HomeModel.Response>>

    @FormUrlEncoded
    @POST("/api/devices/get")
    fun getDeviceList(@Field("idToken") token: String) : Call<List<HomeModel.Response>>
}