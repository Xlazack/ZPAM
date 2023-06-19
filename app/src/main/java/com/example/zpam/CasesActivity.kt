package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CasesActivity : AppCompatActivity() {

    private lateinit var doctorCasesRef: CollectionReference
    private lateinit var userFilesRef: CollectionReference
    private lateinit var symptoms: String
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var userFileId: String
    private lateinit var entryId: String
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)
        // Step 1: Fetch symptom entries from Firestore
        val firestore = Firebase.firestore
        auth = Firebase.auth
        userId = auth.currentUser!!.uid
        doctorCasesRef = firestore.collection("Doctors").document(userId).collection("cases")
        userFilesRef = firestore.collection("Users")

        doctorCasesRef.get()
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
            //Get symptoms category selected in entry
            userFileId = entry.getString("userId").toString()
            entryId = entry.getString("entryId").toString()


            userFilesRef.document(userFileId).collection("userData").document("data").get()
                .addOnSuccessListener() {documentSnapshot ->
                    userName = documentSnapshot.getString("userName")!! + " " + documentSnapshot.getString("userSurname")!!

                    userFilesRef.document(userFileId).collection("userFiles").document(entryId).get()
                        .addOnSuccessListener() {documentSnapshot ->
                            symptoms = documentSnapshot.getString("symptom")!!

                            // Once the data is retrieved, inflate the CardView
                            val cardView = layoutInflater.inflate(R.layout.card_symptom_entry, cardLayout, false) as CardView

                            // Populate the card view with the relevant data
                            val entryTextView: TextView = cardView.findViewById(R.id.entryTextView)
                            entryTextView.text = "Imię i Nazwisko: ${userName}"
                            val secondEntryTextView: TextView = cardView.findViewById(R.id.secondEntryTextView)
                            secondEntryTextView.text = symptoms

                            cardView.setOnClickListener {
                                val intent = Intent(this, SymptomEntryDetailsForDoctorActivity::class.java)
                                intent.putExtra("entryId", entryId)
                                intent.putExtra("symptom", symptoms)
                                intent.putExtra("userId", userFileId)
                                startActivity(intent)
                            }

                            cardLayout.addView(cardView)
                        }
                }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}