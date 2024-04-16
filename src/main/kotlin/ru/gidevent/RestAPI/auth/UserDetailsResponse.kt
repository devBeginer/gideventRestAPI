package ru.gidevent.RestAPI.auth


data class UserDetailsResponse(
    val id: Long,
    val photo: String,
    val firstName: String,
    val lastName: String,
    val bookingsCount: Int,
    val todayBookingsCount: Int,
    val advertsCount: Int,
    val ordersCount: Int,
    val roles: Set<Role>
)