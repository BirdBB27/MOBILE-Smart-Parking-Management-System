package com.seds.smartparkingapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class ActivityFragment : Fragment() {

    private lateinit var adapter: ActivityAdapter
    private var isParkingTabActive = true // Mặc định là Tab Lịch sử đỗ xe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        val cvTabHistory = view.findViewById<MaterialCardView>(R.id.cvTabHistory)
        val tvTabHistory = view.findViewById<TextView>(R.id.tvTabHistory)
        val cvTabWallet = view.findViewById<MaterialCardView>(R.id.cvTabWallet)
        val tvTabWallet = view.findViewById<TextView>(R.id.tvTabWallet)

        // Bạn cần thêm một RecyclerView vào fragment_activity.xml với ID là rvActivity
        val rvActivity = view.findViewById<RecyclerView>(R.id.rvActivity)
        rvActivity.layoutManager = LinearLayoutManager(requireContext())
        adapter = ActivityAdapter(emptyList())
        rvActivity.adapter = adapter

        val colorBlack = ContextCompat.getColor(requireContext(), R.color.black)
        val colorGray = ContextCompat.getColor(requireContext(), R.color.text_gray)
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        val colorTransparent = Color.TRANSPARENT

        // Hàm thay đổi trạng thái Tab UI
        fun updateTabUI(isParking: Boolean) {
            isParkingTabActive = isParking
            if (isParking) {
                cvTabHistory.setCardBackgroundColor(colorWhite)
                cvTabHistory.cardElevation = 4f
                tvTabHistory.setTextColor(colorBlack)
                tvTabHistory.setTypeface(null, Typeface.BOLD)

                cvTabWallet.setCardBackgroundColor(colorTransparent)
                cvTabWallet.cardElevation = 0f
                tvTabWallet.setTextColor(colorGray)
                tvTabWallet.setTypeface(null, Typeface.NORMAL)
            } else {
                cvTabWallet.setCardBackgroundColor(colorWhite)
                cvTabWallet.cardElevation = 4f
                tvTabWallet.setTextColor(colorBlack)
                tvTabWallet.setTypeface(null, Typeface.BOLD)

                cvTabHistory.setCardBackgroundColor(colorTransparent)
                cvTabHistory.cardElevation = 0f
                tvTabHistory.setTextColor(colorGray)
                tvTabHistory.setTypeface(null, Typeface.NORMAL)
            }
            fetchDataFromServer(null)
        }

        cvTabHistory.setOnClickListener { updateTabUI(true) }
        cvTabWallet.setOnClickListener { updateTabUI(false) }

        swipeRefreshLayout.setOnRefreshListener {
            fetchDataFromServer(swipeRefreshLayout)
        }

        // Tải dữ liệu lần đầu
        fetchDataFromServer(null)

        return view
    }

    private fun fetchDataFromServer(swipeLayout: SwipeRefreshLayout?) {
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getInstance(requireContext())
                if (isParkingTabActive) {
                    val response = api.getHistory()
                    if (response.isSuccessful) adapter.updateData(response.body()?.data ?: emptyList())
                } else {
                    val response = api.getTransactions()
                    if (response.isSuccessful) {
                        val transactions = response.body()?.data ?: emptyList()
                        // Map TransactionItem sang HistoryItem để hiển thị
                        val mapped = transactions.map {
                            HistoryItem(it.transaction_id, it.type, it.description, it.amount, it.created_at, null, "Thành công")
                        }
                        adapter.updateData(mapped)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi cập nhật dữ liệu", Toast.LENGTH_SHORT).show()
            } finally {
                swipeLayout?.isRefreshing = false
            }
        }
    }
}