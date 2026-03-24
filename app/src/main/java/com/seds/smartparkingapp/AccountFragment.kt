package com.seds.smartparkingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Tìm nút Quản lý phương tiện và gắn sự kiện
        val btnManageVehicle = view.findViewById<View>(R.id.btnManageVehicle)
        btnManageVehicle?.setOnClickListener {
            val intent = Intent(activity, VehicleManagementActivity::class.java)
            startActivity(intent)
        }

        // Tìm nút Phương thức thanh toán và chuyển trang
        val btnPaymentMethod = view.findViewById<View>(R.id.btnPaymentMethod)
        btnPaymentMethod?.setOnClickListener {
            val intent = Intent(activity, PaymentMethodActivity::class.java)
            startActivity(intent)
        }

        val btnAppSettings = view.findViewById<View>(R.id.btnAppSettings)
        btnAppSettings?.setOnClickListener {
            val intent = Intent(activity, AppSettingsActivity::class.java)
            startActivity(intent)
        }

        val btnEditProfile = view.findViewById<View>(R.id.btnEditProfile)
        btnEditProfile?.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val btnChangePassword = view.findViewById<View>(R.id.btnChangePassword)
        btnChangePassword?.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // NÚT ĐĂNG XUẤT (Đã cập nhật tính năng xóa Token)
        val btnLogout = view.findViewById<View>(R.id.btnLogout)
        btnLogout?.setOnClickListener {
            // Xóa Token khỏi bộ nhớ để không tự động đăng nhập lại
            val tokenManager = TokenManager(requireContext())
            tokenManager.clearToken()

            // Đẩy về màn hình Đăng nhập (MainActivity)
            val intent = Intent(activity, MainActivity::class.java)

            // Xóa lịch sử màn hình
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}