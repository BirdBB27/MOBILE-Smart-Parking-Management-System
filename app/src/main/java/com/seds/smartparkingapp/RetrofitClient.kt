package com.seds.smartparkingapp

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Đường link API (Nhớ giữ nguyên dấu / ở cuối)
    private const val BASE_URL = "https://bionic-ingratiating-josh.ngrok-free.dev/api/"

    // ĐÂY CHÍNH LÀ HÀM getInstance MÀ MAIN ACTIVITY ĐANG TÌM KIẾM
    fun getInstance(context: Context): ApiService {

        // Tạo "Người chặn đường" để tự động nhét Token vào Header
        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            // Lấy Token từ kho lưu trữ
            val tokenManager = TokenManager(context)
            val token = tokenManager.getToken()

            // Nếu lấy được Token thì gắn nó vào Header mang tên "Authorization"
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        // Gắn "Người chặn đường" vào bộ điều hướng mạng OkHttp
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        // Xây dựng Retrofit với cấu hình mạng mới
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}