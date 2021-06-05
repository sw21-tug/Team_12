package com.team12.myopensecret

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*


class EditEntryActivity : AppCompatActivity() {
    private lateinit var model: JournalDataEntry
    private lateinit var titleField: EditText
    private lateinit var notesField: EditText
    //private lateinit var labelsGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsActivity.loadLocale(this)
        setContentView(R.layout.activity_edit_entry)
        titleField = findViewById(R.id.edit_title)
        notesField = findViewById(R.id.notes_field)
        val dataEntry = intent.extras?.getSerializable("data") as JournalDataEntry
        titleField.setText(dataEntry.title)
        notesField.setText(dataEntry.description)
        setTitle(R.string.edit_entry)
        model = dataEntry
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_entry_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.accept_entry) {
            startSuccessfulIntent()
        } else if (item.itemId == R.id.delete_entry) {
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
        }
        //TODO: Else for cancel

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
        /*
        FIXME: labelsGroup.checkedChipIds.forEach{
             hasSelectedChip = true
         }*/
        if (hasError) {
            return
        }

        /* val selectedLabels = ArrayList<LabelData>()
         labelsGroup.checkedChipIds.forEach{
             selectedLabels.add(findViewById<Chip>(it).tag as LabelData)
         } */
        val selectedLabels = ArrayList<LabelData>()
        intent.putExtra("JOURNAL_ENTRY", JournalDataEntry(title, notes, selectedLabels, model.dbId))
        setResult(1, intent)
        finish()
    }


}