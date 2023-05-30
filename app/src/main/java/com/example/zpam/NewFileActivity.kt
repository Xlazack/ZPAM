package com.example.zpam

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
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
    private lateinit var areaSpinner: Spinner



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
        areaSpinner = findViewById(R.id.new_file_areaSpinner)


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

        val symptomsRef = FirebaseDatabase.getInstance().getReference("Symptoms")

// Step 1: Retrieve the records from the "Symptoms" category
        symptomsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val symptomNames = mutableListOf<String>()

                // Step 2: Create a list to hold the symptom names

                // Step 3: Iterate through the retrieved records and extract the symptom names
                for (symptomSnapshot in dataSnapshot.children) {
                    val symptomName = symptomSnapshot.key
                    symptomName?.let {
                        symptomNames.add(it)
                    }
                }

                // Step 4: Initialize an ArrayAdapter with the symptom names
                val adapter = ArrayAdapter(this@NewFileActivity, android.R.layout.simple_spinner_item, symptomNames)

                // Step 5: Set the ArrayAdapter as the adapter for your spinner
                areaSpinner.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if the operation was cancelled or failed
                println("Error: ${databaseError.message}")
            }
        })




// Step 2: Add an OnItemSelectedListener to the first spinner
        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Step 3: Retrieve the selected symptom
                val selectedSymptom = parent?.getItemAtPosition(position) as String

                // Step 4: Query the database to get the children of the selected symptom
                val databaseReference = FirebaseDatabase.getInstance().getReference("Symptoms")
                val childrenPaths: List<String> = retrieveChildrenPathsFromDatabase(databaseReference, selectedSymptom)


                // Step 5: Create spinners dynamically based on the number of children
                createDynamicSpinners(childrenPaths)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }
    }

    // Step 4: Query the database to get the children of the selected symptom
    fun retrieveChildrenPathsFromDatabase(databaseReference: DatabaseReference, selectedSymptom: String): List<String> {
        val childrenPaths: MutableList<String> = mutableListOf()
        val symptomReference = databaseReference.child(selectedSymptom)

       /* symptomReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)*/ symptomReference.get().addOnSuccessListener { dataSnapshot ->
                for (childSnapshot in dataSnapshot.children) {
                    val childPath = childSnapshot.key ?: ""
                    childrenPaths.add(childPath)
                }

                // Step 5: Create spinners dynamically based on the number of children
                createDynamicSpinners(childrenPaths)
            }

            /*override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if the operation was cancelled or failed
                println("Error: ${databaseError.message}")
            }
        })*/

        return childrenPaths
    }

    // Step 6: Populate each dynamically created spinner with the paths of the children
    fun retrievePathsFromDatabase(path: String): List<String> {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Symptoms/$path")
        val paths: MutableList<String> = mutableListOf()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val childPath = childSnapshot.key as String
                    paths.add(childPath)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if the operation was cancelled or failed
                println("Error: ${databaseError.message}")
            }
        })

        return paths
    }

    // Step 6: Populate each dynamically created spinner with the paths of the children
    fun createDynamicSpinners(childrenPaths: List<String>) {
        val layoutInflater = LayoutInflater.from(this)
        val layout = findViewById<LinearLayout>(R.id.spinnerContainer)
        // Remove existing dynamic spinners if any
        layout.removeAllViews()

        for (path in childrenPaths) {
            val spinner = Spinner(this)
            val paths: List<String> = retrievePathsFromDatabase(path)

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paths)
            spinner.adapter = adapter

            // Add the dynamically created spinner to the layout
            layout.addView(spinner)
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






