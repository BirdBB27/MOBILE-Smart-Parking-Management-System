package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Ánh xạ layout của HomeFragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Tìm nút Nạp tiền (Hãy đảm bảo bạn đã đặt ID android:id="@+id/btnTopUp" cho nút Nạp tiền trong fragment_home.xml)
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