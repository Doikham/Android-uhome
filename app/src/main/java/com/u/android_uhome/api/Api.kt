package com.u.android_uhome.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api{

    private var retrofit: Retrofit? = null
    private var baseUrl: String = "http://ec2-13-229-73-124.ap-southeast-1.compute.amazonaws.com:3000"

    fun getRetrofitInstance():Retrofit {
        if (retrofit == null)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
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