package com.team12.myopensecret

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup

class ViewEntryActivity: AppCompatActivity() {

    private lateinit var titleField:TextView
    private lateinit var descriptionField:TextView
    private lateinit var chipsField:ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entry)

        titleField = findViewById(R.id.title_view)
        descriptionField = findViewById(R.id.description_view)
        chipsField = findViewById(R.id.chipsView)
        var dataEntry = intent.extras?.getSerializable("JOURNAL_ENTRY") as JournalDataEntry
        titleField.text = dataEntry.title
        descriptionField.text = dataEntry.description
    }
}