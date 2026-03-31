package com.seds.smartparkingapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etOldPassword = findViewById<EditText>(R.id.etOldPassword)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSavePassword = findViewById<Button>(R.id.btnSavePassword)

        btnBack.setOnClickListener { finish() }

        btnSavePassword.setOnClickListener {
            val oldPass = etOldPassword.text.toString()
            val newPass = etNewPassword.text.toString()
            val confirmPass = etConfirmPassword.text.toString()

            // 1. Kiểm tra nhập thiếu
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Kiểm tra độ dài
            if (newPass.length < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Kiểm tra khớp mật khẩu
            if (newPass != confirmPass) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSavePassword.text = "Đang xử lý..."
            btnSavePassword.isEnabled = false

            // Gọi API
            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@ChangePasswordActivity)
                    val request = ChangePasswordRequest(oldPass, newPass)
                    val response = api.changePassword(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@ChangePasswordActivity, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show()
                        finish() // Văng ra ngoài Tab Account
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Mật khẩu hiện tại không đúng!"
                        Toast.makeText(this@ChangePasswordActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ChangePasswordActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                } finally {
                    btnSavePassword.text = "Xác nhận đổi mật khẩu"
                    btnSavePassword.isEnabled = true
                }
            }
        }
    }
}