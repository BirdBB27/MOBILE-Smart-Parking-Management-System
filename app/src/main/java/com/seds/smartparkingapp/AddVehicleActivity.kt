package com.seds.smartparkingapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddVehicleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        // 1. Ánh xạ giao diện (Nhớ check ID xem có đúng với file XML không)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etLicensePlate = findViewById<EditText>(R.id.etLicensePlate) // Ô nhập biển số (VD: 59A-123.45)
        val btnSaveVehicle = findViewById<Button>(R.id.btnSaveVehicle) // Nút Lưu

        // Xử lý nút quay lại
        btnBack?.setOnClickListener {
            finish()
        }

        // 2. Xử lý khi bấm nút Lưu
        btnSaveVehicle?.setOnClickListener {
            val licensePlate = etLicensePlate.text.toString().trim()

            // Kiểm tra rỗng
            if (licensePlate.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập biển số xe!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Đổi trạng thái nút
            btnSaveVehicle.text = "Đang lưu..."
            btnSaveVehicle.isEnabled = false

            // Gọi API Thêm xe
            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@AddVehicleActivity)
                    val request = AddVehicleRequest(licensePlate)
                    val response = api.addVehicle(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@AddVehicleActivity, "Thêm xe thành công!", Toast.LENGTH_SHORT).show()
                        finish() // Đóng trang này, quay lại màn hình Quản lý xe
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi khi thêm xe"
                        Toast.makeText(this@AddVehicleActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AddVehicleActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    // Trả lại nút bấm
                    btnSaveVehicle.text = "Lưu"
                    btnSaveVehicle.isEnabled = true
                }
            }
        }
    }
}