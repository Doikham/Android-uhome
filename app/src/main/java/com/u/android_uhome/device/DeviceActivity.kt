package com.u.android_uhome.device

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.u.android_uhome.utils.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.record.RecordActivity
import kotlinx.android.synthetic.main.activity_device.*
import kotlinx.android.synthetic.main.activity_home.optionBtn
import kotlinx.android.synthetic.main.activity_home.toolbar1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)
        optionBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }

        val bundle = intent.extras
        val tokenId = bundle?.getString("tokenId")
        val roomId = bundle?.getInt("roomId").toString()
        val homeId = bundle?.getString("homeId")

        deviceList.layoutManager = LinearLayoutManager(this)
        deviceList.itemAnimator = DefaultItemAnimator()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(APICenter::class.java)
        val request = DeviceModel.Request(tokenId!!, roomId)
        val call = service.getDevices(request)
        call.enqueue(object : Callback<DeviceModel.ResponseMessage> {
            override fun onResponse(
                call: Call<DeviceModel.ResponseMessage>?,
                response: Response<DeviceModel.ResponseMessage>?
            ) {
                setAdapterData(response?.body()?.message, tokenId, homeId!!)
            }

            override fun onFailure(
                call: Call<DeviceModel.ResponseMessage>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@DeviceActivity, "Unable to load devices",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setAdapterData(
        devices: List<DeviceModel.ResponseDevicesList>?,
        token: String,
        homeId: String
    ) {
        deviceList.adapter = DeviceAdapter(devices!!, token, homeId)
    }
}
