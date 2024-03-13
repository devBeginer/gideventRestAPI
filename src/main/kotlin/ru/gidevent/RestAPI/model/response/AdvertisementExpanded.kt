package ru.gidevent.RestAPI.model.response

import com.google.gson.annotations.SerializedName
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.model.dto.TicketPriceDto

data class AdvertisementExpanded(
        val id: Long,
        val name: String,
        val duration: Int,
        val description: String,
        val transportation: TransportationVariant,
        val ageRestrictions: Int,
        val visitorsCount: Int,
        val isIndividual: Boolean,
        val photos: String,
        val rating: Float,
        val category: Category,
        val city: City,
        val favourite: Boolean?,
        val seller: Seller,
        val priceList: List<TicketPriceResponse>?,
        val reviewsList: List<FeedbackResponse>?,
        val eventTimeList: List<EventTimeResponse>?
)
