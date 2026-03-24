package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MapFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val rvZoneA = view.findViewById<RecyclerView>(R.id.rvZoneA)
        val rvZoneB = view.findViewById<RecyclerView>(R.id.rvZoneB)

        // Sắp xếp dạng lưới 5 cột cho giống thiết kế cũ
        rvZoneA.layoutManager = GridLayoutManager(requireContext(), 5)
        rvZoneB.layoutManager = GridLayoutManager(requireContext(), 5)

        // Gọi API lấy sơ đồ bãi đỗ
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(requireContext())
                val response = api.getParkingSlots()

                if (response.isSuccessful && response.body() != null) {
                    val allSlots = response.body()!!.data // Lấy biến data từ API

                    // Tách danh sách theo Zone A và B
                    val slotsA = allSlots.filter { it.zone == "A" }
                    val slotsB = allSlots.filter { it.zone == "B" }

                    // Gắn vào Adapter và xử lý sự kiện Click
                    rvZoneA.adapter = ParkingSlotAdapter(slotsA) { slot ->
                        goToBookingActivity(slot.slot_id)
                    }
                    rvZoneB.adapter = ParkingSlotAdapter(slotsB) { slot ->
                        goToBookingActivity(slot.slot_id)
                    }

                } else {
                    Toast.makeText(requireContext(), "Lỗi tải sơ đồ: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    private fun goToBookingActivity(slotId: String) {
        val intent = Intent(requireContext(), BookingActivity::class.java)
        // Gắn tên ô đỗ (VD: "A1") vào giỏ hàng mang tên "SELECTED_SLOT"
        intent.putExtra("SELECTED_SLOT", slotId)
        startActivity(intent)
    }
}