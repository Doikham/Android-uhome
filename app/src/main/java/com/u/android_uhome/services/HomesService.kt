package com.u.android_uhome.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST

interface HomesService {
    @POST("/")
    fun getDeviceDetail(): Call<ResponseBody>
}