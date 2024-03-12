package ru.gidevent.RestAPI.model.dto

import ru.gidevent.RestAPI.model.db.City
import java.util.*


data class SearchOptionsDto(
        var priceFrom: Float?,
        var priceTo: Float?,
        var dateFrom: Calendar?,
        var dateTo: Calendar?,
        val categories: MutableList<Long>?,
        var isIndividual: Boolean?,
        var isGroup: Boolean?,
        var durationFrom: Float?,
        var durationTo: Float?,
        val transport: MutableList<Long>?,
        var ageRestriction: Int?,
        var city: Long?,
        var ratingFrom: Float?,
        var ratingTo: Float?
)
