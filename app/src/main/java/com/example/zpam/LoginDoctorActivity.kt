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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginDoctorActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registerButton: Button
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var user: FirebaseUser
    private var isDoctor: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_doctor)

        // Inicjalizacja obiektów widoku
        emailEditText = findViewById<EditText>(R.id.login_doctor_emailAddress_text)
        passwordEditText = findViewById<EditText>(R.id.login_doctor_password_text)
        loginButton = findViewById<Button>(R.id.login_doctor_login_button)
        registerButton = findViewById(R.id.login_doctor_register_button)

        // Inicjalizacja instancji Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseReference = FirebaseDatabase.getInstance().getReference("Users")
        showToast(firebaseAuth.toString())

        // Obsługa kliknięcia przycisku logowania
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            //showToast(email+password)
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
    private fun fetchUserDataFromFirebase(): Boolean? {
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val userId = user!!.uid
        val userReference = firebaseReference.child(userId).child("userData")
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.getValue(UserModel::class.java)
                    userData?.let { user ->
                        isDoctor = user.userIsDoctor
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Obsługa błędu pobierania danych z Firebase
            }
        })
        return isDoctor
    }

    private fun loginUser(email: String, password: String) {
        if (email == "" || password == "") {
            // Show an error message indicating that both email and password are required
            showToast("Please enter your email and password.")
            return
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Użytkownik został pomyślnie zalogowany
                    Log.d(TAG, "signInWithEmail:success")
                    showToast("Udane Logowanie")
                    // Przejście do HomeActivity
                    if (fetchUserDataFromFirebase() == true) {
                        showToast("Ping")
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (fetchUserDataFromFirebase() == false){
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // W przypadku błędu logowania
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    // Obsługa różnych błędów logowania

                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            // Nieprawidłowy adres e-mail lub konto nie istnieje
                            showToast("Nieprawidłowy adres e-mail lub konto nie istnieje")
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Nieprawidłowe hasło
                            showToast("Nieprawidłowe hasło")
                        }
                        else -> {
                            // Inny rodzaj błędu
                            showToast("Błąd przy logowaniu")
                        }
                    }
                }
            }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}