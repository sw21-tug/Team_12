package com.team12.myopensecret

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SettingsActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //loadLocale()
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        onBackPressed()
        return true
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