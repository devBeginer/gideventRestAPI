package ru.gidevent.RestAPI.model.request

import jakarta.persistence.*



data class CategoryRequest(
        val categoryId: Long,
        val name: String
)
