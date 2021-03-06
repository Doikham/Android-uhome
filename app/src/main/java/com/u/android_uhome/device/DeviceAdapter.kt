package com.u.android_uhome.device

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.u.android_uhome.R
import kotlinx.android.synthetic.main.device.view.*

class DeviceAdapter(
    private val devices: List<DeviceModel.ResponseDevicesList>,
    private val token: String,
    private val homeId: String
) :
    RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.device, parent, false)
        itemLayoutView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    private fun onClick(v: View?, deviceId: Int) {
        val context: Context = v!!.context
        ColorPickerDialogBuilder
            .with(context)
            .initialColor(-0x1)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorChangedListener {
            }
            .setOnColorSelectedListener { selectedColor ->
                Toast.makeText(
                    v.context,
                    "onColorSelected: 0x" + Integer.toHexString(
                        selectedColor
                    ), Toast.LENGTH_SHORT
                ).show()
            }
            .setPositiveButton(
                "ok"
            ) { _, selectedColor, allColors ->
                if (allColors != null) {
                    var sb: StringBuilder? = null
                    for (color in allColors) {
                        if (color == null) continue
                        if (sb == null) sb = StringBuilder("Color List:")
                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase())
                    }
                    if (sb != null) Toast.makeText(
                        v.context,
                        sb.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val service = DeviceService()
                service.callChangeColor(
                    token,
                    deviceId.toString(),
                    "#" + Integer.toHexString(selectedColor),
                    homeId
                )
            }
            .setNegativeButton(
                "cancel"
            ) { _, _ -> }
            .showColorEdit(true)
            .setColorEditTextColor(
                ContextCompat.getColor(
                    v.context,
                    android.R.color.holo_blue_bright
                )
            )

            .alphaSliderOnly()
            .build()
            .show()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = devices[position].deviceName
        holder.on.isChecked = devices[position].on
        holder.on.setOnCheckedChangeListener { _, _ ->
            val service = DeviceService()
            service.callToggleSwitch(token, devices[position].deviceId.toString(), homeId)
        }
        holder.itemView.setOnClickListener {
            onClick(holder.itemView, devices[position].deviceId)
        }
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val name: TextView = itemLayoutView.deviceName
        var on: Switch = itemLayoutView.toggleBtn
    }
}