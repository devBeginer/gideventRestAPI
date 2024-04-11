package ru.gidevent.RestAPI.model.request

data class BookingRequest(
        val id: Long=0,
        val eventTime: Long,
        val advertisement: Long,
        val date: Long,
        val totalPrice: Int,
        val isApproved: Boolean,
        val groups: List<GroupRequest>
)
