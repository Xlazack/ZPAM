package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import com.example.zpam.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var userId: String
private lateinit var db: FirebaseFirestore

class QualificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qualifications)

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = Firebase.firestore
        val symptomsCollection = db.collection("Symptoms")

        val symptomOptionsLayout: LinearLayout = findViewById(R.id.qualifications_linearLayout)
        val scrollView: ScrollView = findViewById(R.id.qualifications_scrollView)

        val saveButton = findViewById<Button>(R.id.qualifications_saveButton)
        saveButton.setOnClickListener {
            saveCheckedOptionsToFirestore()
        }

        val backButton = findViewById<Button>(R.id.qualifications_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeDoctorActivity::class.java)
            startActivity(intent)
            finish() //Zamienić gdy doda się wylogowywanie
        }

        symptomsCollection.get().addOnSuccessListener { querySnapshot ->
            val categories = mutableListOf<String>()
            for (document in querySnapshot.documents) {
                val categoryName = document.getString("name")
                if (categoryName != null) {
                    categories.add(categoryName)
                }
            }
            showToast(categories.toString())
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

        //symptomOptionsLayout.addView(symptomOptionsLayout)


    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchUserDataFromFirestore(userId: String, userRef: DocumentReference, name: EditText, surname: EditText, dateOfBirth: EditText, PESEL: EditText, email: EditText, phone: EditText, address: EditText) {
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(UserModel::class.java)
                    userData?.let { user ->
                        name.setText(user.userName)
                        surname.setText(user.userSurname)
                        dateOfBirth.setText(user.userBirthDate)
                        PESEL.setText(user.userPesel)
                        email.setText(user.userEmail)
                        phone.setText(user.userPhone)
                        address.setText(user.userAddress)
                        // Assign values to other fields accordingly
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle Firestore data fetching error
            }
    }

    fun saveCheckedOptionsToFirestore() {
        val selectedOptions = mutableListOf<String>()


        // Kontener zawierający checkboxy
        val symptomOptionsLayout = findViewById<LinearLayout>(R.id.qualifications_linearLayout)
        // Iteruj przez wszystkie checkboxy
        for (i in 0 until symptomOptionsLayout.childCount) {
            val checkbox = symptomOptionsLayout.getChildAt(i) as? CheckBox
            if (checkbox != null && checkbox.isChecked) {
                // Jeśli checkbox jest zaznaczony, dodaj jego wartość do listy zaznaczonych opcji
                selectedOptions.add(checkbox.text.toString())
            }
        }

        // Twórz obiekt, który będzie zapisany w Firestore
        val dataToSave = hashMapOf(
            "selectedOptions" to selectedOptions
        )

        // Zapisz dane do Firestore
        db.collection("Doctors").document(userId).set(dataToSave)
            .addOnSuccessListener {
                // Zapis powiódł się
            }
            .addOnFailureListener { error ->
                // Obsłuż błąd
            }
    }
}
