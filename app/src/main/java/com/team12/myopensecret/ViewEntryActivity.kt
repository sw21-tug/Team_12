package com.team12.myopensecret

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
private lateinit var entryList: LinearLayout

class ViewEntryActivity: AppCompatActivity() {

    private lateinit var titleField:TextView
    private lateinit var descriptionField:TextView
    private lateinit var chipsField:ChipGroup
    private lateinit var model: JournalDataEntry
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entry)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        titleField = findViewById(R.id.title_view)
        descriptionField = findViewById(R.id.description_view)
        chipsField = findViewById(R.id.chips_view)
        var dataEntry = intent.extras?.getSerializable("data") as JournalDataEntry
        titleField.text = dataEntry.title
        descriptionField.text = dataEntry.description
        dataEntry.labels.forEach {
            addLabelToGroup(it, chipsField)
        }
        model = dataEntry
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        /* if (item.itemId == R.id. delete_entry) {
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

        return super.onOptionsItemSelected(item)*/
        if(item.itemId == R.id.edit_entry){
            val intent = Intent(this, EditEntryActivity::class.java)
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

    private fun addToJournalsList(data: JournalDataEntry) {
        var journalView: View = layoutInflater.inflate(R.layout.journal_entry, entryList, false)
        journalView.findViewById<TextView>(R.id.journal_title).text = (data.title)
        var desc = data.description
        if (desc.length > 80)
            desc = desc.substring(0, 80) + "..."
        journalView.findViewById<TextView>(R.id.journal_description).text = desc
        data.labels.forEach {
            addLabelToGroup(it, journalView.findViewById<ChipGroup>(R.id.journal_chips))
        }
        journalView.setOnClickListener{
            val intent = Intent(this, EditEntryActivity::class.java)
            intent.putExtra("data", data)
            startActivityForResult(intent, 1)

        }
        entryList.addView(journalView, 0)
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
}