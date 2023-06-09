package com.example.zpam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.settings_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val userDataButton = findViewById<Button>(R.id.settings_userDataButton)
        userDataButton.setOnClickListener {
            val intent = Intent(this, UserDataActivity::class.java)
            startActivity(intent)
        }

        val chooseAddress = findViewById<Button>(R.id.chooseAddressButton)
        chooseAddress.setOnClickListener {
            val intent = Intent(this, ChooseAddressFromMapActivity::class.java)
            startActivity(intent)
        }
    }
}