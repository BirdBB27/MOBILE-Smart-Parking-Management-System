package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class VehicleManagementActivity : AppCompatActivity() {

    private lateinit var rvVehicles: RecyclerView
    private lateinit var adapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_management)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnAddVehicle = findViewById<LinearLayout>(R.id.btnAddVehicle)

        rvVehicles = findViewById(R.id.rvVehicles)
        rvVehicles.layoutManager = LinearLayoutManager(this)

        // Khởi tạo Adapter rỗng lúc ban đầu
        adapter = VehicleAdapter(emptyList())
        rvVehicles.adapter = adapter

        btnBack.setOnClickListener { finish() }

        // Mở màn hình Thêm xe
        btnAddVehicle.setOnClickListener {
            val intent = Intent(this, AddVehicleActivity::class.java)
            startActivity(intent)
        }
    }

    // Dùng onResume để tự động cập nhật danh sách mỗi khi quay lại màn hình này
    override fun onResume() {
        super.onResume()
        loadVehiclesFromApi()
    }

    private fun loadVehiclesFromApi() {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(this@VehicleManagementActivity)
                val response = api.getVehicles()

                if (response.isSuccessful && response.body() != null) {
                    val vehicleList = response.body()!!.data
                    // Đổ dữ liệu mới vào Adapter
                    adapter.updateData(vehicleList)
                } else {
                    Toast.makeText(this@VehicleManagementActivity, "Không thể tải danh sách xe", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@VehicleManagementActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
            }
        }
    }
}