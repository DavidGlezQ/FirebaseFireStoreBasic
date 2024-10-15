package com.david.glez.basicfirestorage

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var firetore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        firetore = Firebase.firestore
        //basicInsert()
        //multipleInsert()
        //basicReadData()
        //basicReadDocument()
        //basicReadDocumentParse()
        //basicReadDocumentFromCache()
        //subCollection()
        //basicRealTime()
        //basicRealTimeCollection()
        //basicQuery()
        mediumQuery()
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

    private fun multipleInsert() {
        for (i in 0..50) {
            val user = hashMapOf(
                "name" to "Alejandro $i",
                "age" to i,
                "city" to "Guadalajara",
                "happy" to (i % 2 == 0)
            )
            firetore.collection("users").add(user)
        }
    }

    private fun basicReadData() {
        firetore.collection("users").get().addOnSuccessListener {
            it.forEach { document ->
                Log.i("Success Read: ${document.id}", document.data.toString())
            }
            Log.i("Success", "Success")
        }
    }

    private fun basicReadDocument() {
        lifecycleScope.launch {
            val result = firetore.collection("users").document("5SR9oqB2hs1aiVs9gqvP").get().await()
            Log.i("Success", "${result.id} ${result.data.toString()}")
        }
    }

    private fun basicReadDocumentParse() {
        lifecycleScope.launch {
            val result = firetore.collection("users").document("5SR9oqB2hs1aiVs9gqvP").get().await()
            val userData = result.toObject<User>()
            Log.i("Success", "${result.id} ${result.data.toString()}")
            Log.i("Success", "${result.id} $userData")
        }
    }

    private fun basicReadDocumentFromCache() {
        lifecycleScope.launch {
            val ref = firetore.collection("users").document("5SR9oqB2hs1aiVs9gqvP")
            val source = Source.CACHE
            val result = ref.get(source).await()
            Log.i("Success", "${result.id} ${result.data.toString()}")
        }
    }

    private fun subCollection() {
        val result = firetore.collection("users").document("favs").collection("test")
            .document("JToVcQdSXPMnBdzBxDzd")
        Log.i("Success", "${result.id} $result")
    }

    private fun basicRealTime() {
        firetore.collection("users").document("5SR9oqB2hs1aiVs9gqvP")
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                Log.i("Success realtime", documentSnapshot?.data.toString())
            }
    }

    private fun basicRealTimeCollection() {
        firetore.collection("users")
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                Log.i("Success realtime", documentSnapshot?.size().toString())
            }
    }

    private fun basicQuery() {
        val query =
            firetore.collection("users").orderBy("age", Query.Direction.DESCENDING).limit(10)
        query.get().addOnSuccessListener {
            it.forEach { result ->
                Log.i("Query", result.data.toString())
            }
        }
    }

    private fun mediumQuery() {
        val query =
            firetore.collection("users").orderBy("age", Query.Direction.DESCENDING).limit(10)
                .whereEqualTo("happy", true)
                .whereGreaterThan("age", 30)



        query.get().addOnSuccessListener {
            it.forEach { result ->
                Log.i("Query", result.data.toString())
            }
        }
    }
}

data class User(
    val name: String? = "",
    val age: Int? = 0,
    val city: String? = "",
    val happy: Boolean? = false
)