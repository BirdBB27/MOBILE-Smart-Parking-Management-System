package com.seds.smartparkingapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class QrScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val ivQrCode = findViewById<ImageView>(R.id.ivQrCode)
        val tvQrContent = findViewById<TextView>(R.id.tvQrContent)

        // Xử lý nút quay lại
        btnBack.setOnClickListener {
            finish()
        }

        // Tạo nội dung cho mã QR (Thực tế bạn có thể lấy Token hoặc UserID từ SharedPreferences để ghép vào đây)
        // Tạm thời mình lấy một chuỗi cố định kèm Timestamp để test
        val currentTime = System.currentTimeMillis()
        val qrContentString = "SMART_PARKING_USER_VALID_$currentTime"

        tvQrContent.text = qrContentString

        // Chuyển đổi chuỗi chữ thành hình ảnh QR Code
        try {
            val barcodeEncoder = BarcodeEncoder()
            // Tạo ảnh Bitmap QR code kích thước 800x800
            val bitmap = barcodeEncoder.encodeBitmap(qrContentString, BarcodeFormat.QR_CODE, 800, 800)

            // Gắn ảnh vừa tạo vào giao diện
            ivQrCode.setImageBitmap(bitmap)

        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khi tạo mã QR: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}