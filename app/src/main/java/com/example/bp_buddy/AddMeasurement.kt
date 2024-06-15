package com.example.bp_buddy

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

class AddMeasurementDialogFragment : DialogFragment(), View.OnClickListener {

    private var inputSystolic: EditText?
    private var inputDiastolic: EditText?
    private var inputPulse: EditText?
    private var confirmButton: Button?
    private var cancelButton: Button?
    private var button1: Button?
    private var button2: Button?
    private var button3: Button?
    private var button4: Button?
    private var button5: Button?
    private var selectedMood: Int?

    init {
        inputSystolic = null
        inputDiastolic = null
        inputPulse = null
        confirmButton = null
        cancelButton = null
        button1 = null
        button2 = null
        button3 = null
        button4 = null
        button5 = null
        selectedMood = null
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
        button1 = view.findViewById(R.id.button1)
        button2 = view.findViewById(R.id.button2)
        button3 = view.findViewById(R.id.button3)
        button4 = view.findViewById(R.id.button4)
        button5 = view.findViewById(R.id.button5)

        // Ustawienie nasłuchiwania kliknięć przycisków
        confirmButton?.setOnClickListener(this)
        cancelButton?.setOnClickListener(this)
        button1?.setOnClickListener(this)
        button2?.setOnClickListener(this)
        button3?.setOnClickListener(this)
        button4?.setOnClickListener(this)
        button5?.setOnClickListener(this)


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
                dismiss() // Zamknij
            }

            R.id.button1 -> selectMood(1)
            R.id.button2 -> selectMood(2)
            R.id.button3 -> selectMood(3)
            R.id.button4 -> selectMood(4)
            R.id.button5 -> selectMood(5)

        }
    }

    private fun selectMood(mood: Int) {
        selectedMood = mood
//         Resetuj kolory przycisków
        button1?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textBG
            )
        )
        button2?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textBG
            )
        )
        button3?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textBG
            )
        )
        button4?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textBG
            )
        )
        button5?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textBG
            )
        )

        // Ustaw kolor wybranego przycisku
        when (mood) {
            1 -> button1?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink_200
                )
            )

            2 -> button2?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink_200
                )
            )

            3 -> button3?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink_200
                )
            )

            4 -> button4?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink_200
                )
            )

            5 -> button5?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink_200
                )
            )
        }
    }

    private fun validateInput(): Boolean {
        val systolic = inputSystolic?.text.toString().trim()
        val diastolic = inputDiastolic?.text.toString().trim()
        val pulse = inputPulse?.text.toString().trim()

        if (TextUtils.isEmpty(systolic) || TextUtils.isEmpty(diastolic) || TextUtils.isEmpty(pulse) || selectedMood == null) {
            showErrorSnackBar("Wszystkie pola muszą być wypełnione", true)
            return false
        }

        if (!diastolic?.isDigitsOnly()!!) {
            showErrorSnackBar("Ciśnienie rozkurczowe musi być liczbą", true)
            return false
        }

        if (!systolic?.isDigitsOnly()!!) {
            showErrorSnackBar("Ciśnienie skurczowe musi być liczbą", true)
            return false
        }

        if (!pulse?.isDigitsOnly()!!) {
            showErrorSnackBar("Tętno musi być liczbą", true)
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
                "Tętno" to inputPulse?.text.toString().toInt(),
                "Samopoczucie" to selectedMood
            )

            userCollection.document(measurementId)
                .set(measurement)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Dokument pomiarów został pomyślnie dodany jako $measurementId.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val dialog = MeasurementInfoDialogFragment()
                    dialog.setPreviousFragment(this@AddMeasurementDialogFragment)
                    // Przekazanie wartości ciśnienia do dialogu
                    val args = Bundle()
                    args.putString("diastolicValue", inputDiastolic?.text.toString())
                    args.putString("systolicValue", inputSystolic?.text.toString())
                    args.putString("pulseValue", inputPulse?.text.toString())
                    dialog.arguments = args
                    dialog.show(childFragmentManager, "MeasurementInfoDialogFragment")

//                    dismiss() // zamknięcie dialogu

                }
                .addOnFailureListener { e ->
                    showErrorSnackBar("Wystąpił błąd podczas dodawania dokumentu pomiarów", true)
                }
        }.addOnFailureListener { e ->
            showErrorSnackBar("Wystąpił błąd podczas pobierania dokumentów", true)
        }
    }


    private fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val snackbarView = snackbar.view

            // Ustawienie koloru paska Snackbar na podstawie typu komunikatu
            val bgColor = if (errorMessage) {
                ContextCompat.getColor(requireContext(), R.color.colorSnackBarError)
            } else {
                ContextCompat.getColor(requireContext(), R.color.colorSnackBarSuccess)
            }
            snackbarView.setBackgroundColor(bgColor)
            snackbar.show()
        }
    }
}
