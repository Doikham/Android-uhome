package com.u.android_uhome.record

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.u.android_uhome.R
import com.u.android_uhome.utils.APICenter
import kotlinx.android.synthetic.main.activity_home.toolbar1
import kotlinx.android.synthetic.main.activity_record.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class RecordActivity : AppCompatActivity() {

    private var buttonDate: Button? = null
    private var textDate: TextView? = null
    var cal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val barChart = findViewById<View>(R.id.barchart) as BarChart

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)

        goBackBtn.setOnClickListener {
            finish()
        }

        val bundle = intent.extras
        val tokenId = bundle?.getString("idToken")
        val homeId = bundle?.getString("homeId")

        // get the references from layout file
        textDate = this.displayDate
        buttonDate = this.changeDateBtn

        val calendar = Calendar.getInstance(TimeZone.getDefault())

        updateDateInView(tokenId!!, barChart, homeId!!)
        // create an OnDateSetListener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(tokenId, barChart, homeId)
            }
        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        buttonDate!!.setOnClickListener {
            DatePickerDialog(
                this@RecordActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView(token: String, barChart: BarChart, homeId: String) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textDate!!.text = sdf.format(cal.time)
        getDataToGraph(token, barChart, sdf.format(cal.time), homeId)
    }

    private fun getDataToGraph(tokenId: String, barChart: BarChart, date: String, homeId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(APICenter::class.java)
        val request = RecordModel.RequestRecord(tokenId, date, homeId)
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

    fun setBarGraph(res: Array<Array<String>>?, barChart: BarChart) {
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in res!!.indices) {
            entries.add(
                BarEntry(
                    ((((res[i][1].toLong()) / 1000) / 60) + ((((res[i][1].toLong()) / 1000) % 60).toDouble() / 60)
                            ).toFloat(), i
                )
            )
        }
        val labels = ArrayList<String>()
        for (j in res.indices) {
            labels.add(res[j][0])
        }
        val barDataSet = BarDataSet(entries, "Rooms")
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.valueTextSize = 20.0F
        barChart.animateY(2000)
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of labels into chart
        barChart.setDescription("unit in minutes") // set the description
        barChart.setDescriptionTextSize(20.0F)
    }
}
