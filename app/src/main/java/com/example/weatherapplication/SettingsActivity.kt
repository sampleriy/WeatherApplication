package com.example.weatherapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etName = findViewById<EditText>(R.id.etName)
        val spinnerLang = findViewById<Spinner>(R.id.spinnerLanguage)
        val spinnerFont = findViewById<Spinner>(R.id.spFont)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switchTheme)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val languages = arrayOf("English", "Hindi", "Kannada")
        spinnerLang.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        val fonts = arrayOf("Small", "Medium", "Large")
        spinnerFont.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fonts)

        btnSave.setOnClickListener {
            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show()
        }
    }
}