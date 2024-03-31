package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Booking
import ru.gidevent.RestAPI.model.db.EventTime
import ru.gidevent.RestAPI.model.dto.TicketPriceDto

interface BookingRepository: CrudRepository<Booking, Long> {
    fun findByAdvertisement(advertisement: Advertisement): Iterable<Booking>

}