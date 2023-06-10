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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Search2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wybor_lekarza)

        val wybierzLekarza = findViewById<Button>(R.id.settings_backButton)
        wybierzLekarza.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)


            recyclerView = findViewById(R.id.recyclerView)
            adapter = UserAdapter()

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            // Inicjalizacja Firebase Firestore
            val db = FirebaseFirestore.getInstance()

            // Pobieranie danych użytkowników z Firestore
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    val userList = mutableListOf<User>()
                    for (document in result) {
                        val name = document.getString("name")
                        val specialization = document.getString("specialization")
                        val imageUrl = document.getString("imageUrl")
                        if (name != null && specialization != null && imageUrl != null) {
                            val user = User(name, specialization, imageUrl)
                            userList.add(user)
                        }
                    }
                    adapter.setUserList(userList)
                }
                .addOnFailureListener { exception ->
                    // Obsługa błędu
                }
        }
    }}
