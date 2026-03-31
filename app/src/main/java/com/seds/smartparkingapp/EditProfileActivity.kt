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

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tvAvatarLetter = findViewById<TextView>(R.id.tvAvatarLetter)
        val etEditName = findViewById<EditText>(R.id.etEditName)
        val etEditPhone = findViewById<EditText>(R.id.etEditPhone)
        val etEditEmail = findViewById<EditText>(R.id.etEditEmail)
        val btnSaveProfile = findViewById<Button>(R.id.btnSaveProfile)

        btnBack.setOnClickListener { finish() }

        // Mới vào trang, gọi API để điền sẵn Tên, SĐT, Email cũ lên
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(this@EditProfileActivity)
                val response = api.getUserProfile()
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.data

                    etEditName.setText(user.name)
                    etEditPhone.setText(user.phone)
                    // Nếu BE chưa trả về email trong API Profile thì dòng này có thể để trống
                    // Tạm thời mình cứ set trước, nếu có thì hiện, không thì thôi.

                    if (user.name.isNotEmpty()) {
                        tvAvatarLetter.text = user.name.substring(0, 1).uppercase()
                    }
                }
            } catch (e: Exception) {
                // Ignore lỗi mạng để không làm phiền người dùng
            }
        }

        // Xử lý khi bấm nút "Lưu thay đổi"
        btnSaveProfile.setOnClickListener {
            val newName = etEditName.text.toString().trim()
            val newEmail = etEditEmail.text.toString().trim() // Theo UI của bạn, mình cho phép đổi Tên và Email

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ Tên và Email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSaveProfile.text = "Đang lưu..."
            btnSaveProfile.isEnabled = false

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@EditProfileActivity)

                    // GỌI API CẬP NHẬT Ở ĐÂY
                    // Nhớ khai báo UpdateProfileRequest(val name: String, val email: String) trong ApiModels nhé
                    val request = UpdateProfileRequest(newName, newEmail)
                    val response = api.updateProfile(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfileActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                        finish() // Đóng trang, văng ra ngoài tab Account sẽ tự load lại
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi cập nhật! Vui lòng thử lại."
                        Toast.makeText(this@EditProfileActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfileActivity, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show()
                } finally {
                    btnSaveProfile.text = "Lưu thay đổi"
                    btnSaveProfile.isEnabled = true
                }
            }
        }
    }
}