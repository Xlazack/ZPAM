package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usernameEditText: EditText
    private lateinit var firebaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById(R.id.register_emailAddress_text)
        //usernameEditText = findViewById(R.id.register_Name_text)
        passwordEditText = findViewById(R.id.register_Password_text)
        confirmPasswordEditText = findViewById(R.id.register_repeatPassword_text)
        registerButton = findViewById(R.id.register_registerButton)
        loginButton = findViewById(R.id.register_loginButton)


        // Inicjalizacja instancji Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // Obsługa kliknięcia przycisku rejestracji
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            registerUser(email, null, password, confirmPassword)
        }

        // Obsługa kliknięcia przycisku Zaloguj
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, username: String?, password: String, confirmPassword: String) {
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
                            .setDisplayName(username)
                            .build()

                        user?.updateProfile(userProfileChangeRequest)
                            ?.addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    // Pomyślnie zaktualizowano nazwę użytkownika
                                    // Możesz wykonać dodatkowe czynności po rejestracji, takie jak utworzenie pozycji w bazie danych itp.
                                    showToast("Pomyślnie zaktualizowano nazwę użytkownika")

                                    // Przekazanie danych do bazy danych Firebase
                                    val userId = user.uid
                                    showToast(userId)
                                    val userReference = FirebaseDatabase.getInstance().reference
                                        .child("Users")
                                        .child(userId)
                                    val user = UserModel(null, null, null, null, email, null)
                                    firebaseReference.child(userId).setValue(user)
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