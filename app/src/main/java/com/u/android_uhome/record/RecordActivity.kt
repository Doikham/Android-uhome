package com.u.android_uhome.record

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
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
import kotlin.collections.ArrayList

class RecordActivity : AppCompatActivity() {

    private var buttonDate: Button? = null
    private var textDate: TextView? = null
    var cal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val barChart = findViewById<View>(R.id.barchart) as BarChart

        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.animateY(2000)
        barChart.setMaxVisibleValueCount(60)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)
        barChart.setExtraOffsets(5f, 10f, 5f, 5f)

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxisPosition.BOTH_SIDED
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.textSize = 22F
        xAxis.labelCount = 7

        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.textSize = 30F
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(8, false)
        rightAxis.spaceTop = 15f
        rightAxis.textSize = 30F
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val l: Legend = barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.textSize = 22F
        l.formSize = 30F
        l.xEntrySpace = 4f

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val toolbar = toolbar1
        setSupportActionBar(toolbar)

        goBackBtn.setOnClickListener {
            finish()
        }

        backFromRecord.setOnClickListener {
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
                    i.toFloat(),
                    ((((res[i][1].toLong()) / 1000) / 60) + ((((res[i][1].toLong()) / 1000) % 60).toDouble() / 60)
                            ).toFloat()
                )
            )
        }

        val barDataSet = BarDataSet(entries, "Time in room (minutes)")

        val colors = java.util.ArrayList<Int>()
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        barDataSet.colors = colors

        val labels = ArrayList<String>()
        for (j in res.indices) {
            labels.add(res[j][0])
        }
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String? {
                return labels[value.toInt()]
            }
        }

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = formatter

        val data = BarData(barDataSet)
        data.setValueTextSize(30F)
        data.barWidth = 0.9F

        barChart.animateY(2000)
        barChart.data = data
        barChart.invalidate()
    }
}
