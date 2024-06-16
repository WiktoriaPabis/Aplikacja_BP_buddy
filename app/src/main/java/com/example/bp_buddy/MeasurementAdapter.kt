package com.example.bp_buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter RecyclerView do wyświetlania listy pomiarów.
 *
 * @param measurementsList1 Lista pomiarów do wyświetlenia.
 * @param clickListener1    Listener reagujący na kliknięcia elementów listy.
 */
class MeasurementsAdapter(measurementsList1: List<Map<String, Any>>, clickListener1: (Int) -> Unit
                                                                    // Listener dla kliknięcia elementu
    ): RecyclerView.Adapter<MeasurementsAdapter.MeasurementViewHolder>() {

        private val measurementsList = measurementsList1
        private val clickListener = clickListener1

    /**
     * Tworzenie nowego ViewHoldera opartego na widoku elementu listy.
     *
     * @param parent   Rodzic widoku, do którego ViewHolder zostanie dołączony.
     * @param viewType Typ widoku, jeśli adapter ma więcej, niż jeden typ elementów (nieużywane w tym przypadku).
     * @return Nowy ViewHolder stworzony na podstawie widoku elementu listy.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.measurement_adapter, parent, false)
        return MeasurementViewHolder(view)
    }

    /**
     * Wiązanie danych z danymi na określonej pozycji w RecyclerView.
     *
     * @param holder   ViewHolder do aktualizacji zawartości.
     * @param position Pozycja elementu w liście pomiarów.
     */
    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) {
        val measurement = measurementsList[position]
        holder.bind(measurement)

        // Ustawienie OnClickListener dla elementu w RecyclerView
        holder.itemView.setOnClickListener {
            clickListener.invoke(holder.adapterPosition)
        }
    }

    /**
     * Zwraca liczbę elementów w liście pomiarów.
     *
     * @return Liczba elementów w liście pomiarów.
     */
    override fun getItemCount(): Int = measurementsList.size

    /**
     * ViewHolder do przechowywania widoku elementu listy pomiarów.
     *
     * @param itemView Widok reprezentujący pojedynczy element listy pomiarów.
     */
    class MeasurementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val measurementTextView: TextView = itemView.findViewById(R.id.measurementTextView)

        /**
         * Metoda do wiązania danych pomiaru z widokiem ViewHoldera.
         *
         * @param measurement Mapa zawierająca dane pomiaru.
         */
        fun bind(measurement: Map<String, Any>) {
            measurementTextView.text = measurement.toString()
        }
    }
}
