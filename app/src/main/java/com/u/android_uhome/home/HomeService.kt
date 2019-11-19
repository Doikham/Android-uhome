package com.u.android_uhome.home

import com.u.android_uhome.APICenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-18-139-110-142.ap-southeast-1.compute.amazonaws.com:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun callToggleSwitch(token: String, deviceId: String, currentStatus: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.RequestToggle(token, deviceId, currentStatus)
        val call = service.toggleSwitch(request)
        call.enqueue(object : Callback<HomeModel.ResponseToggle> {
            override fun onResponse(
                call: Call<HomeModel.ResponseToggle>?,
                response: Response<HomeModel.ResponseToggle>?
            ) {
            }

            override fun onFailure(call: Call<HomeModel.ResponseToggle>?, throwable: Throwable?) {

            }
        })
    }
}