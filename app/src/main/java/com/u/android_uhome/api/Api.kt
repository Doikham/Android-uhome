package com.u.android_uhome.api

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api{

    private var retrofit: Retrofit? = null
//    private var baseUrl: String? = ""
//    private var header = hashMapOf<String,String>()

    fun <T> call(RequestClass: Class<T>): T{
        var httpClient = OkHttpClient.Builder()
        retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-229-73-124.ap-southeast-1.compute.amazonaws.com:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        return retrofit!!.create(RequestClass)
    }

//    fun setCallBack(call: Call<ResponseBody>, onSuccess: (jsonData: String -> Unit)) {
//        call.enqueue(object : Callback<ResponseBody>{
//            fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
//                val dataResponse = response.body()!!.string()
//                onSuccess(dataResponse)
//            }
//        })
//    }
}