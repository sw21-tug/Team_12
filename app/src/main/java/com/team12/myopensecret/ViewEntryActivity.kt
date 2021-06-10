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
import android.content.Intent
import android.widget.Toast

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
        if(item.itemId == R.id.edit_entry){
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra("data", model)
            startActivityForResult(intent, 0)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
        if(requestCode == 0 && resultCode == 1) {
            if (data != null) {
                val dataEntry = data.extras?.getSerializable("JOURNAL_ENTRY") as JournalDataEntry
                val suc = MainActivity.dataBase.updateEmployee(dataEntry)
                if (suc == -1) {
                    Toast.makeText(this, "Failed Inserting into Database", Toast.LENGTH_SHORT).show()
                    return
                }
                updateView(dataEntry)
            }
        }
    }

    private fun updateView(dataEntry: JournalDataEntry) {

        //FIXME: Doesn't update new values
        findViewById<TextView>(R.id.title_view).text = dataEntry.title
        findViewById<TextView>(R.id.description_view).text = dataEntry.description
        //findViewById<ChipGroup>(R.id.chips_view).tag = dataEntry.labels
        model = dataEntry
    }
}