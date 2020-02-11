package com.u.android_uhome.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.u.android_uhome.R

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val barChart = findViewById<View>(R.id.barchart) as BarChart

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(8f, 0))
        entries.add(BarEntry(2f, 1))
        entries.add(BarEntry(5f, 2))

        val bardataset = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("Restroom")
        labels.add("Bedroom")
        labels.add("Living room")

        val data = BarData(labels, bardataset)
        barChart.data = data // set the data and list of labels into chart

        barChart.setDescription("Set Bar Chart Description Here") // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        barChart.animateY(5000)
    }
}
