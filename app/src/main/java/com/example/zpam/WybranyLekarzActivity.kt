package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        val userId = intent.getStringExtra("userId")!!
        val entryId = intent.getStringExtra("entryId")!!
        val doctorId = intent.getStringExtra("doctorId")!!

        val backButton = findViewById<Button>(R.id.settings_backButton4)
        backButton.setOnClickListener {
            val intent = Intent(this, Search2::class.java)
            startActivity(intent)
            finish()
        }

        val db = Firebase.firestore
        db.collection("Doctors").document(doctorId).collection("userData").document("data")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    val userName = documentSnapshot.getString("userName")
                    val userSurname = documentSnapshot.getString("userSurname")
                    val userBio = documentSnapshot.getString("userBio")

                    if (userName != null && userSurname != null && userBio != null) {
                        val fullNameTextView = findViewById<TextView>(R.id.Imie_i_nazwisko_1)
                        val bioTextView = findViewById<TextView>(R.id.editTextTextMultiLine)

                        fullNameTextView.text = "$userName $userSurname"
                        bioTextView.text = userBio
                    } else {
                        Log.d("Firestore", "One or more fields are missing")
                    }
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "Error getting documents: ", exception)
            }

    }

}
