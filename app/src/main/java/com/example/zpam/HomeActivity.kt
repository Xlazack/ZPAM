package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val settingsButton = findViewById<ImageButton>(R.id.home_settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val newFileButton = findViewById<ImageButton>(R.id.home_newInstanceButton)
        newFileButton.setOnClickListener {
            val intent = Intent(this, UpdatedNewFileActivity::class.java)
            startActivity(intent)
        }

        /*val newsearchButton = findViewById<ImageButton>(R.id.home_searchButton)
        newsearchButton.setOnClickListener {
            val intent = Intent(this, Symptoms::class.java)
            startActivity(intent)
        }*/

        val fileExploreButton = findViewById<ImageButton>(R.id.home_yourFilesButton)
        fileExploreButton.setOnClickListener {
            val intent = Intent(this, FilesActivity::class.java)
            startActivity(intent)
        }
    }
}