package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm) // Nhớ đặt ID này cho MaterialButton trong XML

        // Lấy tên ô đỗ từ Intent
        val selectedSlot = intent.getStringExtra("SELECTED_SLOT") ?: ""

        btnBack.setOnClickListener { finish() }

        btnConfirm.setOnClickListener {
            if (selectedSlot.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy thông tin ô đỗ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Đổi trạng thái nút
            btnConfirm.text = "Đang xử lý..."
            btnConfirm.isEnabled = false

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@BookingActivity)
                    // Gửi request với biển số xe mặc định để test
                    val request = BookingRequest(selectedSlot, "59A-123.45")
                    val response = api.bookParkingSlot(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@BookingActivity, "Đặt chỗ thành công!", Toast.LENGTH_LONG).show()

                        // Đặt thành công thì quay về Trang chủ
                        val intent = Intent(this@BookingActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi đặt chỗ"
                        Toast.makeText(this@BookingActivity, errorMsg, Toast.LENGTH_LONG).show()
                        btnConfirm.text = "Thanh toán & Đặt chỗ"
                        btnConfirm.isEnabled = true
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@BookingActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
                    btnConfirm.text = "Thanh toán & Đặt chỗ"
                    btnConfirm.isEnabled = true
                }
            }
        }
    }
}