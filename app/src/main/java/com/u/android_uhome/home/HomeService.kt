package com.u.android_uhome.home

import com.u.android_uhome.HomesService
import com.u.android_uhome.api.Api

class HomeService {
    fun deviceList(onSuccess: () -> Unit) {
//        val body =  hashMapOf<String,String>()
        val api = Api()
        val request =  api.call(HomesService::class.java)
//        api.setCallBack(
//            call = request.getDeviceDetail(1)
//        )
    }
}