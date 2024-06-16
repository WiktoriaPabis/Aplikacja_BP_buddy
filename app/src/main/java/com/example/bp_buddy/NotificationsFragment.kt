package com.example.bp_buddy

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bp_buddy.databinding.FragmentNotificationsBinding
import java.util.*

/**
 * Fragment odpowiedzialny za obsługę planowania powiadomień.
 */
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    /**
     * Metoda cyklu życia fragmentu, odpowiadająca za tworzenie widoku fragmentu.
     *
     * @param inflater           Inflater do nadmuchiwania widoku fragmentu.
     * @param container          Kontener widoku fragmentu.
     * @param savedInstanceState Zapisany stan fragmentu, jeśli istnieje.
     * @return Widok fragmentu.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.timePicker.setIs24HourView(true)
        return binding.root
    }

    /**
     * Metoda cyklu życia fragmentu, wywoływana po utworzeniu widoku fragmentu.
     *
     * @param view               Widok fragmentu.
     * @param savedInstanceState Zapisany stan fragmentu, jeśli istnieje.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dodanie nasłuchiwania dla przycisku tworzenia powiadomienia
        binding.createNotification.setOnClickListener {
            scheduleNotification()
        }
    }

    /**
     * Metoda do planowania powiadomienia na określoną godzinę.
     */
    private fun scheduleNotification() {
        val title = "BP Buddy czeka!"
        val message = "Pora na wprowadzenie kolejnego pomiaru :)"
        val time = getTime()

        // Tworzenie intencji dla nadawania powiadomień
        val intent = Intent(requireContext(), Notification::class.java) //require zamiast applictaion
                                                                    //bo jestesmy we fragmencie a nie w main
        intent.putExtra(titleExtra, title) //tytul powiadomienia
        intent.putExtra(messageExtra, message) //lucz dla tresci powiadomienia

        // Tworzenie PendingIntent dla nadawania powiadomień
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), // to samo co wyzej
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Uzyskanie AlarmManagera do zarządzania alarmami
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent) // tutaj dalam require
                                                                                // brakowalo tego bo to sluzy za powiadomienia
        // Wywołanie metody wyświetlenia alertu potwierdzającego zaplanowanie powiadomienia
        showAlert(time, title, message)
    }

    /**
     * Metoda do wyświetlenia alertu informującego o zaplanowaniu powiadomienia.
     *
     * @param time    Czas, na jaki zaplanowano powiadomienie.
     * @param title   Tytuł powiadomienia.
     * @param message Treść powiadomienia.
     */
    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireContext())

        // Wyświetlenie alertu dialogowego z informacją o zaplanowanym powiadomieniu
        AlertDialog.Builder(requireContext())
            .setTitle("Powiadomienie zaplanowane")
            .setMessage(
                "Tytuł: $title\n" +
                        "Wiadomość: $message\n" +
                        "Data i godzina: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Potwierdź") { _, _ -> }
            .show()
    }

    /**
     * Metoda do uzyskania czasu wybranego przez użytkownika z DatePicker i TimePicker.
     *
     * @return Czas w formacie millis od epoch.
     */
    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute
        )
        return calendar.timeInMillis
    }

}
