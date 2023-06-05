package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var firebaseReference: DatabaseReference

class SymptomsAddingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms_adding)

        firebaseReference = FirebaseDatabase.getInstance().getReference("Symptoms")

        val powrot = findViewById<Button>(R.id.button3)
        powrot.setOnClickListener {
            val intent = Intent(this, LoginDoctorActivity3::class.java)
            startActivity(intent)
            finish()
        }

        val upload = findViewById<Button>(R.id.button4)
        upload.setOnClickListener {
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_is").child("Burning").setValue("0111")
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_is").child("Cramping").setValue("0112")
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_is").child("Dull").setValue("0113")
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_located_in").child("Abdomen_but_radiates_to_other_parts_of_the_body").setValue("0121")
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_located_in").child("Lower_abdomen").setValue("0122")
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_located_in").child("Middle_abdomen").setValue("0123")
            firebaseReference.child("Abdominal_pain_in_adults").child("Pain_located_in").child("Upper_abdomen").setValue("0124")
            firebaseReference.child("Abdominal_pain_in_adults").child("Triggered_or_worsened_by").child("Coughing_or_other_jarring_movements").setValue("0131")
            firebaseReference.child("Abdominal_pain_in_adults").child("Triggered_or_worsened_by").child("Drinking_alcohol").setValue("0132")
            firebaseReference.child("Cough_in_adults").child("Cough_is").child("Dry").setValue("0211")
            firebaseReference.child("Cough_in_adults").child("Cough_is").child("Wet").setValue("0212")
            firebaseReference.child("Cough_in_adults").child("Problem_is").child("Recent").setValue("0221")
            firebaseReference.child("Cough_in_adults").child("Problem_is").child("Ongoing").setValue("0222")
            firebaseReference.child("Cough_in_adults").child("Problem_is").child("Worsening").setValue("0223")
        }
    }
}