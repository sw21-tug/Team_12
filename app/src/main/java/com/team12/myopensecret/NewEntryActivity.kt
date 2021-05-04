package com.team12.myopensecret

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class NewEntryActivity : AppCompatActivity() {

    private lateinit var titleField: EditText
    private lateinit var notesField: EditText
    private lateinit var labelsGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsActivity.loadLocale(this)
        setContentView(R.layout.new_journal)

        setTitle(R.string.new_entry)
        titleField = findViewById(R.id.title_field)
        notesField = findViewById(R.id.notes_field)
        labelsGroup = findViewById(R.id.chipsPrograms)
        titleField.setHint(R.string.title)
        notesField.setHint(R.string.notes)
        loadChips()
    }

    private fun loadChips() {
        var labels = MainActivity.dataBase.viewLabelEntries()
        labels.forEach() {
            addLabelToGroup(it)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_entry_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.accept_entry) {
            startSuccessfulIntent()
        } else if (item.itemId == R.id.decline_entry) {
           startCancelIntent()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun startSuccessfulIntent() {
        var intent = Intent()
        var title = titleField.text.toString().trim()
        var notes = notesField.text.toString().trim()
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
        val category = resources.getString(R.string.choosecategory)
        val length = Toast.LENGTH_SHORT
        if (!hasSelectedChip) {
            val toast = Toast.makeText(applicationContext, category, length)
            toast.show()
            return
        }
        if (hasError)
            return
        var selectedLabels = ArrayList<LabelData>()
        labelsGroup.checkedChipIds.forEach{
            selectedLabels.add(findViewById<Chip>(it).tag as LabelData)
        }
        intent.putExtra("JOURNAL_ENTRY", JournalDataEntry(title,notes,selectedLabels, -1))
        setResult(1, intent)
        finish()
    }

    private fun startCancelIntent() {
        var intent = Intent()
        intent.putExtra("JOURNAL_ENTRY", "test")
        setResult(0, intent)
        finish()
    }

    override fun onBackPressed() {
        startCancelIntent()
    }
}