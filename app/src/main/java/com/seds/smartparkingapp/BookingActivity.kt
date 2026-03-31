package com.seds.smartparkingapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private var startTimeApiFormat: String = ""
    private var endTimeApiFormat: String = ""

    // BIẾN QUAN TRỌNG: Lưu số tiền thực tế sẽ bị trừ (Mặc định 10k)
    private var currentBookingFee = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val tvParkingSlotInfo = findViewById<TextView>(R.id.tvParkingSlotInfo)
        val actvBookingLicensePlate = findViewById<AutoCompleteTextView>(R.id.actvBookingLicensePlate)

        val cvStartTime = findViewById<View>(R.id.cvStartTime)
        val tvStartTime = findViewById<TextView>(R.id.tvStartTime)
        val cvEndTime = findViewById<View>(R.id.cvEndTime)
        val tvEndTime = findViewById<TextView>(R.id.tvEndTime)

        // Các TextView hiển thị tiền
        val tvBookingFee = findViewById<TextView>(R.id.tvBookingFee)
        val tvTotalFee = findViewById<TextView>(R.id.tvTotalFee)

        val selectedSlot = intent.getStringExtra("SELECTED_SLOT") ?: ""
        if (selectedSlot.isNotEmpty()) {
            tvParkingSlotInfo.text = "Vị trí ô đỗ đã chọn: Ô $selectedSlot"
        }

        btnBack.setOnClickListener { finish() }

        // ==========================================
        // TẢI DỮ LIỆU VÀ KIỂM TRA VIP SONG SONG
        // ==========================================
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(this@BookingActivity)

                // 1. Tải danh sách phương tiện
                try {
                    val response = api.getVehicles()
                    if (response.isSuccessful && response.body() != null) {
                        val vehicles = response.body()!!.data
                        val licensePlates = vehicles.map { it.license_plate }
                        val adapter = ArrayAdapter(this@BookingActivity, android.R.layout.simple_dropdown_item_1line, licensePlates)
                        actvBookingLicensePlate.setAdapter(adapter)
                        actvBookingLicensePlate.setOnClickListener { actvBookingLicensePlate.showDropDown() }
                    }
                } catch (e: Exception) {}

                // 2. KIỂM TRA TRẠNG THÁI VÉ THÁNG VIP
                try {
                    val vipRes = api.getMyTicket()
                    if (vipRes.isSuccessful && vipRes.body() != null) {
                        if (vipRes.body()!!.has_ticket) {
                            // CẬP NHẬT GIAO DIỆN VIP (MIỄN PHÍ CỌC)
                            currentBookingFee = 0
                            tvBookingFee.text = "0 đ (Miễn phí VIP)"
                            tvBookingFee.setTextColor(Color.parseColor("#FF9800")) // Màu cam VIP

                            tvTotalFee.text = "0 đ"
                            tvTotalFee.setTextColor(Color.parseColor("#FF9800"))

                            btnConfirm.text = "Xác nhận Đặt chỗ (VIP)"
                            btnConfirm.setBackgroundColor(Color.parseColor("#FF9800"))
                        }
                    }
                } catch (e: Exception) {}

            } catch (e: Exception) {}
        }

        cvStartTime.setOnClickListener {
            pickDateTime(tvStartTime) { formattedApiStr -> startTimeApiFormat = formattedApiStr }
        }
        cvEndTime.setOnClickListener {
            pickDateTime(tvEndTime) { formattedApiStr -> endTimeApiFormat = formattedApiStr }
        }

        btnConfirm.setOnClickListener {
            val licensePlate = actvBookingLicensePlate.text.toString().trim()

            if (selectedSlot.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy thông tin ô đỗ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (licensePlate.isEmpty() || licensePlate.contains("Chọn phương tiện")) {
                Toast.makeText(this, "Vui lòng chọn biển số xe của bạn!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (startTimeApiFormat.isEmpty() || endTimeApiFormat.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Giờ nhận và Giờ trả chỗ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnConfirm.text = "Đang xử lý..."
            btnConfirm.isEnabled = false

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@BookingActivity)

                    // CHỖ NÀY ĐÃ ĐƯỢC ĐỔI TỪ 10000 CỨNG SANG BIẾN currentBookingFee
                    val request = BookingRequest(selectedSlot, licensePlate, startTimeApiFormat, endTimeApiFormat, currentBookingFee)
                    val response = api.bookParkingSlot(request)

                    if (response.isSuccessful) {
                        val successMsg = if (currentBookingFee == 0) "Đặt chỗ VIP thành công (Miễn phí)!" else "Đặt chỗ thành công!"
                        Toast.makeText(this@BookingActivity, successMsg, Toast.LENGTH_LONG).show()

                        val intent = Intent(this@BookingActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi đặt chỗ (Vui lòng kiểm tra số dư ví)"
                        Toast.makeText(this@BookingActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@BookingActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                } finally {
                    btnConfirm.text = if (currentBookingFee == 0) "Xác nhận Đặt chỗ (VIP)" else "Thanh toán & Đặt chỗ"
                    btnConfirm.isEnabled = true
                }
            }
        }
    }

    private fun pickDateTime(textView: TextView, onSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            TimePickerDialog(this, { _, selectedHour, selectedMinute ->

                val displayStr = String.format(Locale.getDefault(), "%02d/%02d/%04d - %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute)
                textView.text = displayStr
                textView.setTextColor(Color.BLACK)

                val apiStr = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d:00", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute)
                onSelected(apiStr)

            }, hour, minute, true).show()
        }, year, month, day).show()
    }
}