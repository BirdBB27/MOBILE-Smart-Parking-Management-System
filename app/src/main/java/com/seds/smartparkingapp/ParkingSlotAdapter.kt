package com.seds.smartparkingapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

// THÊM MỚI: Truyền thêm một hàm onSlotClick để báo về cho Fragment biết ô nào vừa bị bấm
class ParkingSlotAdapter(
    private val slots: List<ParkingSlot>,
    private val onSlotClick: (ParkingSlot) -> Unit
) : RecyclerView.Adapter<ParkingSlotAdapter.SlotViewHolder>() {

    class SlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardSlot: MaterialCardView = view.findViewById(R.id.cardSlot)
        val tvSlotName: TextView = view.findViewById(R.id.tvSlotName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parking_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.tvSlotName.text = slot.slot_id

        // Đổi màu tùy theo trạng thái
        when (slot.status) {
            "Available" -> {
                holder.cardSlot.strokeColor = Color.parseColor("#4CAF50") // Xanh lá
                holder.cardSlot.setCardBackgroundColor(Color.TRANSPARENT)
                holder.tvSlotName.setTextColor(Color.parseColor("#4CAF50"))
            }
            "Occupied" -> {
                holder.cardSlot.strokeColor = Color.parseColor("#F44336") // Đỏ
                holder.cardSlot.setCardBackgroundColor(Color.TRANSPARENT)
                holder.tvSlotName.setTextColor(Color.parseColor("#F44336"))
            }
            "Reserved" -> {
                holder.cardSlot.strokeColor = Color.parseColor("#2196F3") // Xanh dương
                holder.cardSlot.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
                holder.tvSlotName.setTextColor(Color.parseColor("#2196F3"))
            }
            else -> {
                holder.cardSlot.strokeColor = Color.GRAY
                holder.tvSlotName.setTextColor(Color.GRAY)
            }
        }

        // THÊM MỚI: Bắt sự kiện Click vào ô đỗ xe
        holder.cardSlot.setOnClickListener {
            if (slot.status == "Available") {
                // Nếu ô trống, gọi hàm onSlotClick để chuyển trang
                onSlotClick(slot)
            } else {
                // Nếu ô đã có người, báo lỗi
                Toast.makeText(holder.itemView.context, "Ô đỗ ${slot.slot_id} đã có xe hoặc được đặt trước!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = slots.size
}