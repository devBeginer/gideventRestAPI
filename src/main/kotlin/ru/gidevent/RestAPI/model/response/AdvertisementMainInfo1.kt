package ru.gidevent.RestAPI.model.response

import ru.gidevent.RestAPI.model.db.Category
import ru.gidevent.RestAPI.model.db.City
import ru.gidevent.RestAPI.model.db.TicketPrice
import ru.gidevent.RestAPI.model.db.TransportationVariant
import ru.gidevent.RestAPI.model.dto.AddInfoSeller
import ru.gidevent.RestAPI.model.dto.TicketPriceDto


data class AdvertisementMainInfo1(
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
        val seller: AddInfoSeller,
        val price: List<TicketPriceResponse>
)
