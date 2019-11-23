package com.u.android_uhome.home

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.u.android_uhome.APICenter
import com.u.android_uhome.R
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

    fun callToggleSwitch(token: String, deviceId: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.RequestSwitchLight("1111", "4")
        val call = service.toggleSwitch(request)
        call.enqueue(object : Callback<HomeModel.ResponseSwitchLight> {
            override fun onResponse(
                call: Call<HomeModel.ResponseSwitchLight>?,
                response: Response<HomeModel.ResponseSwitchLight>?
            ) {
                Log.d("app", response?.body().toString())
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseSwitchLight>?,
                throwable: Throwable?
            ) {
            }
        })
    }
}