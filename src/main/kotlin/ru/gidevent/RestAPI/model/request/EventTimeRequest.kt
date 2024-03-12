package ru.gidevent.RestAPI.model.request

data class EventTimeRequest(
        val timeId: Long,
        val advertisement: Long,
        val time: Long, //возможно нужно date
        val isRepeatable: Boolean,
        val daysOfWeek: String,
        val startDate: Long,
        val endDate: Long
) {
}