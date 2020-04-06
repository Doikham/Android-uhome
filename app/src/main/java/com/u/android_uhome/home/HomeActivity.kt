package com.u.android_uhome.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.u.android_uhome.utils.APICenter
import com.u.android_uhome.R
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val tokenId = bundle?.getString("token")

        val toolbar = toolbar1
        setSupportActionBar(toolbar)

        goUserBtn.setOnClickListener {
            finish()
        }

        backFromHome.setOnClickListener {
            finish()
        }

        homeList.layoutManager = LinearLayoutManager(this)
        homeList.itemAnimator = DefaultItemAnimator()

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        homeList.addItemDecoration(
            itemDecorator
        )

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
                if (response?.body()?.message?.isEmpty()!!) {
                    progressBarHome.visibility = View.GONE
                    Toast.makeText(
                        this@HomeActivity, "No house found",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBarHome.visibility = View.GONE
                    homeList.visibility = View.VISIBLE
                }
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

        val callGetStatus = service.getServerStatus()
        callGetStatus.enqueue(object : Callback<HomeModel.ResponseServerStatus> {
            override fun onResponse(
                call1: Call<HomeModel.ResponseServerStatus>?,
                response: Response<HomeModel.ResponseServerStatus>?
            ) {
                serverStatus.text = response!!.body()!!.serverStatus
            }

            override fun onFailure(
                call1: Call<HomeModel.ResponseServerStatus>?,
                throwable: Throwable?
            ) {
            }
        })
        val shared: SharedPreferences =
            getSharedPreferences("MyPref", Context.MODE_PRIVATE)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult?> {
                override fun onComplete(@NonNull task: Task<InstanceIdResult?>) {
                    if (!task.isSuccessful) { //To do//
                        return
                    }

                    val fcmToken: String? = task.result?.token
                    val msg = getString(R.string.fcm_token, fcmToken)

                    if (shared.getString("fcmToken", "") == fcmToken)
                        return

                    sendFcm(fcmToken.toString(), tokenId, service)
                    val editor: SharedPreferences.Editor = shared.edit()
                    editor.putString("fcmToken", fcmToken)
                    editor.apply()

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
