package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Booking
import ru.gidevent.RestAPI.model.db.CustomerCategory
import ru.gidevent.RestAPI.model.db.Group
import ru.gidevent.RestAPI.model.dto.TicketPriceDto
import java.util.Calendar

interface GroupRepository: CrudRepository<Group, Long> {
    fun findByBooking(booking: Booking): Iterable<Group>

    @Query(
            "SELECT * FROM Group g " +
                    "WHERE g.booking.bookingTime = :date " +
                    "AND g.booking.advertisement = :advertisement"
    )
    fun getByAdvetAndDate(advertisement: Advertisement, date: Calendar): Iterable<Group>
}