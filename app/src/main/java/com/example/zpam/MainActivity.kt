package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.main_login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity3::class.java)
            startActivity(intent)
            finish() //Zamienić gdy doda się wylogowywanie
        }

        val mainRegisterButton = findViewById<Button>(R.id.main_register_button)
        mainRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val imADoctorButton = findViewById<Button>(R.id.main_isDoctorButton)
        imADoctorButton.setOnClickListener {
            val intent = Intent(this, MainDoctorActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}