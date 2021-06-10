package com.team12.myopensecret

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class VIewTagActivity : AppCompatActivity() {

    private lateinit var tagsList:LinearLayout
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_overview)
        drawerLayout = findViewById(R.id.drawerLayout2)
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        navView = findViewById(R.id.navView2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.labels_button -> {
                    this.drawerLayout.closeDrawer(GravityCompat.START)
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

        tagsList = findViewById(R.id.tags_list)
        MainActivity.dataBase.viewLabelEntries().forEach{ label ->
            addLabel(label)
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

    private fun addLabel(data: LabelData) {
        var tagLayout: View = layoutInflater.inflate(R.layout.tag_layout, tagsList, false)
        tagLayout.findViewById<TextView>(R.id.tag_name).text = (data.name)
        tagLayout.findViewById<ImageView>(R.id.tag_image).setOnClickListener {
            // TODO: EDIT
        }
        tagLayout.findViewById<TextView>(R.id.tag_color).background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(data.color), BlendModeCompat.SRC_ATOP)
        tagsList.addView(tagLayout, 0)
    }
}