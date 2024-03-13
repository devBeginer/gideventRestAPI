package ru.gidevent.RestAPI.model.response

import java.util.*


data class EventTimeResponse(
        val timeId: Long,
        val time: Long,
        val isRepeatable: Boolean,
        val daysOfWeek: String,
        val startDate: Long,
        val endDate: Long
)