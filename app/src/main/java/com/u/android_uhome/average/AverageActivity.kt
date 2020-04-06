package com.u.android_uhome.average

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.u.android_uhome.R
import com.u.android_uhome.utils.APICenter
import kotlinx.android.synthetic.main.activity_average.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AverageActivity : AppCompatActivity() {

    private var userList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_average)

        val bundle = intent.extras
        val tokenId = bundle?.getString("idToken")
        val homeId = bundle?.getString("homeId")

        val pieChart = findViewById<View>(R.id.pie_chart) as PieChart

        pieChart.setDrawEntryLabels(false)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, -110f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.radius

        pieChart.setDrawCenterText(false)

        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        l.textSize = 20F
        l.formSize = 20F

        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(20F)

        backbtn.setOnClickListener {
            finish()
        }

        backFromAverage.setOnClickListener {
            finish()
        }

        val userSpinner = selectUser
        getUser(tokenId!!, homeId!!, userSpinner, pieChart)
    }

    private fun getUser(tokenId: String, homeId: String, userSpinner: Spinner, pieChart: PieChart) {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(APICenter::class.java)
        val request = AverageModel.RequestResidentList(tokenId, homeId)
        val call = service.getUserList(request)
        call.enqueue(object : Callback<AverageModel.ResponseResidentList> {
            override fun onResponse(
                call: Call<AverageModel.ResponseResidentList>?,
                response: Response<AverageModel.ResponseResidentList>?
            ) {
                setAdapterData(response?.body()?.message, userSpinner, tokenId, homeId, pieChart)
            }

            override fun onFailure(
                call: Call<AverageModel.ResponseResidentList>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@AverageActivity, "Unable to find resident list",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun setAdapterData(
        users: List<AverageModel.ResponseList>?,
        userSpinner: Spinner,
        tokenId: String,
        homeId: String,
        pieChart: PieChart
    ) {
        for (i in users!!.indices) {
            userList.add(users[i].userName)
        }
        val userNameList = ArrayAdapter(this, android.R.layout.simple_spinner_item, userList)
        userNameList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = userNameList
        userSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                getAverage(tokenId, homeId, pieChart, users[position].userId)
            }
        })
    }

    private fun getAverage(tokenId: String, homeId: String, pieChart: PieChart, userId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(APICenter::class.java)
        val request = AverageModel.RequestAverageTime(tokenId, homeId, userId)
        val call = service.getAverageTime(request)
        call.enqueue(object : Callback<AverageModel.ResponseAverage> {
            override fun onResponse(
                call: Call<AverageModel.ResponseAverage>?,
                response: Response<AverageModel.ResponseAverage>?
            ) {
                if (response?.body()?.message?.isEmpty()!!)
                    Toast.makeText(
                        this@AverageActivity, "No average time found",
                        Toast.LENGTH_SHORT
                    ).show()
                setPieChart(response.body()?.message, pieChart)
            }

            override fun onFailure(
                call: Call<AverageModel.ResponseAverage>?,
                throwable: Throwable?
            ) {
                Toast.makeText(
                    this@AverageActivity, "Unable to find average",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun setPieChart(res: Array<Array<String>>?, pieChart: PieChart) {
        val entries: ArrayList<PieEntry> = ArrayList()
        for (i in res!!.indices) {
            entries.add(
                PieEntry(
                    ((((res[i][1].toFloat()) / 1000) / 60) + ((((res[i][1].toFloat()) / 1000) % 60).toDouble() / 60)
                            ).toFloat(),
                    res[i][0]
                )
            )
        }

        val dataSet = PieDataSet(entries, "Average time per room (minutes)")

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 80F
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 30F

        val colors = java.util.ArrayList<Int>()
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(20F)

        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.data = data

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.highlightValues(null)
        pieChart.invalidate()
    }
}
