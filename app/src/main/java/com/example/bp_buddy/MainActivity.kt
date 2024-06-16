
package com.example.bp_buddy
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.bp_buddy.databinding.ActivityMainBinding
import com.example.bp_buddy.databinding.FragmentNotificationsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

/**
 * Główna aktywność aplikacji, odpowiedzialna za zarządzanie widokami fragmentów oraz
 * interakcje użytkownika.
 */


class MainActivity : AppCompatActivity() {
    /**
     * Metoda cyklu życia wywoływana podczas tworzenia aktywności.
     * Inicjalizuje widok, obsługuje logikę związana z zalogowanym użytkownikiem oraz dodaje
     * obsługę paska nawigacyjnego.
     *
     * @param savedInstanceState Zapisany stan aktywności, który może być użyty do przywrócenia
     *                           wcześniejszego stanu.
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        binding.createNotification.setOnClickListener { scheduleNotification() }

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


    private fun scheduleNotification() {

        val intent = Intent(applicationContext, Notification::class.java)
        val title = "BP Buddy czeka!"
        val message = "Pora na wprowadzenie kolejnego pomiaru."
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)

        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Tytuł: " + title +
                "Wiadomość: " + message +
                "Data i godzina: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()

    }

    private fun getTime(): Long {
        val binding = FragmentNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val timePicker24 = binding.timePicker
        timePicker24.setIs24HourView(true)
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {

        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Funkcja zamieniająca bieżący fragment na nowy fragment.
     *
     * @param fragment  Nowy fragment, który ma być wyświetlony.
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
