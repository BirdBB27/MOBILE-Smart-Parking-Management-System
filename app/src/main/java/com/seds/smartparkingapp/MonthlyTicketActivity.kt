package com.seds.smartparkingapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MonthlyTicketActivity : AppCompatActivity() {

    private var selectedMonths = 1
    private var totalPrice = 150000
    private lateinit var actvLicensePlate: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_ticket)

        // 1. Ánh xạ View
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val cvActiveVIP = findViewById<MaterialCardView>(R.id.cvActiveVIP)
        val llBuyTicket = findViewById<LinearLayout>(R.id.llBuyTicket)
        val bottomConfirmBar = findViewById<LinearLayout>(R.id.bottom_confirm_bar)

        actvLicensePlate = findViewById(R.id.actvLicensePlate)
        val tvTicketTotalAmount = findViewById<TextView>(R.id.tvTicketTotalAmount)
        val btnConfirmTicket = findViewById<Button>(R.id.btnConfirmTicket)

        val cardPackage1 = findViewById<MaterialCardView>(R.id.cardPackage1)
        val cardPackage3 = findViewById<MaterialCardView>(R.id.cardPackage3)
        val cardPackage6 = findViewById<MaterialCardView>(R.id.cardPackage6)

        val ivCheck1 = findViewById<ImageView>(R.id.ivCheck1)
        val ivCheck3 = findViewById<ImageView>(R.id.ivCheck3)
        val ivCheck6 = findViewById<ImageView>(R.id.ivCheck6)

        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        btnBack?.setOnClickListener { finish() }

        // --- CÁC HÀM ẨN/HIỆN GIAO DIỆN CHẮC CHẮN ---
        fun showVIPUI() {
            cvActiveVIP.visibility = View.VISIBLE
            llBuyTicket.visibility = View.GONE
            bottomConfirmBar.visibility = View.GONE
        }

        fun showBuyTicketUI() {
            cvActiveVIP.visibility = View.GONE
            llBuyTicket.visibility = View.VISIBLE
            bottomConfirmBar.visibility = View.VISIBLE
        }

        // Mặc định bật giao diện mua vé trước
        showBuyTicketUI()

        // 2. TẢI DỮ LIỆU TỪ SERVER (Bao lỗi)
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(this@MonthlyTicketActivity)

                // Tải danh sách biển số xe
                try {
                    val vehicleRes = api.getVehicles()
                    if (vehicleRes.isSuccessful && vehicleRes.body() != null) {
                        val plates = vehicleRes.body()!!.data.map { it.license_plate }
                        val adapter = ArrayAdapter(this@MonthlyTicketActivity, android.R.layout.simple_dropdown_item_1line, plates)
                        actvLicensePlate.setAdapter(adapter)
                        actvLicensePlate.setOnClickListener { actvLicensePlate.showDropDown() }
                    }
                } catch (e: Exception) { /* Bỏ qua lỗi xe */ }

                // Kiểm tra VIP
                val vipRes = api.getMyTicket()
                if (vipRes.isSuccessful && vipRes.body() != null) {
                    if (vipRes.body()!!.has_ticket) {
                        showVIPUI()
                    } else {
                        showBuyTicketUI()
                    }
                } else {
                    // BE trả về lỗi 404 hoặc 400 (Khách chưa có vé)
                    showBuyTicketUI()
                }
            } catch (e: Exception) {
                // Rớt mạng
                showBuyTicketUI()
            }
        }

        // 3. XỬ LÝ CHỌN GÓI VÀ ĐỔI MÀU (Giữ nguyên)
        fun selectPackage(months: Int, price: Int) {
            selectedMonths = months
            totalPrice = price
            tvTicketTotalAmount.text = format.format(totalPrice)

            val defaultStrokeColor = Color.parseColor("#E0E0E0")
            val activeStrokeColor = Color.parseColor("#FF9800")
            val defaultBgColor = Color.parseColor("#FFFFFF")
            val activeBgColor = Color.parseColor("#FFF3E0")

            cardPackage1.strokeColor = defaultStrokeColor
            cardPackage1.setCardBackgroundColor(defaultBgColor)
            ivCheck1.setImageResource(android.R.drawable.checkbox_off_background)
            ivCheck1.setColorFilter(Color.GRAY)

            cardPackage3.strokeColor = defaultStrokeColor
            cardPackage3.setCardBackgroundColor(defaultBgColor)
            ivCheck3.setImageResource(android.R.drawable.checkbox_off_background)
            ivCheck3.setColorFilter(Color.GRAY)

            cardPackage6.strokeColor = defaultStrokeColor
            cardPackage6.setCardBackgroundColor(defaultBgColor)
            ivCheck6.setImageResource(android.R.drawable.checkbox_off_background)
            ivCheck6.setColorFilter(Color.GRAY)

            when (months) {
                1 -> {
                    cardPackage1.strokeColor = activeStrokeColor
                    cardPackage1.setCardBackgroundColor(activeBgColor)
                    ivCheck1.setImageResource(android.R.drawable.checkbox_on_background)
                    ivCheck1.setColorFilter(activeStrokeColor)
                }
                3 -> {
                    cardPackage3.strokeColor = activeStrokeColor
                    cardPackage3.setCardBackgroundColor(activeBgColor)
                    ivCheck3.setImageResource(android.R.drawable.checkbox_on_background)
                    ivCheck3.setColorFilter(activeStrokeColor)
                }
                6 -> {
                    cardPackage6.strokeColor = activeStrokeColor
                    cardPackage6.setCardBackgroundColor(activeBgColor)
                    ivCheck6.setImageResource(android.R.drawable.checkbox_on_background)
                    ivCheck6.setColorFilter(activeStrokeColor)
                }
            }
        }

        cardPackage1.setOnClickListener { selectPackage(1, 150000) }
        cardPackage3.setOnClickListener { selectPackage(3, 427500) }
        cardPackage6.setOnClickListener { selectPackage(6, 810000) }

        // 4. MUA VÉ
        // 4. MUA VÉ VÀ TRỪ TIỀN
        btnConfirmTicket.setOnClickListener {
            val licensePlate = actvLicensePlate.text.toString().trim()

            // Chặn ngay nếu chưa chọn xe
            if (licensePlate.isEmpty() || licensePlate.contains("Bấm để chọn")) {
                Toast.makeText(this@MonthlyTicketActivity, "⚠️ Vui lòng chọn biển số xe của bạn!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Đổi hiệu ứng nút bấm để người dùng biết App đang chạy
            btnConfirmTicket.text = "Đang xử lý..."
            btnConfirmTicket.isEnabled = false

            lifecycleScope.launch {
                try {
                    val api = RetrofitClient.getInstance(this@MonthlyTicketActivity)

                    // Đóng gói dữ liệu: Gửi Biển số, Số tháng và Cả TỔNG TIỀN lên cho Backend
                    val request = MonthlyTicketRequest(licensePlate, selectedMonths, totalPrice)

                    // Bắn API lên Server
                    val response = api.registerMonthlyTicket(request)

                    if (response.isSuccessful) {
                        Toast.makeText(this@MonthlyTicketActivity, "🎉 Mua vé VIP thành công!", Toast.LENGTH_LONG).show()
                        // Mua xong văng về màn hình chính
                        val intent = Intent(this@MonthlyTicketActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        // Nếu Backend chặn (VD: Hết tiền, lỗi DB...) -> Báo lỗi đỏ lên màn hình
                        val errorMsg = response.errorBody()?.string() ?: "Lỗi từ Server"
                        Toast.makeText(this@MonthlyTicketActivity, "❌ Server từ chối: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    // Nếu rớt mạng hoặc Server sập
                    Toast.makeText(this@MonthlyTicketActivity, "❌ Lỗi mạng: Không thể kết nối!", Toast.LENGTH_LONG).show()
                } finally {
                    // Trả lại trạng thái cho nút bấm dù thành công hay thất bại
                    btnConfirmTicket.text = "Thanh toán & Gia hạn"
                    btnConfirmTicket.isEnabled = true
                }
            }
        }
    }
}