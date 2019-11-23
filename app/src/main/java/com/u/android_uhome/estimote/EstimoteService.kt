package com.u.android_uhome.estimote

import android.util.Log
import android.widget.Toast
import com.u.android_uhome.APICenter
import com.u.android_uhome.home.HomeActivity
import com.u.android_uhome.home.HomeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EstimoteService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-18-139-110-142.ap-southeast-1.compute.amazonaws.com:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun callStartTimer(token: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.Request(token)
        val call = service.startTimer(request)
        call.enqueue(object : Callback<HomeModel.ResponseStartTimer> {
            override fun onResponse(
                call: Call<HomeModel.ResponseStartTimer>?,
                response: Response<HomeModel.ResponseStartTimer>?
            ) {
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseStartTimer>?,
                throwable: Throwable?
            ) {
                Log.w("app", "Can not start timer")
            }
        })
    }

    fun callStopTimer(token: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.Request(token)
        val call = service.startTimer(request)
        call.enqueue(object : Callback<HomeModel.ResponseStartTimer> {
            override fun onResponse(
                call: Call<HomeModel.ResponseStartTimer>?,
                response: Response<HomeModel.ResponseStartTimer>?
            ) {
                Log.d("app", "Start timer")
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseStartTimer>?,
                throwable: Throwable?
            ) {
                Log.w("app", "Can not start timer")
            }
        })
    }
}