package com.seds.smartparkingapp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Gọi lệnh POST tới endpoint /auth/login
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @GET("users/profile")
    suspend fun getUserProfile(): Response<UserProfileResponse>
    @GET("parking/slots")
    suspend fun getParkingSlots(): Response<ParkingSlotResponse>
    // API Đặt chỗ trước
    @POST("parking/book")
    suspend fun bookParkingSlot(@Body request: BookingRequest): Response<BookingResponse>
}