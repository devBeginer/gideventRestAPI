package ru.gidevent.RestAPI.model.response

import ru.gidevent.RestAPI.model.db.Category


data class TopsResponse(
        val general: List<AdvertisementMainInfo>,
        val categories: List<Category>,
        val top: List<AdvertisementMainInfo>
)
