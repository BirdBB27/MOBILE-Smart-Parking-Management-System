package com.seds.smartparkingapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView

class ActivityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // 1. XỬ LÝ VUỐT ĐỂ LÀM MỚI (SWIPE TO REFRESH)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 1500)
        }

        // 2. XỬ LÝ CHUYỂN TAB (TOGGLE)
        val cvTabHistory = view.findViewById<MaterialCardView>(R.id.cvTabHistory)
        val tvTabHistory = view.findViewById<TextView>(R.id.tvTabHistory)

        val cvTabWallet = view.findViewById<MaterialCardView>(R.id.cvTabWallet)
        val tvTabWallet = view.findViewById<TextView>(R.id.tvTabWallet)

        // Khai báo màu
        val colorBlack = ContextCompat.getColor(requireContext(), R.color.black)
        val colorGray = ContextCompat.getColor(requireContext(), R.color.text_gray)
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        val colorTransparent = Color.TRANSPARENT

        // Khi bấm vào "Lịch sử đỗ xe"
        cvTabHistory.setOnClickListener {
            // Bật Tab Lịch sử (Nền trắng, có bóng mờ, chữ đen in đậm)
            cvTabHistory.setCardBackgroundColor(colorWhite)
            cvTabHistory.cardElevation = 4f
            tvTabHistory.setTextColor(colorBlack)
            tvTabHistory.setTypeface(null, Typeface.BOLD)

            // Tắt Tab Ví (Mất nền, mất bóng, chữ xám bình thường)
            cvTabWallet.setCardBackgroundColor(colorTransparent)
            cvTabWallet.cardElevation = 0f
            tvTabWallet.setTextColor(colorGray)
            tvTabWallet.setTypeface(null, Typeface.NORMAL)
        }

        // Khi bấm vào "Giao dịch ví"
        cvTabWallet.setOnClickListener {
            // Bật Tab Ví
            cvTabWallet.setCardBackgroundColor(colorWhite)
            cvTabWallet.cardElevation = 4f
            tvTabWallet.setTextColor(colorBlack)
            tvTabWallet.setTypeface(null, Typeface.BOLD)

            // Tắt Tab Lịch sử
            cvTabHistory.setCardBackgroundColor(colorTransparent)
            cvTabHistory.cardElevation = 0f
            tvTabHistory.setTextColor(colorGray)
            tvTabHistory.setTypeface(null, Typeface.NORMAL)
        }

        return view
    }
}