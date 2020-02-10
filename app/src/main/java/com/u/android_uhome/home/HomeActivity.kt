package com.u.android_uhome.home

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.u.android_uhome.utils.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.record.RecordActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    private lateinit var mp: MediaPlayer

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)
        optionBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }

        val bundle = intent.extras
        val tokenId = bundle?.getString("token")

        homeList.layoutManager = LinearLayoutManager(this)
        homeList.itemAnimator = DefaultItemAnimator()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APICenter::class.java)

        val request = HomeModel.Request(tokenId!!)
        val call = service.getHome(request)
        call.enqueue(object : Callback<HomeModel.ResponseHomeMessage> {
            override fun onResponse(
                call: Call<HomeModel.ResponseHomeMessage>?,
                response: Response<HomeModel.ResponseHomeMessage>?
            ) {
                setAdapterData(response?.body()?.message, tokenId)
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseHomeMessage>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@HomeActivity, "Unable to load homes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val call1 = service.getServerStatus()
        call1.enqueue(object : Callback<HomeModel.ResponseServerStatus> {
            override fun onResponse(
                call1: Call<HomeModel.ResponseServerStatus>?,
                response: Response<HomeModel.ResponseServerStatus>?
            ) {
                Log.d("app", response?.body().toString())
                serverStatus.text = response!!.body()!!.serverStatus
            }

            override fun onFailure(
                call1: Call<HomeModel.ResponseServerStatus>?,
                throwable: Throwable?
            ) {
            }
        })

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult?> {
                override fun onComplete(@NonNull task: Task<InstanceIdResult?>) {
                    if (!task.isSuccessful) { //To do//
                        return
                    }
                    // Get the Instance ID token//
                    val fcmToken: String? = task.result?.token
                    val msg = getString(R.string.fcm_token, fcmToken)

                    sendFcm(fcmToken.toString(), tokenId, service)

                    Log.d(TAG, msg)
                }
            })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setAdapterData(homes: List<HomeModel.ResponseHome>?, token: String) {
        homeList.adapter = HomeAdapter(homes!!, token)
    }

    fun sendFcm(fcmToken: String, tokenId: String, service: APICenter) {
        val request = HomeModel.RequestAddFcm(tokenId, fcmToken)
        val call = service.addFcmToken(request)
        call.enqueue(object : Callback<HomeModel.ResponseGeneral> {
            override fun onResponse(
                call: Call<HomeModel.ResponseGeneral>?,
                response: Response<HomeModel.ResponseGeneral>?
            ) {
                Toast.makeText(
                    this@HomeActivity, response?.body()?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(
                call: Call<HomeModel.ResponseGeneral>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@HomeActivity, "Unable to send FCM token",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
