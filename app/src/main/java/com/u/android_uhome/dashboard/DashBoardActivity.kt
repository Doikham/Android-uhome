package com.u.android_uhome.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.u.android_uhome.R
import com.u.android_uhome.average.AverageActivity
import com.u.android_uhome.estimote.EstimoteApplication
import com.u.android_uhome.estimote.EstimoteModel
import com.u.android_uhome.find.FindMyFamActivity
import com.u.android_uhome.record.RecordActivity
import com.u.android_uhome.room.RoomActivity
import com.u.android_uhome.utils.APICenter
import kotlinx.android.synthetic.main.activity_dash_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val shared: SharedPreferences =
            getSharedPreferences("MyPref", Context.MODE_PRIVATE)

        val bundle = intent.extras
        val homeName = bundle?.getString("homeName")
        val tokenId = bundle?.getString("tokenId")
        val homeId = bundle?.getString("homeId")

        houseName.text = homeName

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbarDashBoard
        setSupportActionBar(toolbar)

        goHome.setOnClickListener {
            finish()
        }

        roomCard.setOnClickListener {
            goToRoomPage()
        }

        findMyFamCard.setOnClickListener {
            goToFindFamPage()
        }

        recordCard.setOnClickListener {
            goToStatPage()
        }

        averageCard.setOnClickListener {
            goToAveragePage()
        }

        backFromDash.setOnClickListener {
            finish()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APICenter::class.java)

        val requestEstimoteApp = EstimoteModel.RequestApp(tokenId!!, homeId!!)
        val callEstimoteService = service.getEstimoteApp(requestEstimoteApp)
        callEstimoteService.enqueue(object : Callback<EstimoteModel.ResponseAppSuccess> {
            override fun onResponse(
                call: Call<EstimoteModel.ResponseAppSuccess>?,
                response: Response<EstimoteModel.ResponseAppSuccess>?
            ) {
                if (response?.body()?.appId.isNullOrEmpty() || response?.body()?.appToken.isNullOrEmpty()) {
                    Toast.makeText(
                        this@DashBoardActivity, response?.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val editor: SharedPreferences.Editor = shared.edit()
                    editor.putString("app_id", response?.body()?.appId)
                    editor.putString("app_token", response?.body()?.appToken)
                    editor.apply()
                    Toast.makeText(
                        this@DashBoardActivity, shared.getString("app_id", ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<EstimoteModel.ResponseAppSuccess>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@DashBoardActivity, "Unable to get estimote credential",
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
                    app.enableBeaconNotifications(tokenId, shared)
                },
                onRequirementsMissing = { requirements ->
                    Log.e("app", "requirements missing: $requirements")
                },

                onError = { throwable ->
                    Log.e("app", "requirements error: $throwable")
                })

    }

    private fun goToRoomPage() {
        val bundle = intent.extras
        val tokenId = bundle?.getString("tokenId")
        val homeId = bundle?.getString("homeId")
        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra("homeId", homeId)
        intent.putExtra("tokenId", tokenId)
        startActivity(intent)
    }
    private fun goToStatPage() {
        val bundle = intent.extras
        val tokenId = bundle?.getString("tokenId")
        val homeId = bundle?.getString("homeId")
        val intent = Intent(this, RecordActivity::class.java)
        intent.putExtra("idToken", tokenId)
        intent.putExtra("homeId", homeId)
        startActivity(intent)
    }

    private fun goToFindFamPage() {
        val bundle = intent.extras
        val tokenId = bundle?.getString("tokenId")
        val homeId = bundle?.getString("homeId")
        val intent = Intent(this, FindMyFamActivity::class.java)
        intent.putExtra("idToken", tokenId)
        intent.putExtra("homeId", homeId)
        startActivity(intent)
    }

    private fun goToAveragePage() {
        val bundle = intent.extras
        val tokenId = bundle?.getString("tokenId")
        val homeId = bundle?.getString("homeId")
        val intent = Intent(this, AverageActivity::class.java)
        intent.putExtra("idToken", tokenId)
        intent.putExtra("homeId", homeId)
        startActivity(intent)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getEstimoteCredential(shared: SharedPreferences): EstimoteCloudCredentials {
        return EstimoteCloudCredentials(
            shared.getString("app_id", ""),
            shared.getString("app_token", "")
        )
    }
}
