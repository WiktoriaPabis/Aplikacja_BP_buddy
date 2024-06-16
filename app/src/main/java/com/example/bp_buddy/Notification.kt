package com.example.bp_buddy

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

/**
 * Klasa odbiornika powiadomień, odpowiedzialna za obsługę odebrania powiadomienia
 * i wyświetlenie go w systemie Android.
 */
const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification: BroadcastReceiver() {

    /**
     * Metoda wywoływana podczas odbioru powiadomienia.
     *
     * @param context Kontekst aplikacji.
     * @param intent  Intencja, która może zawierać dodatkowe dane powiadomienia.
     */
    override fun onReceive(context: Context?, intent: Intent?) {

        // Utworzenie powiadomienia za pomocą NotificationCompat.Builder
        val notification = context?.let {
            NotificationCompat.Builder(it, channelID)
                .setSmallIcon(R.drawable.logo) // Ikona powiadomienia
                .setContentTitle(intent?.getStringExtra(titleExtra)) // Tytuł powiadomienia
                .setContentText(intent?.getStringExtra(messageExtra)) // Treść powiadomienia
                .build()
        }

        // Wyświetlenie powiadomienia za pomocą NotificationManager
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.notify(notificationID, notification)
    }
}

