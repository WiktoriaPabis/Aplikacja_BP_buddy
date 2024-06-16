package com.example.bp_buddy

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.bp_buddy.databinding.ActivityMainBinding
import com.example.bp_buddy.databinding.FragmentNotificationsBinding
import java.util.Calendar
import java.util.Date

class NotificationsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        val timePicker24 : TimePicker = view.findViewById(R.id.timePicker)
        timePicker24.setIs24HourView(true)
        return view
    }
}