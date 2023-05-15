package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById<EditText>(R.id.emailAddress_text)
        passwordEditText = findViewById<EditText>(R.id.password_text)
        loginButton = findViewById<Button>(R.id.main_login_button)
        registerButton = findViewById(R.id.register_button)

        // Inicjalizacja instancji Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Obsługa kliknięcia przycisku logowania
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            loginUser(email, password)
        }

        // Obsługa kliknięcia przycisku przejścia do rejestrowania
        registerButton.setOnClickListener {
            registerButton.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Użytkownik został pomyślnie zalogowany
                    Log.d(TAG, "signInWithEmail:success")

                    // Tutaj możesz wykonać dowolne czynności po zalogowaniu, np. przejście do kolejnego Activity
                } else {
                    // W przypadku błędu logowania
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    // Obsługa różnych błędów logowania
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            // Nieprawidłowy adres e-mail lub konto nie istnieje
                            // Wyświetl odpowiednie komunikaty lub wykonaj odpowiednie działania
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Nieprawidłowe hasło
                            // Wyświetl odpowiednie komunikaty lub wykonaj odpowiednie działania
                        }
                        else -> {
                            // Inny rodzaj błędu
                            // Wyświetl odpowiednie komunikaty lub wykonaj odpowiednie działania
                        }
                    }
                }
            }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}