package com.example.zpam

import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.Reference

private lateinit var firebaseReference: DatabaseReference

class SymptomsAddingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms_adding)

        firebaseReference = FirebaseDatabase.getInstance().getReference("Symptoms")

        val powrot = findViewById<Button>(R.id.button3)
        powrot.setOnClickListener {
            val intent = Intent(this, LoginDoctorActivity::class.java)
            startActivity(intent)
            finish()
        }

        val upload = findViewById<Button>(R.id.button4)
        upload.setOnClickListener {
            firebaseReference.child("Abdominal pain in adults").child("Pain is").child("Burning").setValue("0111")
            firebaseReference.child("Abdominal pain in adults").child("Pain is").child("Cramping").setValue("0112")
            firebaseReference.child("Abdominal pain in adults").child("Pain is").child("Dull").setValue("0113")
            firebaseReference.child("Abdominal pain in adults").child("Pain located in").child("Abdomen but radiates to other parts of the body").setValue("0121")
            firebaseReference.child("Abdominal pain in adults").child("Pain located in").child("Lower abdomen").setValue("0122")
            firebaseReference.child("Abdominal pain in adults").child("Pain located in").child("Middle abdomen").setValue("0123")
            firebaseReference.child("Abdominal pain in adults").child("Pain located in").child("Upper abdomen").setValue("0124")
            firebaseReference.child("Abdominal pain in adults").child("Triggered or worsened by").child("Coughing or other jarring movements").setValue("0131")
            firebaseReference.child("Abdominal pain in adults").child("Triggered or worsened by").child("Drinking alcohol").setValue("0132")
            firebaseReference.child("Cough in adults").child("Cough is").child("Dry").setValue("0211")
            firebaseReference.child("Cough in adults").child("Cough is").child("Wet").setValue("0212")
            firebaseReference.child("Cough in adults").child("Problem is").child("Recent").setValue("0221")
            firebaseReference.child("Cough in adults").child("Problem is").child("Ongoing").setValue("0222")
            firebaseReference.child("Cough in adults").child("Problem is").child("Worsening").setValue("0223")
        }
    }
}