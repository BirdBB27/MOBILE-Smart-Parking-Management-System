package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Xử lý nút "Đăng ký ngay"
        val tvRegisterNow = findViewById<TextView>(R.id.tvRegisterNow)
        tvRegisterNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 2. Xử lý nút "Đăng nhập" -> Vào thẳng Trang chủ (Test UI)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // Gọi lệnh finish() để đóng luôn màn hình Đăng nhập.
            // Như vậy khi ở Trang chủ bấm nút "Back" trên điện thoại, nó sẽ thoát app chứ không quay lại màn hình Login nữa.
            finish()
        }
    }
}