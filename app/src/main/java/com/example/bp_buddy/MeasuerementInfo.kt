package com.example.bp_buddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

/**
 * Fragment dialogowy do wyświetlania informacji o dokonanym pomiarze ciśnienia krwi.
 */
class MeasurementInfoDialogFragment : DialogFragment(), View.OnClickListener {

    private var previousFragment: DialogFragment?
    private var confirmButton1: Button?
    private var measurementInfo1: TextView?
    private var measurementInfo2: TextView?
    private var measurementInfo3: TextView?


    init {
        previousFragment = null
        confirmButton1 = null
        measurementInfo1 = null
        measurementInfo2 = null
        measurementInfo3 = null

    }

    /**
     * Metoda ustawiająca poprzedni fragment dialogowy, który wywołał bieżący dialog.
     *
     * @param fragment Poprzedni fragment dialogowy.
     */
    fun setPreviousFragment(fragment: DialogFragment) {
        previousFragment = fragment

    }

    /**
     * Metoda cyklu życia fragmentu dialogowego, odpowiedzialna za tworzenie i inflację widoku dialogowego.
     *
     * @param inflater           Inflater używany do inflacji widoku.
     * @param container          Kontener rodzicielski, do którego zostanie dołączony widok dialogowy.
     * @param savedInstanceState Zapisany stan fragmentu, jeśli jest ponownie tworzony po zniszczeniu.
     * @return Zainicjalizowany widok dialogowy.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_measurement_info, container, false)

        // Inicjalizacja pól wejściowych i przycisków
        measurementInfo1 = view.findViewById(R.id.measurementInfo1)
        measurementInfo2 = view.findViewById(R.id.measurementInfo2)
        measurementInfo3 = view.findViewById(R.id.measurementInfo3)
        confirmButton1 = view.findViewById(R.id.confirmButton1)

        // Pobranie wartości ciśnienia z argumentów
        val diastolicValue = arguments?.getString("diastolicValue")?.toIntOrNull() ?: 0
        val systolicValue = arguments?.getString("systolicValue")?.toIntOrNull() ?: 0
        val pulseValue = arguments?.getString("pulseValue")?.toIntOrNull() ?: 0

        measurementInformation(systolicValue, diastolicValue, pulseValue)

        // Ustawienie nasłuchiwania kliknięć przycisków
        confirmButton1?.setOnClickListener(this)

        return view
    }

    /**
     * Metoda obsługująca kliknięcia przycisków w dialogu.
     *
     * @param view Widok, który został kliknięty.
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.confirmButton1 -> {
                previousFragment?.dismiss() // zamknięcie poprzedniego dialogu
                dismiss()

            }

        }

    }
// diastolic= rozkurczowe
    /**
     * Metoda do wyświetlenia informacji o dokonanym pomiarze.
     *
     * @param systolicValue  Wartość ciśnienia skurczowego.
     * @param diastolicValue Wartość ciśnienia rozkurczowego.
     * @param pulseValue     Wartość tętna.
     */
    private fun measurementInformation(systolicValue: Int, diastolicValue: Int, pulseValue: Int) {
        val baseText1 = "Twoje ciśnienie skurczowe jest: "
        val baseText2 = "Twoje ciśnienie rozkurczowe jest: "
        val baseText3 = "Twoje tętno jest: "

    val condition1 = when {
        systolicValue in 0..89 -> "zbyt niskie, udaj się do lekarza/zażyj leki"
        systolicValue in 90..119 -> "optymalne"
        systolicValue in 120..129 -> "w normie"
        systolicValue in 130..140 -> "wysokie"
        systolicValue > 140 -> "zbyt wysokie, udaj się do lekarza/zażyj leki"
        else -> ""
    }
    val updatedText1 = "$baseText1 $condition1"
    measurementInfo1?.text = updatedText1


    val condition2 = when {
        diastolicValue in 0..59 -> "zbyt niskie, udaj się do lekarza/zażyj leki"
        diastolicValue in 60..79 -> "optymalne"
        diastolicValue in 80..84 -> "w normie"
        diastolicValue in 85..90 -> "wysokie"
        diastolicValue > 90 -> "zbyt wysokie, udaj się do lekarza/zażyj leki"
        else -> ""
    }
        val updatedText2 = "$baseText2 $condition2"
        measurementInfo2?.text = updatedText2

    val condition3 = when {
        pulseValue in 0..59 -> "zbyt niskie"
        pulseValue in 60..100 -> "w normie"
        pulseValue > 100 -> "zbyt wysokie"
        else -> ""
    }
        val updatedText3 = "$baseText3 $condition3"
        measurementInfo3?.text = updatedText3
    }

}

