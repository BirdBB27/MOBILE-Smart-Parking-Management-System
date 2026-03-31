package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class TopUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val btnConfirmTopUp = findViewById<Button>(R.id.btnConfirmTopUp)
        val tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)

        // Các nút bấm nhanh số tiền
        val btn50k = findViewById<TextView>(R.id.btn50k)
        val btn100k = findViewById<TextView>(R.id.btn100k)
        val btn200k = findViewById<TextView>(R.id.btn200k)
        val btn500k = findViewById<TextView>(R.id.btn500k)

        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

        // Hàm điền nhanh số tiền
        fun setAmount(amount: Int) {
            etAmount.setText(amount.toString())
        }

        // Bắt sự kiện click các nút nạp nhanh
        btn50k.setOnClickListener { setAmount(50000) }
        btn100k.setOnClickListener { setAmount(100000) }
        btn200k.setOnClickListener { setAmount(200000) }
        btn500k.setOnClickListener { setAmount(500000) }

        // Lắng nghe sự thay đổi của ô nhập tiền để cập nhật chữ "Tổng thanh toán"
        etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val amountStr = s.toString().trim()
                val amount = amountStr.toIntOrNull() ?: 0
                tvTotalAmount.text = format.format(amount)
            }
        })

        btnBack.setOnClickListener { finish() }

        // Xử lý gọi API Nạp tiền
        btnConfirmTopUp.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            val amount = amountStr.toIntOrNull()

            if (amount == null || amount < 10000) {
                Toast.makeText(this, "Số tiền tối thiểu là 10.000đ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnConfirmTopUp.text = "Đang xử lý..."
            btnConfirmTopUp.isEnabled = false

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@TopUpActivity)
                    val request = TopUpRequest(amount)
                    val response = api.topUpWallet(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@TopUpActivity, "Nạp thành công ${format.format(amount)}!", Toast.LENGTH_LONG).show()

                        // Nạp xong thì văng về Trang chủ để load lại số dư
                        val intent = Intent(this@TopUpActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi nạp tiền!"
                        Toast.makeText(this@TopUpActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@TopUpActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                } finally {
                    btnConfirmTopUp.text = "Xác nhận nạp tiền"
                    btnConfirmTopUp.isEnabled = true
                }
            }
        }
    }
}