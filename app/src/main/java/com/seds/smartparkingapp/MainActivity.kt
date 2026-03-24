package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Chuyển sang Đăng ký
        val tvRegisterNow = findViewById<TextView>(R.id.tvRegisterNow)
        tvRegisterNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 2. Ánh xạ các thành phần Đăng nhập
        val etPhone = findViewById<TextInputEditText>(R.id.etPhone) // Dùng ID etPhone theo giao diện, nhưng lấy dữ liệu Email
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etPhone.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Kiểm tra rỗng
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Đổi chữ nút thành Đang xử lý để người dùng biết
            btnLogin.text = "Đang xử lý..."
            btnLogin.isEnabled = false

            // 3. Gọi API Đăng nhập bằng Coroutines (chạy ngầm không làm đơ app)
            lifecycleScope.launch {
                try {
                    // Tạo gói dữ liệu yêu cầu
                    val request = LoginRequest(email, password)

                    // KẾT NỐI API: Truyền Context (this@MainActivity) vào RetrofitClient
                    val api = RetrofitClient.getInstance(this@MainActivity)
                    val response = api.login(request)

                    // Phục hồi lại nút
                    btnLogin.text = "Đăng nhập"
                    btnLogin.isEnabled = true

                    if (response.isSuccessful) {
                        val token = response.body()?.token

                        if (token != null) {
                            // BẮT ĐẦU LƯU TOKEN
                            val tokenManager = TokenManager(this@MainActivity)
                            tokenManager.saveToken(token)
                            // KẾT THÚC LƯU TOKEN

                            Toast.makeText(this@MainActivity, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                            // Chuyển thẳng vào HomeActivity
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Lỗi: Không lấy được Token", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Trả về mã lỗi 400, 401...
                        Toast.makeText(this@MainActivity, "Sai Email hoặc mật khẩu!", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    // Lỗi mạng hoặc server
                    btnLogin.text = "Đăng nhập"
                    btnLogin.isEnabled = true
                    Toast.makeText(this@MainActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}