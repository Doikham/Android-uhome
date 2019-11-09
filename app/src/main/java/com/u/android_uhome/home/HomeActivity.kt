package com.u.android_uhome.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.u.android_uhome.HomesService
import com.u.android_uhome.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    lateinit var interactor: HomeInteractor
    lateinit var router: HomeRouter
    lateinit var model: HomeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        HomeConfigure.configure(this)

        val bundle = intent.extras
        val tokenId = bundle!!.getString("token")

        Toast.makeText(this@HomeActivity, tokenId,
            Toast.LENGTH_LONG).show()

        deviceList.layoutManager = LinearLayoutManager(this)
        deviceList.itemAnimator = DefaultItemAnimator()
        val retrofit = Retrofit.Builder()
//                    .baseUrl(getString(R.string.baseUrl))
                    .baseUrl("http://ec2-18-139-110-142.ap-southeast-1.compute.amazonaws.com:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

//        var service = retrofit.create(HomesService::class.java)
//        val call = service.getDeviceDetail(1)
//        call.enqueue(object: Callback<List<HomeModel.Response>> {
//            override fun onResponse(call: Call<List<HomeModel.Response>>,
//                           response: Response<List<HomeModel.Response>>) {
//                setAdapterData(response.body()!!)
//            }
//            override fun onFailure(call:Call<List<HomeModel.Response>>, throwable:Throwable) {
//                Toast.makeText(this@HomeActivity, "Unable to load devices",
//                    Toast.LENGTH_SHORT).show()
//            }
//        })

        var service = retrofit.create(HomesService::class.java)
        val call = service.getDeviceList("1111")
        call.enqueue(object: Callback<List<HomeModel.Response>> {
            override fun onResponse(call: Call<List<HomeModel.Response>>,
                                    response: Response<List<HomeModel.Response>>) {
                setAdapterData(response.body()!!)
            }
            override fun onFailure(call:Call<List<HomeModel.Response>>, throwable:Throwable) {
                Toast.makeText(this@HomeActivity, "Unable to load devices",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setAdapterData(devices: List<HomeModel.Response>) {
        deviceList.adapter = HomeAdapter(devices)
    }
}
