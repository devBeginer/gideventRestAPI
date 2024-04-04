package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Booking
import ru.gidevent.RestAPI.model.db.VisitorsGroup
import java.util.Calendar

interface GroupRepository: CrudRepository<VisitorsGroup, Long> {
    fun findByBooking(booking: Booking): Iterable<VisitorsGroup>

    @Query(
            "SELECT g FROM VisitorsGroup g " +
                    "WHERE g.booking.bookingTime = :date " +
                    "AND g.booking.advertisement = :advertisement"//, nativeQuery = true
    )
    fun getByAdvertAndDate(advertisement: Advertisement, date: Calendar): Iterable<VisitorsGroup>
}