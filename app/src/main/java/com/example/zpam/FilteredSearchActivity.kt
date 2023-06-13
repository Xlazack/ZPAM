package com.example.zpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FilteredSearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wybor_lekarza_filtered)

        val entryId = intent.getStringExtra("entryId")!!
        val symptom = intent.getStringExtra("symptom")!!

        showToast(symptom)

        userList = mutableListOf<User>()

        val backButton = findViewById<Button>(R.id.settings_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerView)
        adapter = UserAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Inicjalizacja Firebase Firestore
        val db = Firebase.firestore

        // Pobieranie danych użytkowników z Firestore
        db.collection("Doctors")
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.map { doctorID ->
                    val userDataTask = db.collection("Doctors").document(doctorID.id).collection("userData").document("data")
                        .get()

                    val doctorTask = db.collection("Doctors").document(doctorID.id)
                        .get()

                    Tasks.whenAllComplete(userDataTask, doctorTask)
                        .continueWith { task ->
                            val userDocument = userDataTask.result
                            val doctorDocument = doctorTask.result

                            if (task.isSuccessful && userDocument != null && doctorDocument != null) {
                                val name = userDocument.getString("userName")
                                val surname = userDocument.getString("userSurname")
                                // val userBio = document.getString("userBio")
                                val address = userDocument.getString("userAddress")
                                val mail = userDocument.getString("userEmail")
                                val selectedOptions = doctorDocument.get("selectedOptions") as? List<String>
                                //showToast(selectedOptions.toString())

                                if (name != null && surname != null && address != null && mail != null && selectedOptions != null && selectedOptions.contains(symptom)) {
                                    showToast(selectedOptions.toString() + symptom)
                                    User(name, surname, address, mail)
                                } else {
                                    null
                                }
                            } else {
                                null
                            }
                        }
                }

                Tasks.whenAllSuccess<User>(tasks)
                    .addOnSuccessListener { users ->
                        adapter.setUserList(users.filterNotNull())
                    }
                    .addOnFailureListener { exception ->
                        // Obsługa błędu
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
