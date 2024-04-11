package ru.gidevent.RestAPI.model.response

data class SellerBookingResponse(
        val id: Long,
        val eventTime: Long,
        val advertisement: String,
        val date: Long,
        val totalPrice: Int,
        val isApproved: Boolean,
        val customerCount: Int
)
