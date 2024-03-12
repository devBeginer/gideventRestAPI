package ru.gidevent.RestAPI.model.response

import ru.gidevent.RestAPI.model.db.Category
import ru.gidevent.RestAPI.model.db.TransportationVariant

data class OptionsVariants(
        val transport: List<TransportationVariant>,
        val category: List<Category>
)
