package com.seds.smartparkingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
class AppSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)
        val btnBack = findViewById<View>(R.id.btnBack)
        btnBack?.setOnClickListener {
            finish()
        }
    }
}