package ru.gidevent.RestAPI.model.request

data class EventTimeRequest(
        val timeId: Long,
        val advertisement: Long,
        val time: String, //возможно нужно date
        val isRepeatable: Boolean,
        val daysOfWeek: String,
        val date: String
) {
}