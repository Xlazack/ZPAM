package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WybranyLekarzActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio_lekarza)

        val backButton = findViewById<Button>(R.id.settings_backButton4)
        backButton.setOnClickListener {
            val intent = Intent(this, Search2::class.java)
            startActivity(intent)
        }
    }
    }
