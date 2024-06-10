package com.example.bp_buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MeasurementsAdapter(private val measurementsList: List<Map<String, Any>>) :
    RecyclerView.Adapter<MeasurementsAdapter.MeasurementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.measurement_adapter, parent, false)
        return MeasurementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) {
        val measurement = measurementsList[position]
        holder.bind(measurement)
    }

    override fun getItemCount(): Int = measurementsList.size

    class MeasurementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val measurementTextView: TextView = itemView.findViewById(R.id.measurementTextView)

        fun bind(measurement: Map<String, Any>) {
            measurementTextView.text = measurement.toString()
        }
    }
}
