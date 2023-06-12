//package com.example.zpam
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//class Search2 : AppCompatActivity(){
//
//    @SuppressLint("WrongViewCast")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_wybor_lekarza)
//
//        val wybierzLekarza = findViewById<Button>(R.id.view2)
//        wybierzLekarza.setOnClickListener {
//            val intent = Intent(this, SearchActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}


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
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wybor_lekarza)

        userList = mutableListOf<User>()

        val wybierzLekarza = findViewById<Button>(R.id.settings_backButton)
        wybierzLekarza.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerView)
        adapter = UserAdapter()

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
                            val userBio = document.getString("userBio")
                            if (name != null && surname != null && userBio != null) {
                                val user = User(name, surname, userBio)
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
