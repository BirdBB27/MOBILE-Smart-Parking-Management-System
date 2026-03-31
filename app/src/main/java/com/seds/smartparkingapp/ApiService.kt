package com.seds.smartparkingapp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("users/profile")
    suspend fun getUserProfile(): Response<ProfileResponse>

    @PUT("users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    @PUT("users/password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("users/topup")
    suspend fun topUpWallet(@Body request: TopUpRequest): Response<TopUpResponse>

    @GET("users/history")
    suspend fun getHistory(): Response<HistoryResponse>

    @GET("users/transactions")
    suspend fun getTransactions(): Response<TransactionResponse>

    @GET("parking/slots")
    suspend fun getParkingSlots(): Response<ParkingSlotResponse>

    @POST("parking/book")
    suspend fun bookParkingSlot(@Body request: BookingRequest): Response<BookingResponse>

    @POST("vehicles")
    suspend fun addVehicle(@Body request: AddVehicleRequest): Response<AddVehicleResponse>

    @GET("vehicles")
    suspend fun getVehicles(): Response<VehicleListResponse>

    // ==========================================
    // NHÓM API VÉ THÁNG
    // ==========================================
    // 1. Kiểm tra vé tháng
    @GET("tickets/my-ticket")
    suspend fun getMyTicket(): Response<MyTicketResponse>

    // 2. Mua vé tháng mới
    @POST("tickets/monthly-ticket")
    suspend fun registerMonthlyTicket(@Body request: MonthlyTicketRequest): Response<MonthlyTicketResponse>
    // API Lấy thông tin phiên đặt chỗ đang hoạt động
    @GET("parking/active-booking")
    suspend fun getActiveBooking(): Response<ActiveBookingResponse>
}