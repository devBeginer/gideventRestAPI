package ru.gidevent.RestAPI.model.response

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.User
import java.util.Calendar

data class BookingResponse(
        val id: Long,
        val eventTime: Long,
        val user: Long,
        val advertisement: Long,
        val bookingTime: Long,
        val date: Long,
        val totalPrice: Int,
        val idApproved: Boolean
)
