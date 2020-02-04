package com.u.android_uhome.home

import android.os.StrictMode
import android.util.Log
import com.u.android_uhome.APICenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-18-141-56-39.ap-southeast-1.compute.amazonaws.com:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun callToggleSwitch(token: String, deviceId: String){
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.RequestSwitchLight(token, deviceId)
        val call = service.toggleSwitch(request)
//        try {
//            val response: Response<HomeModel.ResponseSwitchLight> = call.execute()
//            Log.d("app", "Toggle light")
//            Log.d("app", response.body()!!.message)
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
        call.enqueue(object : Callback<HomeModel.ResponseSwitchLight> {
            override fun onResponse(
                call: Call<HomeModel.ResponseSwitchLight>?,
                response: Response<HomeModel.ResponseSwitchLight>?
            ) {
                Log.d("app", response?.body()?.message.toString())

            }

            override fun onFailure(
                call: Call<HomeModel.ResponseSwitchLight>?,
                throwable: Throwable?
            ) {
            }
        })
    }
}