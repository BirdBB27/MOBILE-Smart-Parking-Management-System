package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Ánh xạ layout của HomeFragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 1. Ánh xạ các UI hiển thị thông tin
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvBalance = view.findViewById<TextView>(R.id.tvBalance)

        // 2. Gọi API lấy thông tin Profile
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Lấy Context của Fragment để gọi Retrofit
                val api = RetrofitClient.getInstance(requireContext())
                val response = api.getUserProfile()

                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile != null) {
                        // Cập nhật tên
                        tvUserName?.text = "Xin chào, ${userProfile.full_name}!"

                        // Định dạng tiền tệ VNĐ cho số dư ví (VD: 50000 -> 50.000 đ)
                        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                        tvBalance?.text = format.format(userProfile.wallet_balance)
                    }
                } else {
                    // Lấy mã lỗi chính xác từ Server trả về
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(requireContext(), "Lỗi $errorCode: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi mạng: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Xử lý sự kiện click cho các nút chức năng
        val btnTopUp = view.findViewById<Button>(R.id.btnTopUp)
        btnTopUp?.setOnClickListener {
            val intent = Intent(activity, TopUpActivity::class.java)
            startActivity(intent)
        }

        val btnBooking = view.findViewById<View>(R.id.btnBooking)
        btnBooking?.setOnClickListener {
            val intent = Intent(activity, BookingActivity::class.java)
            startActivity(intent)
        }

        val btnScanQR = view.findViewById<View>(R.id.btnScanQR)
        btnScanQR?.setOnClickListener {
            val intent = Intent(activity, QrScanActivity::class.java)
            startActivity(intent)
        }

        val btnMonthlyTicket = view.findViewById<View>(R.id.btnMonthlyTicket)
        btnMonthlyTicket?.setOnClickListener {
            val intent = Intent(activity, MonthlyTicketActivity::class.java)
            startActivity(intent)
        }

        val btnSupport = view.findViewById<View>(R.id.btnSupport)
        btnSupport?.setOnClickListener {
            val intent = Intent(activity, SupportActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}