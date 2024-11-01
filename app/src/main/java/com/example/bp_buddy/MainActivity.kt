
package com.example.bp_buddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * Główna aktywność aplikacji, odpowiedzialna za zarządzanie widokami fragmentów oraz
 * interakcje użytkownika.
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

    /**
     * Metoda do zamiany obecnego fragmentu na nowy fragment.
     *
     * @param fragment Nowy fragment, który ma być wyświetlony.
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

