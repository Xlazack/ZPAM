package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginDoctorActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var rbutton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var registerButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var user: FirebaseUser
    private var isDoctor: Boolean? = null
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_doctor)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById<EditText>(R.id.login_doctor_emailAddress_text)
        passwordEditText = findViewById<EditText>(R.id.login_doctor_password_text)
        loginButton = findViewById<Button>(R.id.login_doctor_login_button)
        registerButton = findViewById(R.id.login_doctor_register_button)
        rbutton = findViewById(R.id.button2)

        // Inicjalizacja instancji Firebase Auth i Firestore
        auth = Firebase.auth
        firestore = Firebase.firestore


        // Obsługa kliknięcia przycisku logowania
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(email, password)
        }

        // Obsługa kliknięcia przycisku przejścia do rejestrowania
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        rbutton.setOnClickListener {
            val intent = Intent(this, SymptomsAddingActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Powrót do okna main dla użytkownika
        val notADoctorButton = findViewById<Button>(R.id.loginDoctor_notDoctorButton)
        notADoctorButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchUserDataFromFirestore() {
        val userReference = firestore.collection("Doctors").document(userId).collection("userData").document("data")
        userReference.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userData = document.toObject(UserModel::class.java)
                    userData?.let { user ->
                        isDoctor = user.userIsDoctor
                        showToast(isDoctor.toString())
                        navigateToHomeActivity()
                    }
                } else {
                    // Dokument nie istnieje
                }
            }
            .addOnFailureListener { exception ->
                // Obsługa błędu pobierania danych z Firestore
            }
    }

    private fun loginUser(email: String, password: String) {
        if (email == "" || password == "") {
            showToast("Please enter your email and password.")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Użytkownik został pomyślnie zalogowany
                    Log.d(TAG, "signInWithEmail:success")
                    userId = auth.currentUser!!.uid
                    showToast("Zalogowany")
                    fetchUserDataFromFirestore()
                } else {
                    // W przypadku błędu logowania
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    // Obsługa różnych błędów logowania
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            showToast("Nieprawidłowy adres e-mail lub konto nie istnieje")
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            showToast("Nieprawidłowe hasło")
                        }
                        else -> {
                            showToast("Błąd przy logowaniu")
                        }
                    }
                }
            }
    }

    private fun navigateToHomeActivity() {
        if (isDoctor == true) {
            val intent = Intent(this, HomeDoctorActivity::class.java)
            startActivity(intent)
        } else if (isDoctor == false) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


