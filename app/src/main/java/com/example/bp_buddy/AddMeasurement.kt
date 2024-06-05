package com.example.bp_buddy

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import androidx.fragment.app.DialogFragment

class AddMeasurementDialogFragment : DialogFragment(), View.OnClickListener {

    private var inputSystolic: EditText? = null
    private var inputDiastolic: EditText? = null
    private var inputPulse: EditText? = null
    private var confirmButton: Button? = null
    private var cancelButton: Button? = null

    init {
        inputSystolic = null
        inputDiastolic = null
        inputPulse = null
        confirmButton = null
        cancelButton = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_measurement, container, false)

        // Inicjalizacja pól wejściowych i przycisków
        inputSystolic = view.findViewById(R.id.inputSystolic)
        inputDiastolic = view.findViewById(R.id.inputDiastolic)
        inputPulse = view.findViewById(R.id.inputPulse)
        confirmButton = view.findViewById(R.id.confirmButton)
        cancelButton = view.findViewById(R.id.cancelButton)

        // Ustawienie nasłuchiwania kliknięć przycisków
        confirmButton?.setOnClickListener(this)
        cancelButton?.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.confirmButton -> {
                // Obsługa kliknięcia przycisku "Potwierdź"
                val email = FirebaseAuth.getInstance().currentUser?.email.toString()
                addMeasurement(email)
            }
            R.id.cancelButton -> {
                // Obsługa kliknięcia przycisku "Anuluj"
                dismiss() // Zamknij dialog
            }
        }
    }

    private fun validateInput(): Boolean {
        val systolic = inputSystolic?.text.toString().trim()
        val diastolic = inputDiastolic?.text.toString().trim()
        val pulse = inputPulse?.text.toString().trim()

        if (TextUtils.isEmpty(systolic) || TextUtils.isEmpty(diastolic) || TextUtils.isEmpty(pulse)) {
            (activity as BaseActivity).showErrorSnackBar("Wszystkie pola muszą być wypełnione", true)
            return false
        }

        return true
    }

    private fun addMeasurement(email: String) {
        if (!validateInput()) return

        val db = Firebase.firestore
        val userCollection = db.collection(email)

        userCollection.get().addOnSuccessListener { documents ->
            val nextMeasurementNumber = documents.size() + 1
            val measurementId = "Pomiar $nextMeasurementNumber"

            val currentTime = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dateTime = dateFormat.format(Date(currentTime))

            val measurement = hashMapOf(
                "Data i czas" to dateTime,
                "Ciśnienie rozkurczowe" to inputDiastolic?.text.toString().toInt(),
                "Ciśnienie skurczowe" to inputSystolic?.text.toString().toInt(),
                "Tętno" to inputPulse?.text.toString().toInt()
            )

            userCollection.document(measurementId)
                .set(measurement)
                .addOnSuccessListener {
                    (activity as? BaseActivity)?.showErrorSnackBar("Dokument pomiarów został pomyślnie dodany jako $measurementId.", false)
                    dismiss() // Zamknij dialog

                }
                .addOnFailureListener { e ->
                    (activity as? BaseActivity)?.showErrorSnackBar("Wystąpił błąd podczas dodawania dokumentu pomiarów: $e", true)
                }
        }.addOnFailureListener { e ->
            (activity as? BaseActivity)?.showErrorSnackBar("Wystąpił błąd podczas pobierania dokumentów: $e", true)
        }
    }
}
