package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SymptomEntryDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_entry_details)

        val entryId = intent.getStringExtra("entryId")!!

        // Step 1: Fetch symptom entry data from Firestore based on the entry ID
        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser!!.uid
        val userFilesRef = firestore.collection("Users").document(userId).collection("userFiles").document(entryId)

        userFilesRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val entryData = documentSnapshot.data

                // Step 2: Display the symptom entry data in TextViews or other UI elements
                val entryTextView: TextView = findViewById(R.id.entryTextView)
                // Populate the entryTextView and other UI elements with the symptom entry data
                entryTextView.text = "Entry ID: $entryId\n${entryData.toString()}"
            }
            .addOnFailureListener { exception ->
                // Handle error fetching symptom entry data
            }

        val chooseDoctor = findViewById<Button>(R.id.symptomEntryDetails_chooseDoctorButton)
        chooseDoctor.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
