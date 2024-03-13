package ru.gidevent.RestAPI.model.response

data class FeedbackResponse(
        val userName: String,
        val avatarUrl: String,
        val rating: Int,
        val text: String
)