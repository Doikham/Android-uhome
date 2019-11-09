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
//    lateinit var api: Api

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
//        private var retrofit: Retrofit? = null
        var baseUrl = "http://ec2-13-229-73-124.ap-southeast-1.compute.amazonaws.com:3000"

        val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

//        var service = api.getRetrofitInstance().create(HomesService::class.java)
        var service = retrofit.create(HomesService::class.java)
        val call = service.getDeviceDetail(1)
        call.enqueue(object: Callback<List<HomeModel.Response>> {
            //Handle a successful response//
            override fun onResponse(call: Call<List<HomeModel.Response>>,
                           response: Response<List<HomeModel.Response>>) {
//                print(response.body())
//                Toast.makeText(this@HomeActivity, response.body().toString(),
//                    Toast.LENGTH_LONG).show()
                setAdapterData(response.body()!!)
            }
            //Handle execution failures//
            override fun onFailure(call:Call<List<HomeModel.Response>>, throwable:Throwable) {
                //If the request fails, then display the following toast//
                Toast.makeText(this@HomeActivity, "Unable to load devices",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setAdapterData(devices: List<HomeModel.Response>) {
        deviceList.adapter = HomeAdapter(devices)
    }
}
