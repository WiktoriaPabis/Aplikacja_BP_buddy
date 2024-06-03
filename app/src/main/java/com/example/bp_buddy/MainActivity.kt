package com.example.bp_buddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Główna aktywność aplikacji, pojawiająca się po poprawnym zalogowaniu.
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var auth = FirebaseAuth.getInstance()

        // Sprawdź, czy użytkownik jest zalogowany
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Pobierz adres e-mail zalogowanego użytkownika
            val email = currentUser.email
            if (email != null) {
                // Wywołaj metodę addEmptyMeasurementCollection, przekazując adres e-mail użytkownika
//                addEmptyMeasurementCollection(email.toString())
                addMeasurement(email.toString())

            }

        }

    }
    private fun addEmptyMeasurementCollection(email: String) {
        val db = Firebase.firestore

        // Utwórz referencję do kolekcji "email" w bazie danych
        val emailCollection = db.collection(email)


        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateTime = dateFormat.format(Date(currentTime))

        // Utwórz nowy dokument dla pomiarów użytkownika
        val measurementsData = hashMapOf(
            "Data i czas" to dateTime,
            "Ciśnienie rozkurczowe" to "",
            "Ciśnienie skurczowe" to "",
            "Ciśnienie tętno" to ""
        )


        // Dodaj nowy dokument z danymi pomiarowymi
//        emailCollection.document("Pomiary")
            emailCollection.add(measurementsData)
//            .set(measurementsData)
            .addOnSuccessListener {
                // Powiadomienie o sukcesie
                println("Dokument pomiarów został pomyślnie dodany.")
            }
            .addOnFailureListener { e ->
                // Obsługa błędu
                println("Wystąpił błąd podczas dodawania dokumentu pomiarów: $e")
            }
    }

    private fun addMeasurement(email: String) {
        val db = Firebase.firestore
        val userCollection = db.collection(email)

        // Pobierz aktualną liczbę pomiarów dla użytkownika
        userCollection.get().addOnSuccessListener { documents ->
            val nextMeasurementNumber = documents.size() + 1
            val measurementId = "Pomiar $nextMeasurementNumber"

            // Utwórz nowy pomiar
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dateTime = dateFormat.format(Date(currentTime))

            val measurement = hashMapOf(
                "Data i czas" to dateTime,
                "Ciśnienie rozkurczowe" to 120,
                "Ciśnienie skurczowe" to 80,
                "Tętno" to 60
            )

            // Dodaj nowy dokument z danymi pomiarowymi do kolekcji użytkownika
            userCollection.document(measurementId)
                .set(measurement)
                .addOnSuccessListener {
                    // Pomyślnie dodano dokument
                    println("Dokument pomiarów został pomyślnie dodany jako $measurementId.")
                }
                .addOnFailureListener { e ->
                    // Wystąpił błąd podczas dodawania dokumentu
                    println("Wystąpił błąd podczas dodawania dokumentu pomiarów: $e")
                }
        }.addOnFailureListener { e ->
            println("Wystąpił błąd podczas pobierania dokumentów: $e")
        }
    }
}

