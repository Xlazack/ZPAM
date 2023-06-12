package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainDoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_doctor)

        val loginButton = findViewById<Button>(R.id.main_doctor_login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginDoctorActivity::class.java)
            startActivity(intent)
            finish() //Zamienić gdy doda się wylogowywanie
        }

        val mainRegisterButton = findViewById<Button>(R.id.main_doctor_register_button)
        mainRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterDoctorActivity::class.java)
            startActivity(intent)
            finish()
        }

        val imADoctorButton = findViewById<Button>(R.id.main_doctor_isDoctorButton)
        imADoctorButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}