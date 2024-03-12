package ru.gidevent.RestAPI.model.request

import ru.gidevent.RestAPI.model.db.City
import java.util.*


data class SearchOptions(
        var priceFrom: Float?,
        var priceTo: Float?,
        var dateFrom: Long?,
        var dateTo: Long?,
        val categories: List<Long>?,
        var isIndividual: Boolean,
        var isGroup: Boolean,
        var durationFrom: Float?,
        var durationTo: Float?,
        val transport: List<Long>?,
        var ageRestriction: Int?,
        var city: Long?,
        var ratingFrom: Float?,
        var ratingTo: Float?
)
