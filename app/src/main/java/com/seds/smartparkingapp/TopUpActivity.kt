package com.seds.smartparkingapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class TopUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        // 1. Xử lý nút Back quay lại
        val btnBack = findViewById<View>(R.id.btnBack)
        btnBack?.setOnClickListener {
            finish()
        }

        // 2. Ánh xạ các nút mệnh giá và ô nhập tiền
        val btn50k = findViewById<TextView>(R.id.btn50k)
        val btn100k = findViewById<TextView>(R.id.btn100k)
        val btn200k = findViewById<TextView>(R.id.btn200k)
        val btn500k = findViewById<TextView>(R.id.btn500k)
        val etAmount = findViewById<TextInputEditText>(R.id.etAmount)

        // Gom các nút vào danh sách để dễ xử lý
        val buttons = listOf(btn50k, btn100k, btn200k, btn500k)
        val amounts = listOf("50000", "100000", "200000", "500000") // Số tiền tương ứng

        // Màu sắc
        val colorBlue = ContextCompat.getColor(this, R.color.primary_blue)
        val colorGray = ContextCompat.getColor(this, R.color.text_gray)
        val colorWhite = Color.WHITE
        val bgGrayLight = ColorStateList.valueOf(Color.parseColor("#F5F5F5"))
        val bgBlue = ColorStateList.valueOf(colorBlue)

        // Duyệt qua từng nút và gắn sự kiện click
        for (i in buttons.indices) {
            buttons[i]?.setOnClickListener {

                // Bước A: Reset TẤT CẢ các nút về trạng thái xám (Chưa chọn)
                for (btn in buttons) {
                    btn?.backgroundTintList = bgGrayLight
                    btn?.setTextColor(colorGray)
                }

                // Bước B: Đổi màu nút VỪA BẤM sang trạng thái xanh (Đã chọn)
                buttons[i]?.backgroundTintList = bgBlue
                buttons[i]?.setTextColor(colorWhite)

                // Bước C: Điền số tiền tương ứng vào ô nhập Text
                etAmount?.setText(amounts[i])
            }
        }
    }
}