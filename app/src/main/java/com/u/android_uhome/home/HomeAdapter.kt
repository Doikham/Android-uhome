package com.u.android_uhome.home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.u.android_uhome.R
import com.u.android_uhome.dashboard.DashBoardActivity
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

        return ViewHolder(itemLayoutView)
    }

    private fun onClick(v: View?, homeId: String, homeName: String) {
        val intent = Intent(v!!.context, DashBoardActivity::class.java)
        intent.putExtra("homeId", homeId)
        intent.putExtra("tokenId", token)
        intent.putExtra("homeName", homeName)
        v.context.startActivity(intent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = homes[position].homeName
        holder.itemView.setOnClickListener {
            Log.d("info", homes[position].homeId.toString())
            onClick(holder.itemView, homes[position].homeId.toString(), homes[position].homeName)
        }
    }

    override fun getItemCount(): Int {
        return homes.size
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val name: TextView = itemLayoutView.homeName
    }
}