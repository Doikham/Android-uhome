package com.u.android_uhome.home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.u.android_uhome.R
import com.u.android_uhome.room.RoomActivity
import kotlinx.android.synthetic.main.home.view.*

class HomeAdapter(private val homes: List<HomeModel.ResponseHome>, private val token: String) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home, parent, false)
        itemLayoutView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
//        itemLayoutView.setOnClickListener(v)

        return ViewHolder(itemLayoutView)
    }

    private fun onClick(v: View?, homeId: Int) {
        val intent = Intent(v!!.context, RoomActivity::class.java)
        intent.putExtra("homeId", homeId)
        intent.putExtra("tokenId", token)
        v.context.startActivity(intent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = homes[position].homeId.toString()
        holder.name.text = homes[position].homeName
        holder.itemView.setOnClickListener {
            Log.d("info", homes[position].homeId.toString())
            onClick(holder.itemView, homes[position].homeId)
        }
    }

    override fun getItemCount(): Int {
        return homes.size
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val id: TextView = itemLayoutView.homeId
        val name: TextView = itemLayoutView.homeName
    }
}