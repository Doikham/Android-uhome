package com.u.android_uhome.find

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.u.android_uhome.R
import kotlinx.android.synthetic.main.member.view.*

class FindMyFamAdapter(
    private val members: List<FindMyFamModel.ResponseFamily>
) :
    RecyclerView.Adapter<FindMyFamAdapter.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.member, parent, false)
        itemLayoutView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(itemLayoutView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = members[position].userName
        holder.roomName.text = "Room: " + members[position].roomName
        holder.enterTime.text = "Time: " + members[position].enterTime
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val userName: TextView = itemLayoutView.userName
        val roomName: TextView = itemLayoutView.roomName
        val enterTime: TextView = itemLayoutView.enterTime
    }

    override fun getItemCount(): Int {
        return members.size
    }
}