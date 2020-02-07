package com.u.android_uhome.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.u.android_uhome.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.record.RecordActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.optionBtn
import kotlinx.android.synthetic.main.activity_home.toolbar1
import kotlinx.android.synthetic.main.activity_room.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

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
        val homeId = bundle?.getInt("homeId").toString()

        roomList.layoutManager = LinearLayoutManager(this)
        roomList.itemAnimator = DefaultItemAnimator()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(APICenter::class.java)

        val request = RoomModel.Request(tokenId!!, homeId)
        val call = service.getRoom(request)
        call.enqueue(object : Callback<RoomModel.ResponseMessage> {
            override fun onResponse(
                call: Call<RoomModel.ResponseMessage>?,
                response: Response<RoomModel.ResponseMessage>?
            ) {
                setAdapterData(response?.body()?.message, tokenId, homeId)
            }

            override fun onFailure(call: Call<RoomModel.ResponseMessage>?, throwable: Throwable?) {
                Toast.makeText(
                    this@RoomActivity, "Unable to load rooms",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun setAdapterData(rooms: List<RoomModel.ResponseRoomList>?, token: String, homeId: String) {
        roomList.adapter = RoomAdapter(rooms!!, token, homeId)
    }
}
