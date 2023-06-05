package com.example.zpam

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.logging.Logger.global

data class SymptomOption(
    val name: String,
    val option: String
)

data class SymptomData(
    val userId: String,
    val symptom: String,
    val options: List<SymptomOption>
)

private lateinit var firebaseReference: DatabaseReference
private lateinit var filesRef: DatabaseReference
private lateinit var filesReference: CollectionReference
private lateinit var db: FirebaseFirestore
private lateinit var userId: String
private lateinit var fileId: String
class UpdatedNewFileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updated_new_file)

        db = Firebase.firestore

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseReference = FirebaseDatabase.getInstance().getReference("Users")
        filesRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId).child("Files")
        filesReference = db.collection("Users").document(userId).collection("userFiles")


// Referencja do kolekcji "Symptoms"
        val symptomsCollection = db.collection("Symptoms")

// Inicjalizacja głównego spinnera
        val mainSpinner: Spinner = findViewById(R.id.mainSpinner)

// Inicjalizacja kontenera dla spinnerów opcji symptomów
        val childSpinnersContainer: LinearLayout = findViewById(R.id.childSpinnersContainer)

// Pobierz kategorie symptomów z Firestore i wypełnij główny spinner
        symptomsCollection.get().addOnSuccessListener { querySnapshot ->
            val categories = mutableListOf<String>()
            for (document in querySnapshot.documents) {
                val categoryName = document.getString("name")
                if (categoryName != null) {
                    categories.add(categoryName)
                }
            }

            val mainSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
            mainSpinner.adapter = mainSpinnerAdapter
        }

// Obsługa zdarzenia wyboru kategorii w głównym spinnerze
        mainSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position) as String

                // Pobierz dane dla wybranej kategorii z Firestore
                symptomsCollection.document(selectedCategory).get().addOnSuccessListener { documentSnapshot ->
                    val children = documentSnapshot.get("children") as? List<HashMap<String, Any>>

                    // Wyczyszczenie kontenera z poprzednich spinnerów
                    childSpinnersContainer.removeAllViews()

                    // Tworzenie spinnerów dla opcji symptomów
                    children?.forEach { child ->
                        val childName = child["name"] as? String
                        val options = child["options"] as? List<String>

                        if (childName != null && options != null) {
                            // Tworzenie opisu nad spinnerem
                            val labelTextView = TextView(this@UpdatedNewFileActivity)
                            labelTextView.text = childName

                            // Tworzenie spinnera z opcjami
                            val childSpinner = Spinner(this@UpdatedNewFileActivity)
                            val childSpinnerAdapter = ArrayAdapter(this@UpdatedNewFileActivity, android.R.layout.simple_spinner_item, options)
                            childSpinner.adapter = childSpinnerAdapter

                            // Dodawanie opisu i spinnera do kontenera
                            childSpinnersContainer.addView(labelTextView)
                            childSpinnersContainer.addView(childSpinner)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nie wykonujemy żadnych działań, gdy nie wybrano żadnej kategorii
            }
        }

        val backButton = findViewById<Button>(R.id.updatedNewFile_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() //Zamienić gdy doda się wylogowywanie
        }

        val saveButton = findViewById<Button>(R.id.updatedNewFile_saveButton)
        saveButton.setOnClickListener {
            pushDataToFirebase()
        }


    }

    /*override fun onResume() {
        super.onResume()

        filesReference.get().addOnSuccessListener { dataSnapshot ->
            fileId = dataSnapshot.childrenCount.toString()
            //showToast(recordCount.toString())
            // Use the recordCount variable here
            showToast(fileId)
        }.addOnFailureListener { error ->
            // Handle error here
        }
    }*/
    override fun onResume() {
        super.onResume()

        val userFilesCollection = Firebase.firestore.collection("Users").document(userId).collection("userFiles")
        userFilesCollection
            .get()
            .addOnSuccessListener { querySnapshot ->
                fileId = querySnapshot.documents.size.toString()
                showToast(fileId)
            }
            .addOnFailureListener { error ->
                // Handle error here
            }
    }

    @SuppressLint("WrongViewCast")
    fun getSelectedOptions(): List<SymptomOption> {
        val selectedOptions = mutableListOf<SymptomOption>()
        val optionContainer = findViewById<LinearLayout>(R.id.childSpinnersContainer)
        showToast(optionContainer.childCount.toString())
        var optionName = ""

        for (i in 0 until optionContainer.childCount) {
            val child = optionContainer.getChildAt(i)
            if (child is TextView) {
                val optionTextView = child as TextView
                optionName = optionTextView.text.toString()
            }
            if (child is Spinner) {
                val optionSpinner = child as Spinner
                val selectedOption = optionSpinner.selectedItem.toString()
                val symptomOption = SymptomOption(optionName, selectedOption)
                selectedOptions.add(symptomOption)
            }
        }

        return selectedOptions
    }

    fun pushDataToFirebase(){

        val mainSpinner = findViewById<Spinner>(R.id.mainSpinner)
        val selectedSymptom = mainSpinner.selectedItem.toString()
        showToast(selectedSymptom)
        // Pobranie wybranych opcji
        val selectedOptions = getSelectedOptions()
        // Tworzenie obiektu SymptomData
        val symptomData = SymptomData(userId, selectedSymptom, selectedOptions)
        showToast(symptomData.toString())
        // Zapis obiektu SymptomData do bazy danych
        filesReference.document(fileId).set(symptomData)
            .addOnSuccessListener {
                // Zapis powiódł się
                // Wykonaj odpowiednie działania po zapisie danych
                showToast("Hurrah")
            }
            .addOnFailureListener { error ->
                // Wystąpił błąd podczas zapisywania danych
                // Obsłuż błąd
                showToast("Booo")
            }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}