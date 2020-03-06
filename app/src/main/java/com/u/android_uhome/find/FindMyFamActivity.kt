package com.u.android_uhome.find

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.u.android_uhome.R
import com.u.android_uhome.utils.APICenter
import kotlinx.android.synthetic.main.activity_find_my_fam.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.toolbar1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindMyFamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_my_fam)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)

        goBackBtn.setOnClickListener {
            finish()
        }

        progressBar
        val bundle = intent.extras
        val tokenId = bundle?.getString("idToken")
        val homeId = bundle?.getString("homeId")

        memberList.layoutManager = LinearLayoutManager(this)
        memberList.itemAnimator = DefaultItemAnimator()

        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)

        memberList.addItemDecoration(
            itemDecorator
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APICenter::class.java)
        val request = FindMyFamModel.Request(tokenId!!, homeId!!)
        val call = service.findMember(request)
        call.enqueue(object : Callback<FindMyFamModel.Response> {
            override fun onResponse(
                call: Call<FindMyFamModel.Response>?,
                response: Response<FindMyFamModel.Response>?
            ) {
                setAdapterData(response?.body()?.message, tokenId, homeId)
                Log.d("message", response?.body()?.message.toString())
                if(response?.body()?.message?.isEmpty()!!)
                    progressBar.visibility = View.VISIBLE
                else
                    progressBar.visibility = View.GONE
            }

            override fun onFailure(
                call: Call<FindMyFamModel.Response>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@FindMyFamActivity, "Unable to load members",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun setAdapterData(
        members: List<FindMyFamModel.ResponseFamily>?,
        token: String,
        homeId: String
    ) {
        memberList.adapter = FindMyFamAdapter(members!!, token, homeId)
    }
}
