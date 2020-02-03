package com.u.android_uhome.estimote

import android.os.StrictMode
import android.util.Log
import com.u.android_uhome.APICenter
import com.u.android_uhome.home.HomeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EstimoteService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-18-141-56-39.ap-southeast-1.compute.amazonaws.com:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun callStartTimer(token: String): String? {
        val service = retrofit.create(APICenter::class.java)
        val request = HomeModel.Request(token)
        val call = service.startTimer(request)
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val response: Response<HomeModel.ResponseStartTimer> = call.execute()
            Log.d("app", "Start timer")
            return response.body()?.id
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
//        call.clone().enqueue(object : Callback<HomeModel.ResponseStartTimer> {
//            override fun onResponse(
//                call: Call<HomeModel.ResponseStartTimer>?,
//                response: Response<HomeModel.ResponseStartTimer>?
//            ) {
//                Log.d("app", "Start timer")
//                val id: String = response!!.body()!!.id
//
//            }
//
//            override fun onFailure(
//                call: Call<HomeModel.ResponseStartTimer>?,
//                throwable: Throwable?
//            ) {
//                Log.w("app", "Can not start timer")
//            }
//        })
        return null
    }

    fun callStopTimer(token: String, id: String) {
        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.RequestStopTimer(token, id)
        val call = service.stopTimer(request)
        call.enqueue(object : Callback<HomeModel.ResponseStopTimer> {
            override fun onResponse(
                call: Call<HomeModel.ResponseStopTimer>?,
                response: Response<HomeModel.ResponseStopTimer>?
            ) {
                Log.d("app", "Stop timer")
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseStopTimer>?,
                throwable: Throwable?
            ) {
                Log.w("app", "Can not stop timer")
            }
        })
    }
}