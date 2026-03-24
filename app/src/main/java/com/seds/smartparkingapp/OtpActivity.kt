package com.seds.smartparkingapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class OtpActivity : AppCompatActivity() {

    // Khai báo biến đếm ngược
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        val btnBack = findViewById<View>(R.id.btnBack)
        btnBack?.setOnClickListener { finish() }

        // 1. Chuyển vào Trang chủ khi bấm Xác nhận
        val btnConfirmOTP = findViewById<View>(R.id.btnConfirmOTP)
        btnConfirmOTP?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // 2. Xử lý logic Gửi lại mã OTP
        val tvResendOTP = findViewById<TextView>(R.id.tvResendOTP)
        val colorBlue = ContextCompat.getColor(this, R.color.primary_blue)
        val colorGray = ContextCompat.getColor(this, R.color.text_gray)

        // Hàm bắt đầu đếm ngược 60 giây
        fun startCountdown() {
            tvResendOTP.isEnabled = false // Khóa nút không cho bấm

            countDownTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Cập nhật text mỗi giây
                    val secondsLeft = millisUntilFinished / 1000
                    tvResendOTP.text = "Gửi lại mã sau ${secondsLeft}s"
                    tvResendOTP.setTextColor(colorGray)
                }

                override fun onFinish() {
                    // Đếm xong thì mở khóa cho bấm lại
                    tvResendOTP.isEnabled = true
                    tvResendOTP.text = "Gửi lại mã"
                    tvResendOTP.setTextColor(colorBlue)
                }
            }.start()
        }

        // Tự động chạy đếm ngược khi vừa mở màn hình OTP
        startCountdown()

        // Khi người dùng bấm "Gửi lại mã" (sau khi đã hết 60s)
        tvResendOTP.setOnClickListener {
            Toast.makeText(this, "Đã gửi mã OTP mới đến số điện thoại của bạn!", Toast.LENGTH_SHORT).show()
            startCountdown() // Bấm xong thì đếm ngược lại từ đầu
        }
    }

    // Nhớ hủy bộ đếm khi thoát màn hình để tránh lỗi tràn bộ nhớ
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}