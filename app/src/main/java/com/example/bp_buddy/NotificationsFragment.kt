package com.example.bp_buddy

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.bp_buddy.databinding.FragmentNotificationsBinding
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNotification.setOnClickListener {
            scheduleNotification()
        }
    }

    private fun scheduleNotification() {
        val title = "BP Buddy czeka!"
        val message = "Pora na wprowadzenie kolejnego pomiaru :)"
        val time = getTime()

        val intent = Intent(requireContext(), Notification::class.java) //require zamiast applictaion
                                                                    //bo jestesmy we fragmencie a nie w main
        intent.putExtra(titleExtra, title) //tytul powiadomienia
        intent.putExtra(messageExtra, message) //lucz dla tresci powiadomienia

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), // to samo co wyzej
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent) // tutaj dalam require
                                                                                // brakowalo tego bo to sluzy za powiadomienia

        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\n" +
                        "Message: $message\n" +
                        "At: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

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
