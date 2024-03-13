package ru.gidevent.RestAPI.repository

import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.EventTime
import ru.gidevent.RestAPI.model.db.Favourite

interface EventTimeRepository: CrudRepository<EventTime, Long> {
    fun findByAdvertisement(advertisement: Advertisement): Iterable<EventTime>
}