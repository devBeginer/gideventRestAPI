package ru.gidevent.RestAPI.model.request

data class FeedbackRequest(
        val userId: Long,
        val advertisementId: Long,
        val rating: Int,
        val text: String
)
