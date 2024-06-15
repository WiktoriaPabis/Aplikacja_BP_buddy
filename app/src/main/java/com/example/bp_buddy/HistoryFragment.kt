package com.example.bp_buddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var measurementsAdapter: MeasurementsAdapter
    private val measurementsList = mutableListOf<Map<String, Any>>()
    private var refreshButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Przekazanie funkcji do obsługi kliknięcia do MeasurementsAdapter
        measurementsAdapter = MeasurementsAdapter(measurementsList) { position ->
            val measurementData = measurementsList[position]
            val docId = measurementData["docId"] as String // Pobieranie ID dokumentu
//..................................................................................................

             EditScreen(measurementData, docId)

        }
            recyclerView.adapter = measurementsAdapter

            refreshButton = view.findViewById(R.id.refreshButton)

            // Ustawienie nasłuchiwania kliknięć przycisku logowania
            refreshButton?.setOnClickListener {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val email: String = currentUser?.email.toString()
                measurementsList.clear()
                getAllMeasurements(email)
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            val email: String = currentUser?.email.toString()

            getAllMeasurements(email)

            return view
        }

        private fun getAllMeasurements(email: String) {
            val db = Firebase.firestore
            val userCollection = db.collection(email)

            userCollection.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val measurementData = document.data

                        measurementData["docId"] = document.id // NOWE DODANE!!

                        measurementsList.add(measurementData)
                    }
                    measurementsAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Wystąpił błąd podczas pobierania dokumentów: $e",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

    private fun EditScreen(measurementData: Map<String, Any>, docId: String) {
        val fragment = EditMeasurementFragment()
        val args = Bundle().apply {
            putString("docId", docId)
            putString("Ciśnienie skurczowe", measurementData["Ciśnienie skurczowe"].toString())
            putString("Ciśnienie rozkurczowe", measurementData["Ciśnienie rozkurczowe"].toString())
            putString("Tętno", measurementData["Tętno"].toString())
            putString("Data i czas", measurementData["Data i czas"].toString())
            putString("Samopoczucie", measurementData["Samopoczucie"].toString())
        }
        fragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}




