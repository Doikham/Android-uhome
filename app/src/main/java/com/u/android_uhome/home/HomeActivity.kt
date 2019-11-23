package com.u.android_uhome.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.u.android_uhome.APICenter
import com.u.android_uhome.R
import com.u.android_uhome.estimote.EstimoteApplication
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        HomeConfigure.configure(this)

        val bundle = intent.extras
        val tokenId = bundle?.getString("token")

        deviceList.layoutManager = LinearLayoutManager(this)
        deviceList.itemAnimator = DefaultItemAnimator()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var service = retrofit.create(APICenter::class.java)
        val request = HomeModel.Request(tokenId!!)
        val call = service.getDeviceList(request)
        call.enqueue(object : Callback<List<HomeModel.Response>> {
            override fun onResponse(
                call: Call<List<HomeModel.Response>>?,
                response: Response<List<HomeModel.Response>>?
            ) {
                setAdapterData(response?.body(), tokenId)
            }

            override fun onFailure(call: Call<List<HomeModel.Response>>?, throwable: Throwable?) {
                Toast.makeText(
                    this@HomeActivity, "Unable to load devices",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

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

    fun setAdapterData(devices: List<HomeModel.Response>?, token: String) {
        deviceList.adapter = HomeAdapter(devices!!,token)
    }
}
