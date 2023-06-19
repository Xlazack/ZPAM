package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SymptomEntryDetailsActivity : AppCompatActivity() {
    private lateinit var symptom: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_entry_details)

        val entryId = intent.getStringExtra("entryId")!!
        val symptomID = intent.getStringExtra("symptom")!!
        val userID = intent.getStringExtra("userId")!!

        // Step 1: Fetch symptom entry data from Firestore based on the entry ID
        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser!!.uid
        val userFilesRef = firestore.collection("Users").document(userId).collection("userFiles").document(entryId)

        userFilesRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val symptomLayout: LinearLayout = findViewById(R.id.symptomLayout)
                symptom = documentSnapshot.getString("symptom").toString()
                val symptomTextView = TextView(this)
                symptomTextView.text = "Symptom: $symptom"
                symptomTextView.textSize = 18f
                symptomTextView.setPadding(16)
                symptomLayout.addView(symptomTextView)

                val options = documentSnapshot.get("options") as ArrayList<Map<String, Any>>
                options.forEach { optionDetails ->
                    val optionLayout = LinearLayout(this)
                    optionLayout.orientation = LinearLayout.HORIZONTAL
                    val nameTextView = TextView(this)
                    val optionTextView = TextView(this)
                    nameTextView.text = "${optionDetails["name"]} "
                    optionTextView.text = "${optionDetails["option"]}"
                    optionLayout.addView(nameTextView)
                    optionLayout.addView(optionTextView)
                    symptomLayout.addView(optionLayout)
                }
            }
            .addOnFailureListener { exception ->
                // Handle error fetching symptom entry data
            }

        val chooseDoctor = findViewById<Button>(R.id.symptomEntryDetails_chooseDoctorButton)
        chooseDoctor.setOnClickListener {
            val intent = Intent(this, FilteredSearchActivity::class.java)
            intent.putExtra("userId", userId) // przekazuje userId
            intent.putExtra("symptom", symptom)
            intent.putExtra("entryId", entryId) // przekazuje entryId
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
