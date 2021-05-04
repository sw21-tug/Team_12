package com.team12.myopensecret

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class VIewTagActivity : AppCompatActivity() {

    private lateinit var titleField: TextView
    private lateinit var descriptionField: TextView
    private lateinit var chipsField: ChipGroup
    private lateinit var imageFiled: ImageView
    private lateinit var model: JournalDataEntry
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_overview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        titleField = findViewById(R.id.title_view)
        descriptionField = findViewById(R.id.description_view)
        chipsField = findViewById(R.id.chips_view)
        imageFiled = findViewById(R.id.imageView)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_tag) {
            MainActivity.dataBase.addJournalEntry(model)
            setResult(1)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addLabelToGroup(label: LabelData, chipGroup: ChipGroup) {
        val labelChip = Chip(this)
        labelChip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(label.color))
        labelChip.text = label.name
        labelChip.isClickable = false
        labelChip.chipStrokeWidth = 5f
        labelChip.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.black))
        chipGroup.addView(labelChip)
    }
}
