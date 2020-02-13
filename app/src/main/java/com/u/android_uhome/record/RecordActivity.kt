package com.u.android_uhome.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.u.android_uhome.R
import com.u.android_uhome.utils.APICenter
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val barChart = findViewById<View>(R.id.barchart) as BarChart

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)

        val bundle = intent.extras
        val tokenId = bundle?.getString("idToken").toString()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APICenter::class.java)

        val request = RecordModel.RequestRecord(tokenId)
        val call = service.getUserActivity(request)
        call.enqueue(object : Callback<RecordModel.ResponseRecord> {
            override fun onResponse(
                call: Call<RecordModel.ResponseRecord>?,
                response: Response<RecordModel.ResponseRecord>?
            ) {
                setBarGraph(response?.body()?.message, barChart)
            }

            override fun onFailure(call: Call<RecordModel.ResponseRecord>?, throwable: Throwable?) {
                Toast.makeText(
                    this@RecordActivity, "Unable to load records",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun setBarGraph(res: Array<IntArray>?, barChart: BarChart) {

        val entries: ArrayList<BarEntry> = ArrayList()
//        entries.add(BarEntry(8f, 0))
//        entries.add(BarEntry(2f, 1))
//        entries.add(BarEntry(5f, 2))
//        entries.add(BarEntry(res?.get(0)?.get(1)?.toFloat()!!, 0))
        for (i in res!!.indices) {
            entries.add(BarEntry(res[i][1].toFloat(), i))
        }

        val bardataset = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
//        labels.add("Restroom")
//        labels.add("Bedroom")
//        labels.add("Living room")
//        labels.add(res[0][0].toString())
        for (j in res.indices) {
            labels.add(res[j][0].toString())
        }


        val data = BarData(labels, bardataset)
        barChart.data = data // set the data and list of labels into chart

        barChart.setDescription("unit in millisecond") // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        barChart.animateY(5000)
    }
}
