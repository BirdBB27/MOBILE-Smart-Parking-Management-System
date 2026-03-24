package com.seds.smartparkingapp

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    // Tạo một file ẩn tên là SmartParkingPrefs để lưu dữ liệu
    private val prefs: SharedPreferences = context.getSharedPreferences("SmartParkingPrefs", Context.MODE_PRIVATE)

    // Hàm cất Token
    fun saveToken(token: String) {
        prefs.edit().putString("USER_TOKEN", token).apply()
    }

    // Hàm lấy Token ra dùng
    fun getToken(): String? {
        return prefs.getString("USER_TOKEN", null)
    }

    // Hàm xóa Token (Dùng khi Đăng xuất)
    fun clearToken() {
        prefs.edit().remove("USER_TOKEN").apply()
    }
}