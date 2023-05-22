package com.example.zpam

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.play.core.integrity.p
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.Date

class NewFileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var currentDate: LocalDate
    private lateinit var symptoms: Symptoms
    private lateinit var placeholderText1: EditText
    private lateinit var placeholderText2: EditText
    private var recordCount: Long? = null
    private lateinit var filesRef: DatabaseReference



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_file)

        // Inicjalizacja instancji Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseReference = FirebaseDatabase.getInstance().getReference("Users")
        currentDate = LocalDate.now()
        val user = firebaseAuth.currentUser
        val userId = user!!.uid
        filesRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId).child("Files")
        val placeholderText1 = findViewById<EditText>(R.id.ProblemTextView)
        val placeholderText2 = "Druga wartosc"


//        // Pobieranie informacji na temat ilości plików
//        filesRef.get().addOnSuccessListener { dataSnapshot ->
//            recordCount = dataSnapshot.childrenCount
//            // Use the count variable here
//        }.addOnFailureListener { error ->
//            // Handle error here
//        }

        val createButton = findViewById<Button>(R.id.newFile_createButton)
        createButton.setOnClickListener {
            symptoms = Symptoms(placeholderText1.text.toString(), placeholderText2)
            createNewFile()
        }


        val backButton = findViewById<Button>(R.id.newFile_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() //Zamienić gdy doda się wylogowywanie
        }
    }
    override fun onResume() {
        super.onResume()

        filesRef.get().addOnSuccessListener { dataSnapshot ->
            recordCount = dataSnapshot.childrenCount
            showToast(recordCount.toString())
            // Use the recordCount variable here
        }.addOnFailureListener { error ->
            // Handle error here
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun createNewFile(){
        val user = firebaseAuth.currentUser
        val userId = user!!.uid
        val filesReference = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)
            .child("Files")
        val variableName = "${recordCount.toString()}"
        filesReference.child("${recordCount.toString()}").setValue(symptoms)
    }
}






