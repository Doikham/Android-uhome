package com.u.android_uhome.room

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.u.android_uhome.utils.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.estimote.EstimoteApplication
import com.u.android_uhome.estimote.EstimoteModel
import com.u.android_uhome.find.FindMyFamActivity
import com.u.android_uhome.record.RecordActivity
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

        val bundle = intent.extras
        val tokenId = bundle?.getString("tokenId")
        val homeId = bundle?.getString("homeId")

        val toolbar = toolbar1
        setSupportActionBar(toolbar)

        goHomeBtn.setOnClickListener {
            finish()
        }

        backFromRoom.setOnClickListener {
            finish()
        }

        roomList.layoutManager = LinearLayoutManager(this)
        roomList.itemAnimator = DefaultItemAnimator()

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        roomList.addItemDecoration(
            itemDecorator
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APICenter::class.java)

        val request = RoomModel.Request(tokenId!!, homeId!!)
        val call = service.getRoom(request)
        call.enqueue(object : Callback<RoomModel.ResponseMessage> {
            override fun onResponse(
                call: Call<RoomModel.ResponseMessage>?,
                response: Response<RoomModel.ResponseMessage>?
            ) {
                setAdapterData(response?.body()?.message, tokenId, homeId)
                Log.d("message", response?.body()?.message.toString())
                if (response?.body()?.message?.isEmpty()!!) {
                    progressBarRoom.visibility = View.GONE
                    Toast.makeText(
                        this@RoomActivity, "No room found",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBarRoom.visibility = View.GONE
                    roomList.visibility = View.VISIBLE
                }
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
