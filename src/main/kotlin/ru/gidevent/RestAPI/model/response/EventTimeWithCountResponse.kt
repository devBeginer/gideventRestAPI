package ru.gidevent.RestAPI.model.response

import java.util.*


data class EventTimeWithCountResponse(
        val timeId: Long,
        val time: Long,
        val isRepeatable: Boolean,
        val daysOfWeek: String,
        val startDate: Long,
        val endDate: Long,
        val count: Int
)