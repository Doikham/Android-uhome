package com.u.android_uhome.device

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import com.flask.colorpicker.OnColorChangedListener
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.u.android_uhome.R
import com.u.android_uhome.home.HomeService
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
//                .setTitle(R.string.color_dialog_title)
            .initialColor(-0x1)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorChangedListener { selectedColor ->
                // Handle on color change
                Log.d(
                    "ColorPicker",
                    "onColorChanged: 0x" + Integer.toHexString(selectedColor)
                )
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
                Log.d("color", Integer.toHexString(selectedColor))
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
            .build()
            .show()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = devices[position].deviceName
        holder.id.text = devices[position].deviceId.toString()
        holder.on.isChecked = devices[position].on
        holder.on.setOnCheckedChangeListener { _, _ ->
            val service = HomeService()
            service.callToggleSwitch(token, devices[position].deviceId.toString(), homeId)
        }
        holder.itemView.setOnClickListener {
            Log.d("info", devices[position].deviceId.toString())
            onClick(holder.itemView, devices[position].deviceId)
        }
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val id: TextView = itemLayoutView.deviceId
        val name: TextView = itemLayoutView.deviceName
        var on: Switch = itemLayoutView.toggleBtn
    }
}