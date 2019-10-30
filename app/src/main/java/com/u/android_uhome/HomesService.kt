package com.u.android_uhome

import com.u.android_uhome.home.HomeModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomesService {
    @GET("/api/device/{uid}")
    fun getDeviceDetail(@Path("uid") uid: Int?) : Call<List<HomeModel.Response>>
}