package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Search2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter2
    private lateinit var userList: MutableList<Doctor>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wybor_lekarza)

        userList = mutableListOf<Doctor>()

        val backButton = findViewById<Button>(R.id.settings_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerView)
        adapter = UserAdapter2()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Inicjalizacja Firebase Firestore
        val db = Firebase.firestore

        //Pobieranie danych użytkowników z Firestore
        db.collection("Doctors")
            .get()
            .addOnSuccessListener { result ->
                for ((index, doctorID) in result.withIndex()) {
                    db.collection("Doctors").document(doctorID.id).collection("userData").document("data")
                        .get()
                        .addOnSuccessListener { document ->
                            val name = document.getString("userName")
                            val surname = document.getString("userSurname")
   //                         val userBio = document.getString("userBio")
                            val address = document.getString("userAddress")
                            val mail = document.getString("userEmail")
                            if (name != null && surname != null && address != null && mail != null) {
                                val user = Doctor(doctorID.id,name, surname, address, mail)
                                userList.add(user)
                            }
                            // jeśli to jest ostatni dokument, zaktualizuj adapter
                            if (index == result.size() - 1) {
                                adapter.setUserList(userList)
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Obsługa błędu
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Obsługa błędu
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
