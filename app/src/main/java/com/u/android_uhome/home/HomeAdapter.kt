package com.u.android_uhome.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.u.android_uhome.R
import kotlinx.android.synthetic.main.item.view.*

class HomeAdapter(private val devices: List<HomeModel.Response>, private val token: String) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent,false)
        itemLayoutView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = devices[position].deviceName
        holder.status.text = devices[position].status
        holder.userId.text = devices[position].userId
        holder.deviceId.text = devices[position].deviceId
        holder.on.isChecked = devices[position].on
        holder.on.setOnCheckedChangeListener { _, _ ->
            val service = HomeService()
            service.callToggleSwitch(token, devices[position].deviceId)
            Log.d("app","$service")
        }
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val name: TextView = itemLayoutView.deviceName
        val status: TextView = itemLayoutView.deviceStatus
        val userId: TextView = itemLayoutView.userId
        val deviceId: TextView = itemLayoutView.deviceId
        var on: Switch = itemLayoutView.toggleBtn
    }
}