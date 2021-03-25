package com.team12.myopensecret

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var addEntryButton: FloatingActionButton
    private lateinit var entryList: LinearLayout

    private lateinit var dataBase: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        addEntryButton = findViewById(R.id.add_entry_button)
        entryList = findViewById(R.id.entry_list)

        dataBase =  DataBaseHelper(this)

        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        actionBarToggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myProfile -> {
                    Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.people -> {
                    Toast.makeText(this, "People", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
        addEntryButton.setOnClickListener { clickedItem ->
            when (clickedItem.id) {
                R.id.add_entry_button -> {
                    val intent = Intent(this, NewEntryActivity::class.java)
                    startActivityForResult(intent, 0)
                    true
                }
                else -> {
                    false
                }
            }
        }
        loadJournals()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == 1) {
            if (data != null) {
                var dataEntry = data.extras?.getSerializable("JOURNAL_ENTRY") as JournalDataEntry
                val suc = dataBase.addJournalEntry(dataEntry)
                if (suc.toInt() == -1) {
                    Toast.makeText(this, "Failed Inserting into Database", Toast.LENGTH_SHORT).show()
                    return
                }
                addToJournalsList(dataEntry)
            }
        } else if (requestCode == 0 && resultCode == 0) {
            // new journal got canceled
        }
    }

    private fun addToJournalsList(data: JournalDataEntry) {
        var journalView: View = layoutInflater.inflate(R.layout.journal_entry, entryList, false)
        journalView.findViewById<TextView>(R.id.journal_title).text = (data.title)
        journalView.findViewById<TextView>(R.id.journal_description).text = (data.description)
        entryList.addView(journalView)
    }

    private fun loadJournals() {
        val journals = dataBase.viewJournalEntries()
        journals.forEach {
            addToJournalsList(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(navView)
        }
        return true
    }

    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}