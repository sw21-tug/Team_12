package com.team12.myopensecret

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
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
    private lateinit var labelsGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsActivity.loadLocale(this)
        setContentView(R.layout.activity_edit_entry)

        setTitle(R.string.edit_entry)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_entry_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.accept_entry) {
            startSuccessfulIntent()
        } else if (item.itemId == R.id. delete_entry) {
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


}