package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var firebaseReference: DatabaseReference


class SymptomsAddingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms_adding)

        val db = Firebase.firestore

        // Tworzenie dokumentu "Abdominal pain in adults"
        val abdominalPainDoc = db.collection("Symptoms").document("Abdominal pain in adults")

        val abdominalPainData = hashMapOf(
            "name" to "Abdominal pain in adults",
            "children" to listOf(
                hashMapOf(
                    "name" to "Pain is",
                    "options" to listOf("Burning", "Cramping", "Dull")
                ),
                hashMapOf(
                    "name" to "Pain located in",
                    "options" to listOf(
                        "Lower abdomen",
                        "Middle abdomen",
                        "Upper abdomen",
                        "Pain is radiating to the whole body"
                    )
                ),
                hashMapOf(
                    "name" to "Triggered or worsened by",
                    "options" to listOf("Drinking alcohol", "Coughing or other jarring movements")
                )
            )
        )
        val coughInAdultsDoc = db.collection("Symptoms").document("Cough in adults")

        val coughInAdultsData = hashMapOf(
            "name" to "Cough in adults",
            "children" to listOf(
                hashMapOf(
                    "name" to "Cough is",
                    "options" to listOf("Dry", "Wet")
                ),
                hashMapOf(
                    "name" to "Problem is",
                    "options" to listOf("Ongoing", "Recent", "Worsening")
                )
            )
        )

        val eyeProblemsInAdultsDoc = db.collection("Symptoms").document("Eye problems in adults")

        val eyeProblemsInAdultsData = hashMapOf(
            "name" to "Eye problems in adults",
            "children" to listOf(
                hashMapOf(
                    "name" to "Problem is",
                    "options" to listOf("Blurry distant objects", "Blurry nearby objects", "Blurry or blind spot in center of vision", "Blurry vision at all distances", "Bright zigzag lines", "Clouded, hazy or dim vision","Double vision", "Fading of colors", "Flashes of light","Glare with bright lights", "Halos around lights", "Inability to distinguish certain shades of color","Loss of side vision", "Objects appear crooked or distorted", "Poor night vision", "Progressive expansion of shadow or curtain over visual field","Seeing nonexistent things, or hallucinating","Sensitivity to light","Shimmering spots or stars","Spots or strings floating in field of vision","Tunnel vision","Vision loss, partial or total")
                ),
                hashMapOf(
                    "name" to "Problem affects",
                    "options" to listOf("Both eyes", "One eye")
                ),
                hashMapOf(
                    "name" to "Vision improves somewhat with",
                    "options" to listOf("Holding objects away from face", "Holding objects close to face", "Squinting", "Use of bright lighting")
                ),
                hashMapOf(
                    "name" to "Onset is",
                    "options" to listOf("Gradual or progressive", "Recent (hours to days)", "Sudden (seconds to minutes)")
                ),
                hashMapOf(
                    "name" to "Duration is",
                    "options" to listOf("A few minutes", "Usually no longer than 30 minutes", "Constant")
                ),
                hashMapOf(
                    "name" to "Accompanied by",
                    "options" to listOf("Confusion or trouble talking", "Dizziness or difficulty walking", "Eye pain or discomfort", "Eye redness", "Eyestrain", "Headache", "Numbness or weakness on one side of the body", "Other sensory disturbances", "Swelling around the eye")
                )
            )
        )

        val footPainOrAnklePainInAdultsDoc = db.collection("Symptoms").document("Foot pain or ankle pain in adults")

        val footPainOrAnklePainInAdultsData = hashMapOf(
            "name" to "Foot pain or ankle pain in adults",
            "children" to listOf(
                hashMapOf(
                    "name" to "Located in",
                    "options" to listOf("Ankle", "Area along edge of toenail", "Back of ankle", "Bottom of foot", "Heel", "Middle part of foot", "Toe or front part of foot", "Whole foot")
                ),
                hashMapOf(
                    "name" to "Triggered or worsened by",
                    "options" to listOf("Activity or overuse", "Ill-fitting shoes", "Injury", "Long periods of rest")
                ),
                hashMapOf(
                    "name" to "Accompanied by",
                    "options" to listOf("Black-and-blue mark", "Burning", "Difficulty pushing off with toes", "Feeling of instability", "Flattened arch", "Inability to bear weight", "Joint deformity", "Numbness or tingling", "Redness", "Stiffness", "Swelling", "Thickened or rough skin", "Weakness")
                )
            )
        )


        val powrot = findViewById<Button>(R.id.button4)
        powrot.setOnClickListener {
            val intent = Intent(this, LoginDoctorActivity::class.java)
            startActivity(intent)
            finish()
        }

        val upload = findViewById<Button>(R.id.button3)
        upload.setOnClickListener {
            abdominalPainDoc.set(abdominalPainData)
                .addOnSuccessListener {
                    // Sukces - dokument został utworzony lub zaktualizowany
                }
                .addOnFailureListener { e ->
                    // Błąd - nie udało się utworzyć lub zaktualizować dokumentu
                }
            coughInAdultsDoc.set(coughInAdultsData)
                .addOnSuccessListener {
                    // Sukces - dokument został utworzony lub zaktualizowany
                }
                .addOnFailureListener { e ->
                    // Błąd - nie udało się utworzyć lub zaktualizować dokumentu
                }
            eyeProblemsInAdultsDoc.set(eyeProblemsInAdultsData)
            footPainOrAnklePainInAdultsDoc.set(footPainOrAnklePainInAdultsData)
        }
    }
}

        /*val firestore = FirebaseFirestore.getInstance()

        firebaseReference = FirebaseDatabase.getInstance().getReference("Symptoms")

        val powrot = findViewById<Button>(R.id.button3)
        powrot.setOnClickListener {
            val intent = Intent(this, LoginDoctorActivity::class.java)
            startActivity(intent)
            finish()
        }

        val symptomsCollection = firestore.collection("Symptoms")

// Abdominal pain in adults
        val abdominalPainDocRef = symptomsCollection.document("Abdominal pain in adults")
        abdominalPainDocRef.set(
            mapOf(
                "Pain is" to mapOf(
                    "1" to "Burning",
                    "2" to "Cramping",
                    "3" to "Dull"
                ),
                "Pain located in" to mapOf(
                    "1" to "Lower abdomen",
                    "2" to "Middle abdomen",
                    "3" to "Upper abdomen",
                    "4" to "Pain is radiating to the whole body"
                ),
                "Triggered or worsen by" to mapOf(
                    "1" to "Drinking alcohol",
                    "2" to "Coughing or other jarring movements"
                )
            )
        )

// Cough in adults
        val coughDocRef = symptomsCollection.document("Cough in adults")
        coughDocRef.set(
            mapOf(
                "Cough is" to mapOf(
                    "1" to "Wet",
                    "2" to "Dry"
                ),
                "Problem is" to mapOf(
                    "1" to "Recent",
                    "2" to "Ongoing",
                    "3" to "Worsening"
                )
            )
        )

        val upload = findViewById<Button>(R.id.button4)
        upload.setOnClickListener {
            symptomsCollection
                .add(abdominalPainDocRef)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }*/

        /*upload.setOnClickListener {
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
        }*/
/*
    }
}*/
