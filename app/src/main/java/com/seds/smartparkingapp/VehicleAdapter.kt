package com.seds.smartparkingapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class VehicleAdapter(private var vehicles: List<Vehicle>) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLicensePlate: TextView = view.findViewById(R.id.tvLicensePlate)
        val tvVehicleType: TextView = view.findViewById(R.id.tvVehicleType)
        val ivVehicleIcon: ImageView = view.findViewById(R.id.ivVehicleIcon)
        val cardView: MaterialCardView = view as MaterialCardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.tvLicensePlate.text = vehicle.license_plate
        holder.tvVehicleType.text = vehicle.vehicle_type

        // Đổi màu và icon tùy loại xe
        if (vehicle.vehicle_type.contains("Ô tô", ignoreCase = true)) {
            holder.ivVehicleIcon.setImageResource(android.R.drawable.ic_menu_directions)
            holder.ivVehicleIcon.setColorFilter(Color.parseColor("#D32F2F")) // Đỏ
            holder.cardView.strokeColor = Color.parseColor("#primary_blue") // Viền xanh cho xe ô tô
        } else {
            holder.ivVehicleIcon.setImageResource(android.R.drawable.ic_menu_compass)
            holder.ivVehicleIcon.setColorFilter(Color.GRAY)
            holder.cardView.strokeColor = Color.parseColor("#E0E0E0")
        }
    }

    override fun getItemCount(): Int = vehicles.size

    fun updateData(newVehicles: List<Vehicle>) {
        vehicles = newVehicles
        notifyDataSetChanged()
    }
}