package com.team12.myopensecret

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.collections.ArrayList


class EditEntryActivity : AppCompatActivity() {
    private lateinit var model: JournalDataEntry
    private lateinit var titleField: EditText
    private lateinit var notesField: EditText
    //private lateinit var entryList: LinearLayout
    private lateinit var labelsGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsActivity.loadLocale(this)
        setContentView(R.layout.activity_edit_entry)
        titleField = findViewById(R.id.edit_title)
        notesField = findViewById(R.id.notes_field)
        labelsGroup = findViewById(R.id.edit_chips)
        val dataEntry = intent.extras?.getSerializable("data") as JournalDataEntry
        titleField.setText(dataEntry.title)
        notesField.setText(dataEntry.description)
        setTitle(R.string.edit_entry)
        dataEntry.labels.forEach {
            addLabelToGroup(it)
        }
        loadChips()
        model = dataEntry
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_entry_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.accept_entry) {
            startSuccessfulIntent()
        } /*else if (item.itemId == R.id.delete_entry) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.title_delete))
            builder.setMessage(resources.getString(R.string.description_delete))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                MainActivity.dataBase.deleteEmployee(model)
                val intent = Intent(this, MainActivity::class.java)
                startActivityForResult(intent, 1)
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()
        }*/
        else if (item.itemId == R.id.cancel_editing_entry) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.description_cancel))
            builder.setPositiveButton(R.string.agree) { dialog, which ->
                var intent = Intent()
                intent.putExtra("JOURNAL_ENTRY", "test")
                setResult(0, intent)
                finish()
            }
            builder.setNegativeButton(R.string.disagree) { dialog, which ->
            }

            builder.show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun startSuccessfulIntent() {
        val intent = Intent()
        val title = titleField.text.toString().trim()
        val notes = notesField.text.toString().trim()
        var hasError = false
        if (title.length < 5) {
            titleField.error = resources.getString(R.string.need5chars)
            hasError = true
        }
        if (title.length > 50) {
            titleField.error = resources.getString(R.string.toomany50)
            hasError = true
        }
        if (notes.length < 20) {
            notesField.error = resources.getString(R.string.need20chars)
            hasError = true
        }
        if (notes.length > 200) {
            notesField.error = resources.getString(R.string.toomany200)
            hasError = true
        }
        var hasSelectedChip = false
        labelsGroup.checkedChipIds.forEach{
            hasSelectedChip = true
        }
        if (hasError) {
            return
        }
        val selectedLabels = ArrayList<LabelData>()
        labelsGroup.checkedChipIds.forEach{
            selectedLabels.add(findViewById<Chip>(it).tag as LabelData)
        }
        intent.putExtra("JOURNAL_ENTRY", JournalDataEntry(title, notes, selectedLabels, model.dfs, model.dbId))
        setResult(1, intent)
        finish()
    }

    private fun addLabelToGroup(label: LabelData) {
        var labelChip = Chip(this)
        labelChip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(label.color))
        labelChip.text = label.name
        labelChip.isFocusable = true
        labelChip.isClickable = true
        labelChip.isCheckable = true
        labelChip.chipStrokeWidth = 5f
        labelChip.id = View.generateViewId()
        labelChip.chipStrokeColor = ColorStateList.valueOf(resources.getColor(R.color.black))
        labelChip.tag = label
        labelsGroup.addView(labelChip)
    }

    private fun loadChips() {
        var labels = MainActivity.dataBase.viewLabelEntries()
        labels.forEach() {
            addLabelToGroup(it)
        }
    }


}