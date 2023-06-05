package com.example.zpam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class QualificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qualifications)

        val db = FirebaseFirestore.getInstance()
        val symptomsCollection = db.collection("Symptoms")

        val symptomOptionsLayout: LinearLayout = findViewById(R.id.qualifications_linearLayout)
        val scrollView: ScrollView = findViewById(R.id.qualifications_scrollView)

        symptomsCollection
            .get()
            .addOnSuccessListener { result ->
            val categories = mutableListOf<String>()
            for (document in result) {
                val categoryName = document.getString("name")
                if (categoryName != null) {
                    categories.add(categoryName)
                }
            }

            for (category in categories) {
                val categoryTextView = TextView(this)
                categoryTextView.text = category
                symptomOptionsLayout.addView(categoryTextView)

                val optionsRef = symptomsCollection.document(category).collection("children")
                optionsRef
                    .get()
                    .addOnSuccessListener { childrenSnapshot ->
                        val optionsLayout = LinearLayout(this)
                        optionsLayout.orientation = LinearLayout.VERTICAL
                        val options = mutableListOf<String>()
                        for (optionDocument in childrenSnapshot) {
                            val problem = optionDocument.getString("name")

                            if (problem != null) {
                                val optionCheckBox = TextView(this)
                                optionCheckBox.text = "\t" + problem
                                optionsLayout.addView(optionCheckBox)
                            }

                            //val option = optionDocument.get
                        }

                        symptomOptionsLayout.addView(optionsLayout)
                    }
                    .addOnFailureListener { exception ->
                        // Obsłuż błąd pobierania opcji objawów
                    }
            }

            scrollView.addView(symptomOptionsLayout)
        }
            .addOnFailureListener { exception ->
                // Obsłuż błąd pobierania kategorii objawów
            }
    }
}
