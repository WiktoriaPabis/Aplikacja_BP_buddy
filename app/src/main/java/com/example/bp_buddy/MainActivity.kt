//package com.example.bp_buddy
//
package com.example.bp_buddy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.material.bottomnavigation.BottomNavigationView
//
///**
// * Główna aktywność aplikacji, pojawiająca się po poprawnym zalogowaniu.
// */
//class MainActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        var auth = FirebaseAuth.getInstance()
//
//        // Inicjalizacja przycisku do dodawania pomiarów
//        val addMeasurementButton: Button = findViewById(R.id.addMeasurementButton)
//        addMeasurementButton.setOnClickListener {
//            val dialog = AddMeasurementDialogFragment()
//            dialog.show(supportFragmentManager, "AddMeasurementDialogFragment")
//        }
//
//        // Sprawdzenie, czy użytkownik jest zalogowany
//        val currentUser = auth.currentUser
//
//        if (currentUser != null) {
//            // Pobranie adresu e-mail zalogowanego użytkownika
//            val email = currentUser.email
//            if (email != null) {
//
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, HistoryFragment())
//                    .commit()
//
//
////
////                addMeasurement(email.toString())
////                getAllMeasurements(email.toString())
//
//
//
//            }
//
//        }
//
//
//    }





/**
 * Główna aktywność aplikacji, pojawiająca się po poprawnym zalogowaniu.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val auth = FirebaseAuth.getInstance()

        // Inicjalizacja przycisku do dodawania pomiarów
        val addMeasurementButton: Button = findViewById(R.id.addMeasurementButton)
        addMeasurementButton.setOnClickListener {
            val dialog = AddMeasurementDialogFragment()
            dialog.show(supportFragmentManager, "AddMeasurementDialogFragment")
        }

        // Sprawdzenie, czy użytkownik jest zalogowany
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Pobranie adresu e-mail zalogowanego użytkownika
            val email = currentUser.email
            if (email != null) {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HistoryFragment())
                    .commit()

            }
        }

        // Dodanie obsługi paska nawigacyjnego
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.history -> {
                    replaceFragment(HistoryFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.graph -> {
                    replaceFragment(GraphFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.notifications -> {
                    replaceFragment(NotificationsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

    private fun addMeasurement(email: String) {
        val db = Firebase.firestore
        val userCollection = db.collection(email)

        // Pobranie aktualnej liczby pomiarów dla użytkownika
        userCollection.get().addOnSuccessListener { documents ->
            val nextMeasurementNumber = documents.size() + 1
            val measurementId = "Pomiar $nextMeasurementNumber"

            // Utworzenie nowego pomiaru
            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dateTime = dateFormat.format(Date(currentTime))

            val measurement = hashMapOf(
                "Data i czas" to dateTime,
                "Ciśnienie rozkurczowe" to 120,
                "Ciśnienie skurczowe" to 80,
                "Tętno" to 60
            )

            // Dodanie nowego dokumentu z danymi pomiarowymi do kolekcji użytkownika
            userCollection.document(measurementId)
                .set(measurement)
                .addOnSuccessListener {
                    // Pomyślnie dodano dokument
                    println("Dokument pomiarów został pomyślnie dodany jako $measurementId.")
                }
                .addOnFailureListener { e ->
                    // Wystąpienie błędu podczas dodawania dokumentu
                    println("Wystąpił błąd podczas dodawania dokumentu pomiarów: $e")
                }
        }.addOnFailureListener { e ->
            println("Wystąpił błąd podczas pobierania dokumentów: $e")
        }
    }

    private fun getAllMeasurements(email: String) {
        val db = Firebase.firestore
        val userCollection = db.collection(email)

        userCollection.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val measurementData = document.data
                    println("Dane pomiaru: $measurementData")




                }
            }
            .addOnFailureListener { e ->
                println("Wystąpił błąd podczas pobierania dokumentów: $e")
            }
    }


//}

