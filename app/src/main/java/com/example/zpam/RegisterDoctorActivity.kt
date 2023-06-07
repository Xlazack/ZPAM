package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterDoctorActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_doctor)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById(R.id.register_doctor_emailAddress_text)
        passwordEditText = findViewById(R.id.register_doctor_Password_text)
        confirmPasswordEditText = findViewById(R.id.register_doctor_repeatPassword_text)
        registerButton = findViewById(R.id.register_doctor_registerButton)
        loginButton = findViewById(R.id.register_doctor_loginButton)

        // Inicjalizacja instancji Firebase Auth i Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore

        // Obsługa kliknięcia przycisku rejestracji
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            registerUser(email, password, confirmPassword)
        }

        // Obsługa kliknięcia przycisku Zaloguj
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginDoctorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String, confirmPassword: String) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter your email and password.")
            return
        }

        if (password != confirmPassword) {
            showToast("Password and confirm password do not match.")
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(null)
                        .build()

                    user?.updateProfile(userProfileChangeRequest)
                        ?.addOnCompleteListener { updateProfileTask ->
                            if (updateProfileTask.isSuccessful) {
                                val userId = user.uid
                                val userReference = firestore.collection("Doctors").document(userId).collection("userData").document("data")
                                val userData = UserModel(null, null, null, null, email, null, null, true).show()
                                userReference.set(userData)
                                    .addOnSuccessListener {
                                        showToast("User registered successfully.")
                                        val intent = Intent(this, LoginDoctorActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { exception ->
                                        showToast("Failed to register user.")
                                    }
                            } else {
                                showToast("Failed to update user profile.")
                            }
                        }
                } else {
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> {
                            showToast("An account with this email already exists.")
                        }
                        else -> {
                            showToast("Failed to register user.")
                        }
                    }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
