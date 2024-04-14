package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Booking
import ru.gidevent.RestAPI.model.db.EventTime
import ru.gidevent.RestAPI.model.db.VisitorsGroup
import ru.gidevent.RestAPI.model.dto.TicketPriceDto
import java.util.*

interface BookingRepository: CrudRepository<Booking, Long> {
    fun findByAdvertisement(advertisement: Advertisement): Iterable<Booking>

    @Query(
            "SELECT b FROM Booking b " +
                    "WHERE b.advertisement.seller.sellerId = :id"
    )
    fun getBySeller(id: Long): Iterable<Booking>

    @Query(
            "SELECT b FROM Booking b " +
                    "WHERE b.user.id = :id"
    )
    fun getByCustomer(id: Long): Iterable<Booking>

    @Query(
            "SELECT b FROM Booking b " +
                    "WHERE b.advertisement.seller.user.id = :id " +
                    "AND (:advertId is null OR b.advertisement.id = :advertId) " +
                    "AND (:date is null OR b.date = :date)"
    )
    fun getBySellerAndAdvert(id: Long, advertId: Long?, date: Calendar?): Iterable<Booking>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a " +
                    "INNER JOIN FETCH Booking b on b.advertisement = a AND b.user.id = :id " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtra(id: Long): Iterable<TicketPriceDto>
}