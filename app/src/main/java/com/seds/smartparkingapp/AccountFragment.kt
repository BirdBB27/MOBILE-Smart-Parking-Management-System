package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private lateinit var tvAccountName: TextView
    private lateinit var tvAccountPhone: TextView
    private lateinit var tvAvatarLetter: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Ánh xạ View của Thẻ thông tin
        tvAccountName = view.findViewById(R.id.tvAccountName)
        tvAccountPhone = view.findViewById(R.id.tvAccountPhone)
        tvAvatarLetter = view.findViewById(R.id.tvAvatarLetter)

        // Các nút chuyển trang
        view.findViewById<View>(R.id.btnManageVehicle)?.setOnClickListener {
            startActivity(Intent(activity, VehicleManagementActivity::class.java))
        }
        view.findViewById<View>(R.id.btnPaymentMethod)?.setOnClickListener {
            startActivity(Intent(activity, PaymentMethodActivity::class.java))
        }
        view.findViewById<View>(R.id.btnAppSettings)?.setOnClickListener {
            startActivity(Intent(activity, AppSettingsActivity::class.java))
        }
        view.findViewById<View>(R.id.btnEditProfile)?.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        view.findViewById<View>(R.id.btnChangePassword)?.setOnClickListener {
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
        }

        // Đăng xuất
        view.findViewById<View>(R.id.btnLogout)?.setOnClickListener {
            val tokenManager = TokenManager(requireContext())
            tokenManager.clearToken()
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    // Mỗi khi vào lại tab Account, tự động tải lại thông tin
    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(requireContext())
                val response = api.getUserProfile()
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.data

                    tvAccountName.text = user.name
                    tvAccountPhone.text = user.phone

                    // Lấy chữ cái đầu tiên của Tên làm Avatar
                    if (user.name.isNotEmpty()) {
                        tvAvatarLetter.text = user.name.substring(0, 1).uppercase()
                    }
                }
            } catch (e: Exception) {
                // Không báo lỗi to nếu mạng giật
            }
        }
    }
}