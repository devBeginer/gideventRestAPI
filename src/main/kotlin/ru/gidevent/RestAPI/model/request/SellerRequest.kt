package ru.gidevent.RestAPI.model.request

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.Role


data class SellerRequest(
        val sellerId: Long,
        val login: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val photo: String,
        val about: String
)
