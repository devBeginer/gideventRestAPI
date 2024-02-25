package ru.gidevent.RestAPI.model.request

import jakarta.persistence.*
import java.io.Serializable



data class CustomerCategoryRequest (
        val customerCategoryId: Long,
        val advertisementId: Long,
        val name: String
)

