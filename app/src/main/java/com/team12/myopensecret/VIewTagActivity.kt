package com.team12.myopensecret

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

class VIewTagActivity : AppCompatActivity() {

    private lateinit var tagsList:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_overview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tagsList = findViewById(R.id.tags_list)
        MainActivity.dataBase.viewLabelEntries().forEach{ label ->
            addLabel(label)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addLabel(data: LabelData) {
        var tagLayout: View = layoutInflater.inflate(R.layout.tag_layout, tagsList, false)
        tagLayout.findViewById<TextView>(R.id.tag_name).text = (data.name)
        tagLayout.findViewById<ImageView>(R.id.tag_image).setOnClickListener {
            // TODO: EDIT
        }
        tagLayout.findViewById<TextView>(R.id.tag_color).background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(data.color), BlendModeCompat.SRC_ATOP)
        tagsList.addView(tagLayout, 0)
    }
}