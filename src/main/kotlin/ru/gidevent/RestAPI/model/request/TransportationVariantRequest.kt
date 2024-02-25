package ru.gidevent.RestAPI.model.request

import jakarta.persistence.*



data class TransportationVariantRequest(
        val transportationId: Long,
        val name: String
)
