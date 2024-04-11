package ru.gidevent.RestAPI.model.response

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.CustomerCategory

data class VisitorsGroupResponse(
        val id: Long,
        val customerCategoryId: Long,
        val name: String,
        val count: Int
)