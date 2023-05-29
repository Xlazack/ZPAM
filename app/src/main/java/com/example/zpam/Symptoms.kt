package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SymptomsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptomps_input)

        // Additional code for initializing UI elements or handling button clicks
    }
}

data class Symptoms(val firstVariable: String, val secondVariable: String)
