package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Favourite
import ru.gidevent.RestAPI.model.db.FavouriteId
import ru.gidevent.RestAPI.model.db.Feedback
import ru.gidevent.RestAPI.model.db.FeedbackId
import ru.gidevent.RestAPI.model.dto.TicketPriceDto

interface FavouriteRepository: CrudRepository<Favourite, FavouriteId> {



    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "INNER JOIN FETCH Favourite f on f.favouriteId.advertisementId = a AND f.favouriteId.userId.id = :id " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "WHERE a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtra(id: Long): Iterable<TicketPriceDto>

}