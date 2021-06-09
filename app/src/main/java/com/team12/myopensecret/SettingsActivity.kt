package com.team12.myopensecret

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.util.*

class SettingsActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //loadLocale()
        setContentView(R.layout.activity_settings)

        drawerLayout = findViewById(R.id.drawerLayout4)
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        navView = findViewById(R.id.navView4)
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
                    this.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    false
                }
            }
        }
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when(pos) {
            0 -> setLocale("en", this)
            1 -> setLocale("ru", this)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
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

    companion object {
        private fun setLocale(language:String, activity: Activity) {
            val local = Locale(language)
            Locale.setDefault(local)
            var config = activity.resources.configuration
            config.setLocale(local)
            activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
            //activity.createConfigurationContext(config)
            val s = activity.getSharedPreferences("Settings", MODE_PRIVATE).edit()
            s.putString("My_Lang", language)
            s.apply()
        }

        fun loadLocale(activity: Activity) {
            val prefs = activity.getSharedPreferences("Settings", Activity.MODE_PRIVATE)
            val language = prefs.getString("My_Lang", "en")!!
            setLocale(language, activity)
        }
    }
}