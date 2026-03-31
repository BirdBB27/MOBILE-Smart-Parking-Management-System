package com.seds.smartparkingapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Ánh xạ các UI
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin) // Đã sửa tên biến cho khớp XML

        // Xử lý nút Back trên cùng
        btnBack?.setOnClickListener {
            finish()
        }

        // Bấm nút Đăng nhập thì quay lại màn hình Login
        tvLogin?.setOnClickListener {
            finish()
        }

        // 2. Xử lý khi bấm nút Đăng ký
        btnRegister?.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Kiểm tra rỗng
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra mật khẩu khớp nhau
            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Bắt đầu gọi API
            btnRegister.text = "Đang xử lý..."
            btnRegister.isEnabled = false

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@RegisterActivity)
                    val request = RegisterRequest(name, email, phone, password)
                    val response = api.register(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show()
                        finish() // Đóng trang Đăng ký, quay về Đăng nhập
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi đăng ký!"
                        Toast.makeText(this@RegisterActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Lỗi kết nối mạng: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    // Trả lại trạng thái cho nút
                    btnRegister.text = "Đăng ký"
                    btnRegister.isEnabled = true
                }
            }
        }
    }
}