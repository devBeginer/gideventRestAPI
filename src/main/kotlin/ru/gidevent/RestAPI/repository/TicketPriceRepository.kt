package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.gidevent.RestAPI.model.db.TicketPrice

interface TicketPriceRepository: CrudRepository<TicketPrice, Long> {
    @Query(
            "SELECT * FROM TICKET_PRICE WHERE TICKET_PRICE.advertisement_id = :advertId", nativeQuery = true
    )
    fun getTicketPriceByAdvert(@Param("advertId") advertId: Long): Iterable<TicketPrice>
}