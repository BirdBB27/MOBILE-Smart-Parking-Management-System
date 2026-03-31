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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Ánh xạ các UI Profile
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvBalance = view.findViewById<TextView>(R.id.tvBalance)

        // Ánh xạ các UI Đang giữ chỗ
        val cvActiveBooking = view.findViewById<View>(R.id.cvActiveBooking)
        val llNoBooking = view.findViewById<View>(R.id.llNoBooking)
        val tvBookingSlot = view.findViewById<TextView>(R.id.tvBookingSlot)
        val tvBookingLicensePlate = view.findViewById<TextView>(R.id.tvBookingLicensePlate)
        val tvBookingStartTime = view.findViewById<TextView>(R.id.tvBookingStartTime)
        val tvBookingEndTime = view.findViewById<TextView>(R.id.tvBookingEndTime)
        val btnViewQR = view.findViewById<TextView>(R.id.btnViewQR)

        // Mặc định ẩn thẻ giữ chỗ trong lúc chờ tải dữ liệu
        cvActiveBooking.visibility = View.GONE
        llNoBooking.visibility = View.VISIBLE

        // GỌI API LẤY DỮ LIỆU
        viewLifecycleOwner.lifecycleScope.launch {
            val api = RetrofitClient.getInstance(requireContext())
            val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

            // 1. Tải Profile & Số dư ví
            try {
                val profileRes = api.getUserProfile()
                if (profileRes.isSuccessful && profileRes.body()?.data != null) {
                    val user = profileRes.body()!!.data
                    tvUserName.text = "Xin chào, ${user.name} 👋"
                    tvBalance.text = format.format(user.balance)
                }
            } catch (e: Exception) {
                // Lỗi mạng ẩn im lặng để không phiền người dùng
            }

            // 2. Tải Trạng thái Đang giữ chỗ (THỰC TẾ)
            try {
                val bookingRes = api.getActiveBooking()
                if (bookingRes.isSuccessful && bookingRes.body() != null) {
                    val response = bookingRes.body()!!

                    // Nếu Backend báo là có đơn đặt chỗ đang Active
                    if (response.has_active && response.data != null) {
                        val bookingData = response.data

                        // Bật thẻ thông tin lên, giấu thông báo rỗng đi
                        cvActiveBooking.visibility = View.VISIBLE
                        llNoBooking.visibility = View.GONE

                        // Bơm dữ liệu thật vào View
                        tvBookingSlot.text = "Vị trí: Ô đỗ ${bookingData.slot_id}"
                        tvBookingLicensePlate.text = bookingData.license_plate

                        // (Lưu ý: Nếu giờ giấc BE trả về chưa đẹp, có thể cần xử lý chuỗi ở đây)
                        tvBookingStartTime.text = bookingData.start_time
                        tvBookingEndTime.text = bookingData.end_time

                        btnViewQR.setOnClickListener {
                            Toast.makeText(requireContext(), "Mở QR Code cho ô đỗ ${bookingData.slot_id}", Toast.LENGTH_SHORT).show()
                            // Chuyển sang màn hình QR ở đây (ví dụ: QrScanActivity)
                        }
                    } else {
                        // Khách chưa đặt chỗ nào -> Hiện giao diện trống
                        cvActiveBooking.visibility = View.GONE
                        llNoBooking.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                // Lỗi mạng -> Mặc định hiện chưa có đặt chỗ
                cvActiveBooking.visibility = View.GONE
                llNoBooking.visibility = View.VISIBLE
            }
        }

        // 3. Xử lý các nút chức năng chính
        view.findViewById<Button>(R.id.btnTopUp)?.setOnClickListener {
            startActivity(Intent(requireContext(), TopUpActivity::class.java))
        }
        view.findViewById<View>(R.id.btnBooking)?.setOnClickListener {
            startActivity(Intent(activity, BookingActivity::class.java))
        }
        view.findViewById<View>(R.id.btnScanQR)?.setOnClickListener {
            startActivity(Intent(activity, QrScanActivity::class.java))
        }
        view.findViewById<View>(R.id.btnMonthlyTicket)?.setOnClickListener {
            startActivity(Intent(activity, MonthlyTicketActivity::class.java))
        }
        view.findViewById<View>(R.id.btnSupport)?.setOnClickListener {
            startActivity(Intent(activity, SupportActivity::class.java))
        }

        return view
    }
}