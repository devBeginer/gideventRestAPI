package ru.gidevent.RestAPI.auth

data class VkUser(
        val id: String,
        val first_name: String,
        val last_name: String,
        val deactivated: String,
        val photo_100: String,
        val verified: Int,
        val phone: String,
        val email: String
)
