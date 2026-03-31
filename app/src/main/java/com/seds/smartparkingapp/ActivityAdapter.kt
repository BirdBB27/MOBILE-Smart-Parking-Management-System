package com.seds.smartparkingapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class ActivityAdapter(private var historyList: List<HistoryItem>) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvLicensePlate: TextView = view.findViewById(R.id.tvLicensePlate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item = historyList[position]
        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

        holder.tvTitle.text = item.title
        holder.tvTime.text = item.timestamp
        holder.tvLicensePlate.text = if (!item.license_plate.isNullOrEmpty()) "Xe: ${item.license_plate}" else ""
        holder.tvStatus.text = item.status

        // Phân loại Giao diện dựa theo type Backend trả về
        when (item.type) {
            "TOP_UP", "TOPUP" -> { // Nạp tiền (Màu xanh)
                holder.ivIcon.setImageResource(android.R.drawable.ic_input_add)
                holder.ivIcon.setColorFilter(Color.parseColor("#4CAF50"))
                holder.tvAmount.text = "+ ${format.format(item.amount ?: 0)}"
                holder.tvAmount.setTextColor(Color.parseColor("#4CAF50"))
            }
            "BOOKING", "MONTHLY_TICKET", "PAYMENT" -> { // Trừ tiền (Màu đỏ)
                holder.ivIcon.setImageResource(android.R.drawable.ic_dialog_map)
                holder.ivIcon.setColorFilter(Color.parseColor("#F44336"))
                holder.tvAmount.text = "- ${format.format(item.amount ?: 0)}"
                holder.tvAmount.setTextColor(Color.parseColor("#F44336"))
            }
            else -> {
                holder.ivIcon.setImageResource(android.R.drawable.ic_menu_info_details)
                holder.ivIcon.setColorFilter(Color.GRAY)
                holder.tvAmount.text = ""
            }
        }

        // Xử lý Giao diện Trạng thái
        if (item.status.contains("hủy", ignoreCase = true) || item.status.contains("thất bại", ignoreCase = true)) {
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"))
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"))
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
            holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"))
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun updateData(newList: List<HistoryItem>) {
        historyList = newList
        notifyDataSetChanged()
    }
}