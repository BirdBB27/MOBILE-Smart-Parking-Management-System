package com.seds.smartparkingapp

// 1. Dữ liệu Mobile gửi lên Server lúc Login (Theo tài liệu mới: Dùng Email)
data class LoginRequest(
    val email: String,
    val password: String
)

// 2. Dữ liệu Server trả về cho Mobile sau khi Login thành công
data class LoginResponse(
    val token: String?,
    val message: String?,
    val user: UserInfo?
)

// Thông tin User trả về
data class UserInfo(
    val id: Int,
    val full_name: String,
    val email: String,
    val phone_number: String
)
data class UserProfileResponse(
    val id: Int,
    val full_name: String,
    val email: String,
    val phone_number: String,
    val wallet_balance: Int
)
data class ParkingSlotResponse(
    val message: String?,
    val total_slots: Int?,
    val data: List<ParkingSlot>
)

// Dữ liệu chi tiết của 1 ô đỗ xe
data class ParkingSlot(
    val slot_id: String,
    val zone: String,
    val slot_type: String,
    val status: String
)
// Dữ liệu gửi lên để đặt chỗ
data class BookingRequest(
    val slot_id: String,
    val license_plate: String
)

// Dữ liệu nhận về sau khi đặt thành công
data class BookingResponse(
    val message: String?,
    val booking_id: Int?,
    val status: String?
)