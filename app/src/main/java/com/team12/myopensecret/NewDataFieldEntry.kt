package com.team12.myopensecret

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip

class NewDataFieldEntry : AppCompatActivity() {

    private lateinit var titleField: EditText
    private lateinit var spinner: Spinner
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_data_field)

        titleField = findViewById(R.id.data_field_name)
        checkBox = findViewById(R.id.df_checkbox)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        spinner = findViewById(R.id.spinner2)
        ArrayAdapter.createFromResource(
                this,
                R.array.types_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
        var hasError = false
        if (title.length < 5) {
            titleField.error = resources.getString(R.string.need5chars)
            hasError = true
        }
        if (title.length > 20) {
            titleField.error = resources.getString(R.string.toomany20)
            hasError = true
        }
        if (hasError)
            return
        intent.putExtra("DATA_FIELD_ENTRY", DataFieldData(checkBox.isChecked.toString(), title, spinner.selectedItemPosition.toString(), null, null, -1))
        setResult(10, intent)
        finish()
    }

    private fun startCancelIntent() {
        var intent = Intent()
        intent.putExtra("DATA_FIELD_ENTRY", "test")
        setResult(0, intent)
        finish()
    }
}