package com.team12.myopensecret

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var addEntryButton: FloatingActionButton
    @VisibleForTesting
    lateinit var entryList: LinearLayout

    companion object {
        lateinit var dataBase: DataBaseHelper
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsActivity.loadLocale(this)
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
                R.id.labels_button -> {
                        val intent = Intent(this, VIewTagActivity::class.java)
                        startActivity(intent)
                        true
                }
                R.id.data_fields_button -> {
                    val intent = Intent(this, DataFieldOverview::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings_button -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivityForResult(intent, 2)
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

        createDefaultLabels() // delete later if labels can be created
        loadJournals()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
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
        } else if (requestCode == 1 && resultCode == 1) {
            entryList.removeAllViews()
            entryList.invalidate()
            loadJournals()
        }
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
            val intent = Intent(this, ViewEntryActivity::class.java)
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

    private fun createDefaultLabels() {
        // create some labels
        if (dataBase.viewLabelEntries().isNotEmpty())
            return
        dataBase.addLabelEntry(LabelData("Diary","#FFEB3B", -1))
        dataBase.addLabelEntry(LabelData("Discovery","#F44336", -1))
        dataBase.addLabelEntry(LabelData("Top-Secret","#8BC34A", -1))
        dataBase.addLabelEntry(LabelData("Measurement","#03A9F4", -1))
        dataBase.addLabelEntry(LabelData("Log","#FFBB86FC", -1))
        dataBase.addLabelEntry(LabelData("Disaster","#FF03DAC5", -1))
    }

    private fun loadJournals() {
        val journals = dataBase.viewJournalEntries()
        journals.reversed()
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