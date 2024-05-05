package ru.gidevent.RestAPI.auth.accessTokenResponse

data class VkAccessToken(
    val access_token: String,
    val access_token_id: String,
    val email: String?,
    val is_service: Boolean,
    val phone: String?,
    val phone_validated: Int,
    val source: Int,
    val source_description: String,
    val user_id: Long,
    val expires_in: Long
)