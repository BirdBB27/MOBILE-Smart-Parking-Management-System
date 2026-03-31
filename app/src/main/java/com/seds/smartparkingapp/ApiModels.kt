package com.seds.smartparkingapp

// ==========================
// AUTH (ĐĂNG NHẬP / ĐĂNG KÝ)
// ==========================
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String?,
    val message: String?,
    val user: UserInfo?
)

data class UserInfo(
    val id: Int,
    val full_name: String,
    val email: String,
    val phone_number: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)

data class RegisterResponse(
    val message: String?
)

// ==========================
// USER (PROFILE / VÍ TIỀN)
// ==========================
data class ProfileResponse(
    val message: String?,
    val data: UserProfileData
)

data class UserProfileData(
    val id: Int,
    val name: String,
    val phone: String,
    val role: String,
    val balance: Int
)

data class TopUpRequest(
    val amount: Int
)

data class TopUpResponse(
    val message: String?,
    val wallet_balance: Int?
)

// ==========================
// BÃI ĐỖ & ĐẶT CHỖ
// ==========================
data class ParkingSlotResponse(
    val message: String?,
    val total_slots: Int?,
    val data: List<ParkingSlot>
)

data class ParkingSlot(
    val slot_id: String,
    val zone: String,
    val slot_type: String,
    val status: String
)

data class BookingRequest(
    val slot_id: String,
    val license_plate: String,
    val start_time: String, // Giờ bắt đầu (MỚI)
    val end_time: String,   // Giờ kết thúc (MỚI)
    val amount: Int         // Số tiền cọc để BE lưu lịch sử (MỚI)
)

data class BookingResponse(
    val message: String?,
    val booking_id: Int?,
    val status: String?
)

// 1. Dùng cho API Thêm xe
data class AddVehicleRequest(
    val license_plate: String
)

data class AddVehicleResponse(
    val message: String?
)

// 2. Dùng cho API Lấy danh sách xe
data class VehicleListResponse(
    val message: String?,
    val data: List<Vehicle>
)

data class Vehicle(
    val license_plate: String,
    val vehicle_type: String
)
data class MonthlyTicketRequest(
    val license_plate: String,
    val months: Int,
    val amount: Int
)

data class MonthlyTicketResponse(
    val message: String?
)
data class MyTicketResponse(
    val has_ticket: Boolean,
    val message: String?
)
data class HistoryResponse(
    val message: String?,
    val data: List<HistoryItem>
)

data class HistoryItem(
    val id: Int,
    val type: String,      // "TOP_UP", "BOOKING", "MONTHLY_TICKET"
    val title: String,     // VD: "Vincom Đồng Khởi", "Nạp tiền ví SmartPay"
    val amount: Int?,      // Số tiền
    val timestamp: String, // VD: "01/03/2026 • 09:30"
    val license_plate: String?, // Biển số xe (nếu có)
    val status: String     // VD: "Hoàn thành", "Đã hủy"
)
data class UpdateProfileRequest(
    val name: String,
    val phone: String
)

data class UpdateProfileResponse(
    val message: String?
)
data class TransactionResponse(
    val message: String?,
    val data: List<TransactionItem>
)

data class TransactionItem(
    val transaction_id: Int,
    val user_id: Int,
    val amount: Int,
    val type: String, // Trả về "PAYMENT" hoặc "TOPUP"
    val description: String,
    val created_at: String // VD: "2026-03-25T08:30:00.000Z"
)
data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String
)

data class ChangePasswordResponse(
    val message: String?
)
// Dữ liệu gửi lên để đặt chỗ
data class ActiveBookingResponse(
    val has_active: Boolean,
    val message: String?,
    val data: ActiveBookingData?
)

data class ActiveBookingData(
    val slot_id: String,
    val license_plate: String,
    val start_time: String,
    val end_time: String
)