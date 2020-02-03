package com.u.android_uhome.light

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.OnColorChangedListener
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.u.android_uhome.R

class LightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light)

            val context: Context = this
            ColorPickerDialogBuilder
                .with(context)
//                .setTitle(R.string.color_dialog_title)
                .initialColor(-0x1)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorChangedListener(OnColorChangedListener { selectedColor ->
                    // Handle on color change
                    Log.d(
                        "ColorPicker",
                        "onColorChanged: 0x" + Integer.toHexString(selectedColor)
                    )
                })
                .setOnColorSelectedListener(OnColorSelectedListener { selectedColor ->
                    Toast.makeText(this,
                        "onColorSelected: 0x" + Integer.toHexString(
                            selectedColor
                        ),Toast.LENGTH_SHORT
                    ).show()
                })
                .setPositiveButton("ok",
                    ColorPickerClickListener { dialog, selectedColor, allColors ->
//                        changeBackgroundColor(selectedColor)
                        if (allColors != null) {
                            var sb: StringBuilder? = null
                            for (color in allColors) {
                                if (color == null) continue
                                if (sb == null) sb = StringBuilder("Color List:")
                                sb.append("\r\n#" + Integer.toHexString(color).toUpperCase())
                            }
                            if (sb != null) Toast.makeText(
                                applicationContext,
                                sb.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, which -> })
                .showColorEdit(true)
                .setColorEditTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_blue_bright
                    )
                )
                .build()
                .show()

    }
}
