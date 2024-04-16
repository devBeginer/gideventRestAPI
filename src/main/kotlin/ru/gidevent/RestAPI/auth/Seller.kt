package ru.gidevent.RestAPI.auth

data class SellerInfo(
    val id: Long,
    val photo: String,
    val firstName: String,
    val lastName: String,
    val about: String,
    val advertsCount: Int,
    val feedbackCount: Int,
    val averageRating: Float
)
