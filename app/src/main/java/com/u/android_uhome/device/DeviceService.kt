package com.u.android_uhome.device

import com.u.android_uhome.home.HomeModel
import com.u.android_uhome.utils.APICenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeviceService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-18-141-56-39.ap-southeast-1.compute.amazonaws.com:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun callChangeColor(token: String, deviceId: String, hex: String, homeId: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = DeviceModel.RequestChangeColor(token, deviceId, hex, homeId)
        val call = service.changeColor(request)

        call.enqueue(object : Callback<DeviceModel.Response> {
            override fun onResponse(
                call: Call<DeviceModel.Response>?,
                response: Response<DeviceModel.Response>?
            ) {
            }

            override fun onFailure(
                call: Call<DeviceModel.Response>?,
                throwable: Throwable?
            ) {
            }
        })
    }

    fun callToggleSwitch(token: String, deviceId: String, homeId: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.RequestSwitchLight(token, deviceId, homeId)
        val call = service.toggleSwitch(request)
        call.enqueue(object : Callback<HomeModel.ResponseSwitchLight> {
            override fun onResponse(
                call: Call<HomeModel.ResponseSwitchLight>?,
                response: Response<HomeModel.ResponseSwitchLight>?
            ) {
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseSwitchLight>?,
                throwable: Throwable?
            ) {
            }
        })
    }
}