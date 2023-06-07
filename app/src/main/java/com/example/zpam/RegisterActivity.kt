package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById(R.id.register_emailAddress_text)
        passwordEditText = findViewById(R.id.register_Password_text)
        confirmPasswordEditText = findViewById(R.id.register_repeatPassword_text)
        registerButton = findViewById(R.id.register_registerButton)
        loginButton = findViewById(R.id.register_loginButton)

        // Inicjalizacja instancji Firebase Auth i Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = Firebase.firestore

        // Obsługa kliknięcia przycisku rejestracji
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            registerUser(email, password, confirmPassword)
        }

        // Obsługa kliknięcia przycisku Zaloguj
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String, confirmPassword: String) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter your email and password.")
            return
        }
        if (password == confirmPassword) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Użytkownik został pomyślnie zarejestrowany
                        showToast("Użytkownik został pomyślnie zarejestrowany")
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = firebaseAuth.currentUser

                        // Ustawienie nazwy użytkownika w profilu
                        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(null)
                            .build()

                        user?.updateProfile(userProfileChangeRequest)
                            ?.addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    // Pomyślnie zaktualizowano nazwę użytkownika
                                    showToast("Pomyślnie zaktualizowano nazwę użytkownika")

                                    // Przekazanie danych do bazy danych Firestore
                                    val userId = user.uid
                                    val userReferenceDoc = firebaseFirestore.collection("Users").document(userId).collection("userData").document("data")

                                    //val user = UserModel(null, null, null, null, email, null, null, false)
                                    val user = UserModel(null, null, null, null, email, null, null, false).show()

                                    userReferenceDoc.set(user)
                                        .addOnSuccessListener {
                                            // Pomyślnie dodano dane do bazy danych
                                            showToast("Pomyślnie zaktualizowano dane w bazie danych")
                                        }
                                        .addOnFailureListener { exception ->
                                            // Wystąpił błąd podczas dodawania danych do bazy danych
                                            showToast("Wystąpił błąd podczas aktualizowania danych w bazie danych")
                                        }
                                } else {
                                    // Wystąpił błąd podczas aktualizowania nazwy użytkownika
                                    // Obsłuż błąd odpowiednio
                                }
                            }

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                        // Tutaj możesz wykonać dowolne czynności po rejestracji, np. przejście do kolejnego Activity
                    } else {
                        // W przypadku błędu rejestracji
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                        // Obsługa różnych błędów rejestracji
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                // Konto o podanym adresie e-mail już istnieje
                                showToast("Konto o podanym adresie e-mail już istnieje")
                            }
                            else -> {
                                // Inny rodzaj błędu
                                showToast("Coś poszło nie tak")
                            }
                        }
                    }
                }
        } else {
            // Hasło i potwierdzenie hasła nie pasują do siebie
            showToast("Potwierdzenie hasła nie zgadza się z hasłem")
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
