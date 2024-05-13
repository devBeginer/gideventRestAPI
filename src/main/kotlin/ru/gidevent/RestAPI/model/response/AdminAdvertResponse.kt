package ru.gidevent.RestAPI.model.response

data class AdminAdvertResponse(
        val id: Long,
        val advertisement: String,
        val totalPrice: Int,
        val customerCount: Int,
        val place: String,
        val status: String
)
