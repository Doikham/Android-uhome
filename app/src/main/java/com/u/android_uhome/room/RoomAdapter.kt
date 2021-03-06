package com.u.android_uhome.room

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.u.android_uhome.R
import com.u.android_uhome.device.DeviceActivity
import kotlinx.android.synthetic.main.room.view.*

class RoomAdapter(
    private val rooms: List<RoomModel.ResponseRoomList>,
    private val token: String,
    private val homeId: String
) :
    RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.room, parent, false)
        itemLayoutView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(itemLayoutView)
    }

    private fun onClick(v: View?, roomId: Int) {
        val intent = Intent(v!!.context, DeviceActivity::class.java)
        intent.putExtra("roomId", roomId)
        intent.putExtra("tokenId", token)
        intent.putExtra("homeId", homeId)
        v.context.startActivity(intent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (rooms[position].roomType) {
            "Bathroom" -> holder.image.setImageResource(R.drawable.bath)
            "Living room" -> holder.image.setImageResource(R.drawable.chair)
            "Kitchen" -> holder.image.setImageResource(R.drawable.kitchen)
            "Bedroom" -> holder.image.setImageResource(R.drawable.bed)
        }
        holder.name.text = rooms[position].roomName
        holder.type.text = rooms[position].roomType
        holder.itemView.setOnClickListener {
            onClick(holder.itemView, rooms[position].roomId)
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val name: TextView = itemLayoutView.roomName
        val type: TextView = itemLayoutView.roomType
        val image: ImageView = itemLayoutView.imageView2
    }
}