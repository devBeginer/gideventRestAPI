package ru.gidevent.RestAPI.model.dto

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.CustomerCategory

data class TicketPriceDto(
        val priceId: Long = 0,
        val advertisement: AdvertisementWithFavourite,
        val customerCategory: CustomerCategory,
        val price: Int
)
