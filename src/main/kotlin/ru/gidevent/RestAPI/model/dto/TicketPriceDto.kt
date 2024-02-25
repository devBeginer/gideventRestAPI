package ru.gidevent.RestAPI.model.dto

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.CustomerCategory

data class TicketPriceDto(
        val priceId: Long = 0,
        val advertisementId: Long,
        val customerCategoryId: Long,
        val name: String,
        val price: Int
)
