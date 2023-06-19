package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class WybranyLekarzActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio_lekarza)

        val backButton = findViewById<Button>(R.id.settings_backButton4)
        backButton.setOnClickListener {
            val intent = Intent(this, Search2::class.java)
            startActivity(intent)
        }

        val db = Firebase.firestore
        db.collection("Doctors")
            .get()
            .addOnSuccessListener { result ->
                for ((index, doctorID) in result.withIndex()) {
                    db.collection("Doctors").document(doctorID.id).collection("userData").document("data")
                        .get()
                        .addOnSuccessListener { document ->
                            val bioText = document.getString("userBio")
                            findViewById<TextView>(R.id.editTextTextMultiLine).setText(bioText)

                        }
                    }
                }
            .addOnFailureListener { exception ->
                // Obsługa błędu, jeśli wystąpił problem z pobraniem danych
            }
    }

}
