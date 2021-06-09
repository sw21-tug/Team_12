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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AlertDialog

class DataFieldOverview : AppCompatActivity() {

    private lateinit var dataList:LinearLayout
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    //private lateinit var model: DataFieldData



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_field_overview)

        setTitle(R.string.data_fields)
        drawerLayout = findViewById(R.id.drawerLayout3)
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        navView = findViewById(R.id.navView3)
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
                    this.drawerLayout.closeDrawer(GravityCompat.START)
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
        dfLayout.findViewById<ImageView>(R.id.delete_df).setOnClickListener {
           //else if (item.itemId == R.id.delete_entry) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.title_delete))
            builder.setMessage(resources.getString(R.string.df_delete))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
               MainActivity.dataBase.deleteDataField(data)
                recreate()
               // val intent = Intent(this, DataFieldOverview::class.java)
                //startActivityForResult(intent, 1)
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }

            builder.show()
       // }
        }
        dataList.addView(dfLayout, 0)
    }

}
