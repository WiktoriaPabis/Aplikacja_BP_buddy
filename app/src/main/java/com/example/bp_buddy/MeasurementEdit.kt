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
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Fragment odpowiedzialny za edycję istniejącego pomiaru ciśnienia krwi.
 */
class EditMeasurementFragment : Fragment() {

    private var editSystolic: EditText? = null
    private var editDiastolic: EditText? = null
    private var editPulse: EditText? = null
    private var editData: EditText? = null
    private var editMood: EditText? = null
    private var confirmChangesButton: Button? = null
    private var cancelButton: Button? = null
    private var docId: String? = null
    private var deleteButton: Button? = null


    /**
     * Tworzy i zwraca widok fragmentu edycji pomiaru.
     *
     * @param inflater           Obiekt LayoutInflater używany do nadmuchiwania widoku fragmentu.
     * @param container          Rodzic widoku fragmentu (w tym przypadku RecyclerView).
     * @param savedInstanceState Opcjonalny stan poprzedniego zapisu fragmentu, zapisany
     *                           w obiekcie Bundle.
     * @return Zwraca przygotowany widok fragmentu.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_measurement_edit, container, false)

        editSystolic = view.findViewById(R.id.editSystolic)
        editDiastolic = view.findViewById(R.id.editDiastolic)
        editPulse = view.findViewById(R.id.editPulse)
        editData = view.findViewById(R.id.editData)
        editMood = view.findViewById(R.id.editMood)
        confirmChangesButton = view.findViewById(R.id.confirmChangesButton)
        cancelButton = view.findViewById(R.id.cancelButton)
        deleteButton = view.findViewById(R.id.deleteButton)

        val args = arguments
        docId = args?.getString("docId")
        editSystolic?.setText(args?.getString("Ciśnienie skurczowe"))
        editDiastolic?.setText(args?.getString("Ciśnienie rozkurczowe"))
        editPulse?.setText(args?.getString("Tętno"))
        editData?.setText(args?.getString("Data i czas"))
        editMood?.setText(args?.getString("Samopoczucie"))

        confirmChangesButton?.setOnClickListener {
            updateMeasurement()
        }

        cancelButton?.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        deleteButton?.setOnClickListener {
            deleteMeasurement()
        }

        return view
    }

    /**
     * Metoda do aktualizacji pomiaru w bazie danych Firebase Firestore.
     * Sprawdza poprawność wprowadzonych danych przed wysłaniem ich do bazy.
     */
    private fun updateMeasurement() {
        val systolic = editSystolic?.text.toString()
        val diastolic = editDiastolic?.text.toString()
        val pulse = editPulse?.text.toString()
        val data = editData?.text.toString()
        val mood = editMood?.text.toString()


        if (TextUtils.isEmpty(systolic) || TextUtils.isEmpty(diastolic) || TextUtils.isEmpty(pulse)
            || TextUtils.isEmpty(mood) || TextUtils.isEmpty(data)) {
            showErrorSnackBar("Wszystkie pola muszą być wypełnione", true)
            return
        }

        if (!diastolic?.isDigitsOnly()!!) {
            showErrorSnackBar("Ciśnienie rozkurczowe musi być liczbą", true)
            return
        }

        if (!systolic?.isDigitsOnly()!!) {
            showErrorSnackBar("Ciśnienie skurczowe musi być liczbą", true)
            return
        }

        if (!pulse?.isDigitsOnly()!!) {
            showErrorSnackBar("Tętno musi być liczbą", true)
            return
        }

        if (!mood?.isDigitsOnly()!!) {
            showErrorSnackBar("Samopoczucie musi być liczbą", true)
            return
        }

        if (!(mood.toInt() in 1..5)) {
            showErrorSnackBar("Samopoczucie musi być w zakresie od 1 do 5 (1 najgorsze :( 5 najlepsze :) )", true)
            return
        }


        val db = Firebase.firestore
        val measurementMap = hashMapOf(
            "Ciśnienie skurczowe" to systolic,
            "Ciśnienie rozkurczowe" to diastolic,
            "Tętno" to pulse,
            "Data i czas" to data,
            "Samopoczucie" to mood
        )

        docId?.let {
            db.collection(FirebaseAuth.getInstance().currentUser?.email.toString())
                .document(it)
                .set(measurementMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Pomiar zaktualizowany", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                }
                .addOnFailureListener { e ->
                    showErrorSnackBar("Błąd podczas aktualizacji pomiaru: ", true)
                }
        }
    }

    /**
     * Metoda do wyświetlania paska Snackbar z informacją o błędzie.
     *
     * @param message      Wiadomość do wyświetlenia.
     * @param errorMessage Flaga określająca typ komunikatu (true - błąd, false - sukces).
     */
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

    /**
     * Metoda do usuwania pomiaru z bazy danych Firebase Firestore.
     * Po usunięciu pomiaru wyświetla powiadomienie toast z informacją.
     */
    private fun deleteMeasurement() {
        val db = Firebase.firestore
        docId?.let {
            db.collection(FirebaseAuth.getInstance().currentUser?.email.toString())
                .document(it)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Pomiar usunięty", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                }
                .addOnFailureListener { e ->
                    showErrorSnackBar("Błąd podczas usuwania pomiaru: ", true)
                }
        }
    }
}

