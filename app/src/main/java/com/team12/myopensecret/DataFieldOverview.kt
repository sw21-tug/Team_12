package com.team12.myopensecret

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

class DataFieldOverview : AppCompatActivity() {

    private lateinit var dataList:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_field_overview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dataList = findViewById(R.id.data_list)
        MainActivity.dataBase.viewDataFieldEntries().forEach{ df ->
            addDF(df)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_data_field_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_field_button) {
            val intent = Intent(this, NewDataFieldEntry::class.java)
            startActivityForResult(intent, 20)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
        if (requestCode == 20 && resultCode == 10) {
            if (data != null) {
                var dfEntry = data.extras?.getSerializable("DATA_FIELD_ENTRY") as DataFieldData
                val suc = MainActivity.dataBase.addDataField(dfEntry)
                if (suc.toInt() == -1) {
                    Toast.makeText(this, "Failed Inserting into Database", Toast.LENGTH_SHORT).show()
                    return
                }
                addDF(dfEntry)
            }
        } else if (requestCode == 0 && resultCode == 0) {
            // new journal got canceled
        }
    }

    private fun addDF(data: DataFieldData) {
        var dfLayout: View = layoutInflater.inflate(R.layout.data_field_layout, dataList, false)
        dfLayout.findViewById<TextView>(R.id.df_name).text = (data.name)
        dfLayout.findViewById<TextView>(R.id.df_type).text = when(data.type) {
            "0" -> resources.getStringArray(R.array.types_array)[0]
            "1" -> resources.getStringArray(R.array.types_array)[1]
            else -> resources.getStringArray(R.array.types_array)[2]
        }
        dfLayout.findViewById<ImageView>(R.id.df_image).setOnClickListener {
            // TODO: EDIT
        }
        dataList.addView(dfLayout, 0)
    }
}