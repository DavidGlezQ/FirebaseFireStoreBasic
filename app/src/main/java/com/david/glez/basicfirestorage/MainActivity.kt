package com.david.glez.basicfirestorage

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var firetore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        firetore = Firebase.firestore
        basicInsert()
    }

    private fun basicInsert() {
        val user = hashMapOf(
            "name" to "Alejandro",
            "age" to 25,
            "city" to "Guadalajara",
            "happy" to false
        )
        //firetore.collection("users").add(user)
        //firetore.collection("users").add(user).await()
        //firetore.collection("users").add(user).isSuccessful
        firetore.collection("users").add(user)
            .addOnSuccessListener { Log.i("Success", "Success") }
            .addOnFailureListener { Log.i("Error", "Error ${it.message}") }
    }
}