package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usernameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById(R.id.register_emailAddress_text)
        usernameEditText = findViewById(R.id.register_Name_text)
        passwordEditText = findViewById(R.id.register_Password_text)
        confirmPasswordEditText = findViewById(R.id.register_repeatPassword_text)
        registerButton = findViewById(R.id.register_registerButton)
        loginButton = findViewById(R.id.register_loginButton)


        // Inicjalizacja instancji Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Obsługa kliknięcia przycisku rejestracji
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            registerUser(email, username, password, confirmPassword)
        }

        // Obsługa kliknięcia przycisku Zaloguj
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, username: String, password: String, confirmPassword: String) {
        if (password == confirmPassword) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Użytkownik został pomyślnie zarejestrowany
                        Log.d(TAG, "createUserWithEmail:success")

                        // Tutaj możesz wykonać dowolne czynności po rejestracji, np. przejście do kolejnego Activity
                    } else {
                        // W przypadku błędu rejestracji
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                        // Obsługa różnych błędów rejestracji
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                // Konto o podanym adresie e-mail już istnieje
                                // Wyświetl odpowiednie komunikaty lub wykonaj odpowiednie działania
                            }
                            else -> {
                                // Inny rodzaj błędu
                                // Wyświetl odpowiednie komunikaty lub wykonaj odpowiednie działania
                            }
                        }
                    }
                }
        } else {
            // Hasło i potwierdzenie hasła nie pasują do siebie
            // Wyświetl odpowiednie komunikaty lub wykonaj odpowiednie działania
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}