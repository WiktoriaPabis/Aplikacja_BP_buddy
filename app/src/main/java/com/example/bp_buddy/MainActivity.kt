package com.example.bp_buddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Główna aktywność aplikacji, pojawiająca się po poprawnym zalogowaniu.
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}