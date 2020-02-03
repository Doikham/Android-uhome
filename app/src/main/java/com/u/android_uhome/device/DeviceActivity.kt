package com.u.android_uhome.device

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.u.android_uhome.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.home.HomeAdapter
import com.u.android_uhome.home.HomeModel
import com.u.android_uhome.record.RecordActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

//        val actionbar = supportActionBar
//        actionbar?.setDisplayHomeAsUpEnabled(true)
//
//        val toolbar = toolbar1
//        setSupportActionBar(toolbar)
//        optionBtn.setOnClickListener {
//            val intent = Intent(this, RecordActivity::class.java)
//            startActivity(intent)
//        }
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(getString(R.string.baseUrl))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        var service = retrofit.create(APICenter::class.java)
//        val request = HomeModel.Request(tokenId!!)
//        val call = service.getDeviceList(request)
//        call.enqueue(object : Callback<List<HomeModel.Response>> {
//            override fun onResponse(
//                call: Call<List<HomeModel.Response>>?,
//                response: Response<List<HomeModel.Response>>?
//            ) {
//                setAdapterData(response?.body(), tokenId)
//            }
//
//            override fun onFailure(call: Call<List<HomeModel.Response>>?, throwable: Throwable?) {
//                Toast.makeText(
//                    this@HomeActivity, "Unable to load devices",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//
//    fun setAdapterData(devices: List<HomeModel.Response>?, token: String) {
//        homeList.adapter = DeviceAdapter(devices!!,token)
//    }
}
