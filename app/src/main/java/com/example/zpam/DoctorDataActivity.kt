package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorDataActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var PESELEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var bioEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_data)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        val user = firebaseAuth.currentUser
        val userId = user!!.uid
        val userRef = firestore.collection("Doctors").document(userId).collection("userData").document("data")

        nameEditText = findViewById(R.id.userData_name)
        surnameEditText = findViewById(R.id.userData_surname)
        dateOfBirthEditText = findViewById(R.id.userData_dateOfBirth)
        PESELEditText = findViewById(R.id.userData_PESEL)
        emailEditText = findViewById(R.id.userData_email)
        phoneEditText = findViewById(R.id.userData_phone)
        addressEditText = findViewById(R.id.userData_address)
        bioEditText = findViewById(R.id.userData_Bio)

        fetchUserDataFromFirestore(userId, userRef, nameEditText, surnameEditText, dateOfBirthEditText, PESELEditText, emailEditText, phoneEditText, addressEditText, bioEditText)

        val backButton = findViewById<Button>(R.id.doctorData_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeDoctorActivity::class.java)
            startActivity(intent)
        }


        val saveUserDataButton = findViewById<Button>(R.id.doctorData_saveButton)
        saveUserDataButton.setOnClickListener {
            saveUserDataToFirestore(userRef, nameEditText, surnameEditText, dateOfBirthEditText, PESELEditText, emailEditText, phoneEditText, addressEditText, bioEditText)
        }
    }

    private fun fetchUserDataFromFirestore(
        userId: String,
        userRef: DocumentReference,
        name: EditText,
        surname: EditText,
        dateOfBirth: EditText,
        PESEL: EditText,
        email: EditText,
        phone: EditText,
        address: EditText,
        bio: EditText,
    ) {
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(DoctorModel::class.java)
                    userData?.let { user ->
                        name.setText(user.userName)
                        surname.setText(user.userSurname)
                        dateOfBirth.setText(user.userBirthDate)
                        PESEL.setText(user.userPesel)
                        email.setText(user.userEmail)
                        phone.setText(user.userPhone)
                        address.setText(user.userAddress)
                        bio.setText(user.userBio)
                        // Assign values to other fields accordingly
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle Firestore data fetching error
            }
    }

    private fun saveUserDataToFirestore(userRef: DocumentReference, name: EditText, surname: EditText, dateOfBirth: EditText, PESEL: EditText, email: EditText, phone: EditText, address: EditText, bio: EditText) {
        val nameValue = name.text.toString()
        val surnameValue = surname.text.toString()
        val dateOfBirthValue = dateOfBirth.text.toString()
        val PESELValue = PESEL.text.toString()
        val emailValue = email.text.toString()
        val phoneValue = phone.text.toString()
        val addressValue = address.text.toString()
        val bioValue = bio.text.toString()
        // Get values from other fields

        val userDataUpdates = hashMapOf<String, Any>(
            "userName" to nameValue,
            "userSurname" to surnameValue,
            "userBirthDate" to dateOfBirthValue,
            "userPesel" to PESELValue,
            "userEmail" to emailValue,
            "userPhone" to phoneValue,
            "userAddress" to addressValue,
            "userBio" to bioValue
        )

        userRef.update(userDataUpdates)
            .addOnSuccessListener {
                // Dane zaktualizowane pomyślnie
            }
            .addOnFailureListener { exception ->
                // Obsługa błędu aktualizacji danych w Firestore
            }
    }
}
