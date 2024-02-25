package ru.gidevent.RestAPI.model.dto

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.*


data class AdvertisementWithPrice(
        val id: Long,
        val name: String,
        val duration: Int,
        val description: String,
        val transportation: TransportationVariant,
        val ageRestrictions: Int,
        val visitorsCount: Int,
        val isIndividual: Boolean,
        val photos: String,
        val rating: Int,
        val category: Category,
        val city: City,
        val seller: Seller,
        val price: List<TicketPrice>
)
