package com.u.android_uhome.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.scanning_plugin.api.EstimoteBluetoothScannerFactory
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.iid.FirebaseInstanceId
import com.u.android_uhome.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.estimote.EstimoteApplication
import com.u.android_uhome.record.RecordActivity
import com.u.android_uhome.room.RoomActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    lateinit var interactor: HomeInteractor
    lateinit var router: HomeRouter
    lateinit var model: HomeModel
    private lateinit var mp: MediaPlayer

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        HomeConfigure.configure(this)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)
        optionBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }

        val tkn: String = FirebaseInstanceId.getInstance().id

        val bundle = intent.extras
        val tokenId = bundle?.getString("token")

        homeList.layoutManager = LinearLayoutManager(this)
        homeList.itemAnimator = DefaultItemAnimator()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(APICenter::class.java)

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

        // Starting estimote
        val app = application as EstimoteApplication

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setAdapterData(homes: List<HomeModel.ResponseHome>?, token: String) {
        homeList.adapter = HomeAdapter(homes!!, token)
    }

//    override fun onDestroy () {
//        super.onDestroy ()
//        mp.release ()
//    }
}
