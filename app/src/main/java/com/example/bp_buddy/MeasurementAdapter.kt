package com.example.bp_buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.entry_card_design, parent, false)
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
        private val statSKR : TextView = itemView.findViewById(R.id.cisnienie_SKR)
        private val statROZ : TextView = itemView.findViewById(R.id.cisnienie_ROZ)
        private val statTETNO : TextView = itemView.findViewById(R.id.tetno)
        private val mood : ImageView = itemView.findViewById(R.id.mood)
        private val dateAndTime : TextView = itemView.findViewById(R.id.date_and_time)
        private val entryNumber : TextView = itemView.findViewById(R.id.entry_number)
        /**
         * Metoda do wiązania danych pomiaru z widokiem ViewHoldera.
         *
         * @param measurement Mapa zawierająca dane pomiaru.
         */
        fun bind(measurement: Map<String, Any>) {
            statSKR.text = buildString {
                append("Ciśnienie skurczowe: ")
                append(measurement["Ciśnienie skurczowe"].toString())
            }
            statROZ.text = buildString {
                append("Ciśnienie rozkurczowe: ")
                append(measurement["Ciśnienie rozkurczowe"].toString())
            }
            statTETNO.text = buildString {
                append("Tętno: ")
                append(measurement["Tętno"].toString())
            }
                when (measurement["Samopoczucie"].toString()) {
                    "1" -> mood.setBackgroundResource(R.drawable.buzki5)
                    "2" -> mood.setBackgroundResource(R.drawable.buzki4)
                    "3" -> mood.setBackgroundResource(R.drawable.buzki3)
                    "4" -> mood.setBackgroundResource(R.drawable.buzki2)
                    "5" -> mood.setBackgroundResource(R.drawable.buzki1)
                }
            dateAndTime.text = buildString {
                append("Data i czas: ")
                append(measurement["Data i czas"].toString().elementAt(8))
                append(measurement["Data i czas"].toString().elementAt(9))
                append(".")
                append(measurement["Data i czas"].toString().elementAt(5))
                append(measurement["Data i czas"].toString().elementAt(6))
                append(".")
                append(measurement["Data i czas"].toString().elementAt(0))
                append(measurement["Data i czas"].toString().elementAt(1))
                append(measurement["Data i czas"].toString().elementAt(2))
                append(measurement["Data i czas"].toString().elementAt(3))
                append(" ")
                append(measurement["Data i czas"].toString().elementAt(11))
                append(measurement["Data i czas"].toString().elementAt(12))
                append(":")
                append(measurement["Data i czas"].toString().elementAt(14))
                append(measurement["Data i czas"].toString().elementAt(15))
            }
            entryNumber.text = measurement["docId"].toString()

        }
    }
}
