package ru.gidevent.RestAPI.model.request

import jakarta.persistence.*



data class CityRequest(
        val cityId: Long,
        val name: String
)
