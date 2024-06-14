package com.example.bp_buddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar

class MeasurementInfoDialogFragment : DialogFragment(), View.OnClickListener {

    private var inputDiastolic1: EditText?
    private var previousFragment: DialogFragment? = null
    private var confirmButton1: Button?


    init {
        inputDiastolic1 = null
        confirmButton1 = null

    }

    fun setPreviousFragment(fragment: DialogFragment) {
        previousFragment = fragment
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_measurement_info, container, false)

        // Inicjalizacja pól wejściowych i przycisków
        inputDiastolic1 = view.findViewById(R.id.inputDiastolic1)
        confirmButton1 = view.findViewById(R.id.confirmButton1)


        // Ustawienie nasłuchiwania kliknięć przycisków
        confirmButton1?.setOnClickListener(this)



        return view
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.confirmButton1 -> {
                previousFragment?.dismiss() // zamknięcie poprzedniego dialogu
                dismiss()

            }

        }

    }
}
