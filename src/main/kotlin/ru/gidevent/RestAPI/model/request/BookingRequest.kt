package ru.gidevent.RestAPI.model.request

import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import ru.gidevent.RestAPI.auth.User
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Booking
import ru.gidevent.RestAPI.model.db.CustomerCategory
import ru.gidevent.RestAPI.model.db.EventTime
import java.util.*

data class BookingRequest(
        val id: Long=0,
        val eventTime: Long,
        val advertisement: Long,
        val date: Long,
        val totalPrice: Int,
        val idApproved: Boolean,
        val groups: List<GroupRequest>
)
