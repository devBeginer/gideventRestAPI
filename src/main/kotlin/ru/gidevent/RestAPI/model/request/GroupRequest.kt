package ru.gidevent.RestAPI.model.request

import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Booking
import ru.gidevent.RestAPI.model.db.CustomerCategory
import ru.gidevent.RestAPI.model.db.EventTime

data class GroupRequest (
        val id: Long=0,
        val customerCategory: Long,
        val count: Int,
        val booking: Long
)