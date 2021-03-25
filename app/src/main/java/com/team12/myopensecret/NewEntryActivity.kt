package com.team12.myopensecret

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewEntryActivity : AppCompatActivity() {

    private lateinit var titleField: EditText
    private lateinit var notesField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_journal)

        titleField = findViewById(R.id.title_field)
        notesField = findViewById(R.id.notes_field)
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
            titleField.error = "Need at least 5 Characters"
            hasError = true
        }
        if (notes.length < 20) {
            notesField.error = "Need at least 20 Characters"
            hasError = true
        }
        if (hasError)
            return
        
        intent.putExtra("JOURNAL_ENTRY", JournalDataEntry(title,notes,ArrayList(), -1))
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