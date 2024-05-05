package ru.gidevent.RestAPI.auth.userResponse

data class VkUser(
        val id: Long,
        val first_name: String,
        val last_name: String,
        val deactivated: String?,
        val photo_100: String,
        val verified: Int,
)
