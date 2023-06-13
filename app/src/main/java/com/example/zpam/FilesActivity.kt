package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FilesActivity : AppCompatActivity() {

    private lateinit var userFilesRef: CollectionReference
    private lateinit var symptoms: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)
        // Step 1: Fetch symptom entries from Firestore
        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser!!.uid
        userFilesRef = firestore.collection("Users").document(userId).collection("userFiles")

        userFilesRef.get()
            .addOnSuccessListener { querySnapshot ->
                val entries = mutableListOf<DocumentSnapshot>()
                for (document in querySnapshot.documents) {
                    entries.add(document)
                }
                createSymptomEntryCards(entries)
            }
            .addOnFailureListener { exception ->
                // Handle error fetching symptom entries
            }


    }
    // Step 2: Create symptom entry cards dynamically
    private fun createSymptomEntryCards(entries: List<DocumentSnapshot>) {
        val cardLayout: LinearLayout = findViewById(R.id.cardLayout)

        for (entry in entries) {
            val cardView = layoutInflater.inflate(R.layout.card_symptom_entry, cardLayout, false) as CardView

            // Set data on the card view
            val entryData = entry.data
            val entryId = entry.id // Use the entry ID to pass to the next activity
            //Get symptoms category selected in entry
            userFilesRef.document(entry.id).get()
                .addOnSuccessListener { document ->
                    symptoms = document.getString("symptom").toString()
                    //showToast(symptoms)
                }
            // Populate the card view with the relevant data
            val entryTextView: TextView = cardView.findViewById(R.id.entryTextView)
            entryTextView.text = "Entry ID: $entryId"

            cardView.setOnClickListener {
                val intent = Intent(this, SymptomEntryDetailsActivity::class.java)
                intent.putExtra("entryId", entryId)
                intent.putExtra("symptoms", symptoms)
                startActivity(intent)
            }

            cardLayout.addView(cardView)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}