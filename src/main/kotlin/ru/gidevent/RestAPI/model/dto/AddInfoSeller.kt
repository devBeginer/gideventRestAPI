package ru.gidevent.RestAPI.model.dto

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.Role


data class AddInfoSeller(
        val sellerId: Long,
        val firstName: String,
        val lastName: String,
        val photo: String
)

