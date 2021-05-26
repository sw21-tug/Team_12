package com.team12.myopensecret

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ViewEntryActivity: AppCompatActivity() {

    private lateinit var titleField:TextView
    private lateinit var descriptionField:TextView
    private lateinit var chipsField:ChipGroup
    private lateinit var model: JournalDataEntry
    private lateinit var dfs: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entry)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        titleField = findViewById(R.id.title_view)
        descriptionField = findViewById(R.id.description_view)
        chipsField = findViewById(R.id.chips_view)
        dfs = findViewById(R.id.overview_data_list)
        var dataEntry = intent.extras?.getSerializable("data") as JournalDataEntry
        titleField.text = dataEntry.title
        descriptionField.text = dataEntry.description
        dataEntry.labels.forEach {
            addLabelToGroup(it, chipsField)
        }
        dataEntry.dfs.forEach {
            addDf(it)
        }
        model = dataEntry
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.delete_entry) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.title_delete))
            builder.setMessage(resources.getString(R.string.description_delete))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                MainActivity.dataBase.deleteEmployee(model)
                setResult(1)
                finish()
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_entry_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun addLabelToGroup(label: LabelData, chipGroup: ChipGroup) {
        var labelChip = Chip(this)
        labelChip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(label.color))
        labelChip.text = label.name
        labelChip.isClickable = false
        labelChip.chipStrokeWidth = 5f
        labelChip.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.black))
        chipGroup.addView(labelChip)
    }
    private fun addDf(data: DataFieldData) {
        var dfLayout: View = layoutInflater.inflate(R.layout.df_overview, dfs, false)
        dfLayout.tag = data
        dfLayout.findViewById<TextView>(R.id.df_o_name).text = (data.name)
        val x = dfLayout.findViewById<TextView>(R.id.df_o_value)
        val x2 = dfLayout.findViewById<CheckBox>(R.id.df_o_value2)
        when(data.type) {
            "2" -> {x.visibility = View.GONE; x2.visibility = View.VISIBLE; if (data.value == "true") x2.isChecked = true}
            else -> {
                x.text = data.value
            }
        }
        dfs.addView(dfLayout, 0)
    }
}