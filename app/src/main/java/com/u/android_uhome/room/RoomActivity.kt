package com.u.android_uhome.room

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.u.android_uhome.utils.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.estimote.EstimoteApplication
import com.u.android_uhome.estimote.EstimoteModel
import com.u.android_uhome.record.RecordActivity
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

        // Starting estimote
        val app = application as EstimoteApplication

        val requestEstimoteApp = EstimoteModel.RequestApp(tokenId, homeId)
        val callEstimoteService = service.getEstimoteApp(requestEstimoteApp)
        callEstimoteService.enqueue(object : Callback<EstimoteModel.ResponseApp> {
            override fun onResponse(
                call: Call<EstimoteModel.ResponseApp>?,
                response: Response<EstimoteModel.ResponseApp>?
            ) {
//                sendAppId(response?.body()?.appId, response?.body()?.appToken)
//                getString(R.string.estimote_app_id, response?.body()?.appId)
//                getString(R.string.estimote_app_token, response?.body()?.appToken)
                val editor =
                    getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit()
                editor.putString("app_id", response?.body()?.appId)
                editor.putString("app_token", response?.body()?.appToken)
                editor.apply()
            }

            override fun onFailure(call: Call<EstimoteModel.ResponseApp>?, throwable: Throwable?) {
                Toast.makeText(
                    this@RoomActivity, "Unable to load rooms",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                onRequirementsFulfilled = {
                    Log.d("app", "requirements fulfilled")
                    app.enableBeaconNotifications(tokenId)
                },
                onRequirementsMissing = { requirements ->
                    Log.e("app", "requirements missing: $requirements")
                },

                onError = { throwable ->
                    Log.e("app", "requirements error: $throwable")
                })
    }

    fun setAdapterData(rooms: List<RoomModel.ResponseRoomList>?, token: String, homeId: String) {
        roomList.adapter = RoomAdapter(rooms!!, token, homeId)
    }
}
