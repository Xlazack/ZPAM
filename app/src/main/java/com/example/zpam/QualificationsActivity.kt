package com.example.zpam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import com.example.zpam.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QualificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qualifications)

        val db = Firebase.firestore
        val symptomsCollection = db.collection("Symptoms")

        val symptomOptionsLayout: LinearLayout = findViewById(R.id.qualifications_linearLayout)
        val scrollView: ScrollView = findViewById(R.id.qualifications_scrollView)


        symptomsCollection.get().addOnSuccessListener { querySnapshot ->
            val categories = mutableListOf<String>()
            for (document in querySnapshot.documents) {
                val categoryName = document.getString("name")
                if (categoryName != null) {
                    categories.add(categoryName)
                }
            }
            for (name in categories) {
                val symptomCheckBox = CheckBox(this)
                symptomCheckBox.text = name
                symptomOptionsLayout.addView(symptomCheckBox)
            }
        }
        /*db.collection("Symptoms")
            .get()
            .addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val symptomName = document.getString("name")
                if (symptomName != null) {
                    val symptomCheckBox = CheckBox(this)
                    symptomCheckBox.text = symptomName
                    symptomOptionsLayout.addView(symptomCheckBox)
                }
            }
        }*/
            .addOnFailureListener { exception ->
                // Obsłuż błąd pobierania dokumentów z kolekcji "Symptoms"
            }

        scrollView.addView(symptomOptionsLayout)
    }
}