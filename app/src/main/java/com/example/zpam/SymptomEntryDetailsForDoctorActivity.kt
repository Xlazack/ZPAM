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

class SymptomEntryDetailsForDoctorActivity : AppCompatActivity() {
    private lateinit var symptom: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_entry_doctors_details)

        val entryId = intent.getStringExtra("entryId")!!
        val symptomID = intent.getStringExtra("symptom")!!
        val userID = intent.getStringExtra("userId")!!

        // Step 1: Fetch symptom entry data from Firestore based on the entry ID
        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser!!.uid
        val userFilesRef = firestore.collection("Users").document(userID).collection("userFiles").document(entryId)

        userFilesRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val symptomLayout: LinearLayout = findViewById(R.id.symptomLayout)
                symptom = documentSnapshot.getString("symptom").toString()
                val doctorId = documentSnapshot.getString("chosenDoctor")

                if (doctorId != null) {
                    firestore.collection("Doctors").document(doctorId).collection("userData").document("data").get()
                        .addOnSuccessListener { doctorDocument ->
                            val userName = doctorDocument.getString("userName") + " " + doctorDocument.getString("userSurname") + "\nEmail: " + doctorDocument.getString("userEmail") + "\nNumer Telefonu: " + doctorDocument.getString("userPhone")
                            val userTextView = TextView(this)
                            userTextView.text = "Imię i nazwisko: $userName"
                            userTextView.textSize = 18f
                            userTextView.setPadding(16)
                            symptomLayout.addView(userTextView)

                            /*val userEmail = doctorDocument.getString("userEmail")
                            val userEmailTextView = TextView(this)
                            userEmailTextView.text = "Imię i nazwisko: $userEmail"
                            userEmailTextView.textSize = 18f
                            userEmailTextView.setPadding(16)
                            symptomLayout.addView(userTextView)

                            val userPhone = doctorDocument.getString("userPhone")
                            val userPhoneTextView = TextView(this)
                            userPhoneTextView.text = "Imię i nazwisko: $userPhone"
                            userPhoneTextView.textSize = 18f
                            userPhoneTextView.setPadding(16)
                            symptomLayout.addView(userTextView)*/
                        }
                        .addOnFailureListener { exception ->
                            // Handle error fetching doctor data
                        }
                }

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
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
