package com.example.zpam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChooseAddressFromMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var saveButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userRef: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_address_from_map)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        val user = firebaseAuth.currentUser
        val userId = user!!.uid
        userRef = firestore.collection("Users").document(userId).collection("userData").document("data")

        saveButton = findViewById(R.id.chooseAddress_saveButton)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val backButton = findViewById<Button>(R.id.chooseAddress_backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Dodaj znacznik we Wrocławiu i przesuń tam kamerę
        val wroclaw = LatLng(51.1078852, 17.0385376)
        mMap.addMarker(MarkerOptions().position(wroclaw).title("Marker in Wrocław"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wroclaw))

        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title("Selected location"))
            saveButton.setOnClickListener {
                // Zapisz 'latLng' do Firebase
                val latitude = latLng.latitude
                val longitude = latLng.longitude

                // Pobieranie aktualnych danych użytkownika
                userRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.toObject(UserModel::class.java)
                        userData?.let { user ->
                            // Tworzenie obiektu z aktualizacjami danych
                            val userDataUpdates = hashMapOf<String, Any>(
                                "userName" to user.userName.toString(),
                                "userSurname" to user.userSurname.toString(),
                                "userBirthDate" to user.userBirthDate.toString(),
                                "userPesel" to user.userPesel.toString(),
                                "userEmail" to user.userEmail.toString(),
                                "userPhone" to user.userPhone.toString(),
                                "userAddress" to user.userAddress.toString(),
                                "userLatitude" to latitude,
                                "userLongitude" to longitude
                            )

                            // Aktualizowanie danych użytkownika w Firebase
                            userRef.update(userDataUpdates).addOnSuccessListener {
                                // Dane zaktualizowane pomyślnie
                            }.addOnFailureListener { exception ->
                                // Obsługa błędu aktualizacji danych w Firestore
                            }
                        }
                    }
                }
            }
        }
    }
}
