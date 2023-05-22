package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.logging.Logger.global

class UserDataActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var PESELEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val userId = user!!.uid
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        nameEditText = findViewById(R.id.userData_name)
        surnameEditText = findViewById(R.id.userData_surname)
        dateOfBirthEditText = findViewById(R.id.userData_dateOfBirth)
        PESELEditText = findViewById(R.id.userData_PESEL)
        emailEditText = findViewById(R.id.userData_email)
        phoneEditText = findViewById(R.id.userData_phone)
        addressEditText = findViewById(R.id.userData_address)

        fetchUserDataFromFirebase(userId, userRef, nameEditText, surnameEditText, dateOfBirthEditText, PESELEditText, emailEditText, phoneEditText, addressEditText)

        val backButton = findViewById<Button>(R.id.userData_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        val saveUserDataButton = findViewById<Button>(R.id.userData_saveButton)
        saveUserDataButton.setOnClickListener {
            saveUserDataToFirebase(userRef, nameEditText, surnameEditText, dateOfBirthEditText, PESELEditText, emailEditText, phoneEditText, addressEditText)
        }
    }
}

private fun fetchUserDataFromFirebase(userId:String, userRef: DatabaseReference, name: EditText, surname: EditText, dateOfBirth: EditText, PESEL: EditText, email: EditText, phone: EditText, address: EditText) {
    val userReference = FirebaseDatabase.getInstance().reference
        .child("Users")
        .child(userId)

    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val userData = dataSnapshot.getValue(UserModel::class.java)
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

        override fun onCancelled(databaseError: DatabaseError) {
            // Obsługa błędu pobierania danych z Firebase
        }
    })
}
private fun saveUserDataToFirebase(userRef: DatabaseReference, name: EditText, surname: EditText, dateOfBirth: EditText, PESEL: EditText, email: EditText, phone: EditText, address: EditText) {

    val name = name.text.toString()
    val surname = surname.text.toString()
    val dateOfBirth = dateOfBirth.text.toString()
    val PESEL = PESEL.text.toString()
    val email = email.text.toString()
    val phone = phone.text.toString()
    val address = address.text.toString()
    // Get values from other fields

    val userData = UserModel(name, surname, dateOfBirth, PESEL, email, phone, address/* other values */)
    userRef.setValue(userData)
        .addOnSuccessListener {
            // Data saved successfully
        }
        .addOnFailureListener { exception ->
            // Handle Firebase data saving error
        }
}