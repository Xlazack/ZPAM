package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeDoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_doctor)

        val qualificationsButton = findViewById<Button>(R.id.homeDoctor_symptomsButton)
        qualificationsButton.setOnClickListener {
            val intent = Intent(this, QualificationsActivity::class.java)
            startActivity(intent)
        }
    }
}