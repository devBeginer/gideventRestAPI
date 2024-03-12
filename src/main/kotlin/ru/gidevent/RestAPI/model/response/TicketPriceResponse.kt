package ru.gidevent.RestAPI.model.response

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.CustomerCategory

data class TicketPriceResponse(
        val priceId: Long,
        val customerCategoryId: Long,
        val name: String,
        val price: Int
)