package ru.gidevent.RestAPI.model.request



data class AdvertisementRequest(
        val id: Long,
        val name: String,
        val duration: Int,
        val description: String,
        val transportation: Long,
        val ageRestrictions: Int,
        val visitorsCount: Int,
        val isIndividual: Boolean,
        val photos: String,
        val rating: Int,
        val category: Long,
        val city: Long
)
