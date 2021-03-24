package com.team12.myopensecret

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class NewEntryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_journal)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_entry_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.accept_entry) {
            startSuccesfullIntent()
        } else if (item.itemId == R.id.decline_entry) {
           startCancelIntent()
        }
        else {
          false
        }

        return super.onOptionsItemSelected(item)
    }

    fun startSuccesfullIntent() {
        var intent = Intent()
        intent.putExtra("JOURNAL_ENTRY", JournalDataEntry("test","title",ArrayList()))
        setResult(1, intent)
        finish()
    }

    fun startCancelIntent() {
        var intent = Intent()
        intent.putExtra("JOURNAL_ENTRY", "test")
        setResult(0, intent)
        finish()
    }

    override fun onBackPressed() {
        startCancelIntent()
    }
}