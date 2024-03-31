package ru.gidevent.RestAPI.model.response

import ru.gidevent.RestAPI.model.db.TicketPrice


data class BookingParamsResponse(
        val eventTimeList: List<EventTimeWithCountResponse>,
        val price: List<TicketPrice>
)

