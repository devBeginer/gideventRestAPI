package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.model.dto.AdvertSuggestion
import ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite
import ru.gidevent.RestAPI.model.dto.AdvertisementWithPrice
import ru.gidevent.RestAPI.model.dto.TicketPriceDto
import ru.gidevent.RestAPI.model.response.AdvertisementMainInfo
import java.util.*


interface AdvertisementRepository : CrudRepository<Advertisement, Long> {
    /*fun save(var1: S?): S?
    fun saveAll(var1: Iterable<S?>?): Iterable<S?>?
    override fun findById(var1: ID?): Optional<T?>?
    override fun existsById(var1: ID?): Boolean
    override fun findAll(): MutableIterable<Advertisement>
    fun findAllById(var1: Iterable<ID?>?): Iterable<T?>?
    override fun count(): Long
    override fun deleteById(var1: ID?)
    override fun delete(var1: T?)
    fun deleteAll(var1: Iterable<T?>?)
    override fun deleteAll()*/


    // искать по полям firstName And LastName
    //fun findByFirstNameAndLastName(firstName: String?, lastName: String?): Optional<Advertisement?>?

    // найти первые 5 по FirstName начинающихся с символов и сортировать по FirstName
    //fun findFirst5ByFirstNameStartsWithOrderByFirstName(firstNameStartsWith: String?): List<Employees?>?

    fun findByStatus(status: String): List<Advertisement>
    fun findBySeller(seller: Seller): List<Advertisement>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.AdvertisementWithPrice(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, tp) " +
                    "FROM Advertisement a " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    //"INNER JOIN FETCH TicketPrice ON Advertisement.advertisement_id = TicketPrice.advertisement_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.transportation_id = TRANSPORT.transportation_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.category_id = CATEGORY.category_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.city_id = CITY.city_id " +
                    "ORDER BY a.rating DESC"/*, nativeQuery = false*/
    )
    fun getAdvertWithExtra1(): Iterable<AdvertisementWithPrice>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a " + /*WHERE f.favouriteId.userId = :id*/
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "WHERE a.status = 'ACCEPTED' " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.transportation_id = TRANSPORT.transportation_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.category_id = CATEGORY.category_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.city_id = CITY.city_id " +
                    "ORDER BY a.rating DESC"/*, nativeQuery = false*/
    )
    fun getAdvertWithExtra(): Iterable<TicketPriceDto>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a AND f.favouriteId.userId.id = :id " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "WHERE a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtra(id: Long): Iterable<TicketPriceDto>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a AND f.favouriteId.userId.id = :id " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "WHERE a.id = :advertId " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtraById(id: Long, advertId: Long): Iterable<TicketPriceDto>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a AND f.favouriteId.userId.id = :id " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "WHERE a.name LIKE '%' || :query || '%' AND a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtraByName(id: Long, query: String): Iterable<TicketPriceDto>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "WHERE a.name LIKE '%' || :query || '%' AND a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtraByName(query: String): Iterable<TicketPriceDto>

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.AdvertSuggestion(a.id, a.name, a.city.name) " +
                    "FROM Advertisement a WHERE a.name LIKE '%' || :query || '%' AND a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC " +
                    "LIMIT 15"
    )
    fun getAdvertSuggestion(query: String): Iterable<AdvertSuggestion>

    @Query(
            "SELECT DISTINCT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "AND (:priceFrom is null OR tp.price >= :priceFrom) " +
                    "AND (:priceTo is null OR tp.price <= :priceTo) " +
                    "INNER JOIN FETCH EventTime e on a = e.advertisement " +
                    "AND ((:dateStart is null AND :dateEnd is null) " +
                    "OR ((:monday is not null AND e.daysOfWeek LIKE '%' || :monday || '%') " +
                    "OR (:tuesday is not null AND e.daysOfWeek LIKE '%' || :tuesday || '%') " +
                    "OR (:wednesday is not null AND e.daysOfWeek LIKE '%' || :wednesday || '%') " +
                    "OR (:thursday is not null AND e.daysOfWeek LIKE '%' || :thursday || '%') " +
                    "OR (:friday is not null AND e.daysOfWeek LIKE '%' || :friday || '%') " +
                    "OR (:saturday is not null AND e.daysOfWeek LIKE '%' || :saturday || '%') " +
                    "OR (:sunday is not null AND e.daysOfWeek LIKE '%' || :sunday || '%')) " +
                    "AND (:dateStart<=e.endDate AND :dateEnd>=e.startDate)) " +
                    "WHERE (:ageRestriction is null OR a.ageRestrictions <= :ageRestriction) " +
                    "AND (:isIndividual is null OR a.isIndividual = :isIndividual) " +
                    "AND (:ratingFrom is null OR a.rating >= :ratingFrom) " +
                    "AND (:ratingTo is null OR a.rating <= :ratingTo) " +
                    "AND (:durationFrom is null OR a.duration >= :durationFrom) " +
                    "AND (:durationTo is null OR a.duration <= :durationTo) " +
                    "AND (:transport is null OR a.transportation.transportationId in :transport) " +
                    "AND (:categories is null OR a.category.categoryId in :categories) " +
                    "AND (:city is null OR a.city.cityId = :city) " +
                    "AND a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtraByParams(
            monday: String?,
            tuesday: String?,
            wednesday: String?,
            thursday: String?,
            friday: String?,
            saturday: String?,
            sunday: String?,
            dateStart: Calendar?,
            dateEnd: Calendar?,
            ageRestriction: Int?,
            isIndividual: Boolean?,
            ratingFrom: Float?,
            ratingTo: Float?,
            durationFrom: Float?,
            durationTo: Float?,
            transport: List<Long>?,
            categories: List<Long>?,
            city: Long?,
            priceFrom: Float?,
            priceTo: Float?
    ): Iterable<TicketPriceDto>
    @Query(
            "SELECT DISTINCT new ru.gidevent.RestAPI.model.dto.TicketPriceDto(" +
                    "tp.id, " +
                    "new ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite(a.id, a.name, a.duration, a.description, a.transportation, a.ageRestrictions, a.visitorsCount, a.isIndividual, a.photos, a.rating, a.category, a.city, a.seller, f), " +
                    "tp.customerCategory, " +
                    "tp.price) " +
                    "FROM Advertisement a " +
                    "LEFT JOIN FETCH Favourite f on f.favouriteId.advertisementId = a AND f.favouriteId.userId.id = :id " +
                    "INNER JOIN FETCH TicketPrice tp on tp.advertisement = a " +
                    "AND (:priceFrom is null OR tp.price >= :priceFrom) " +
                    "AND (:priceTo is null OR tp.price <= :priceTo) " +
                    "INNER JOIN FETCH EventTime e on a = e.advertisement " +
                    "AND ((:dateStart is null AND :dateEnd is null) " +
                    "OR ((:monday is not null AND e.daysOfWeek LIKE '%' || :monday || '%') " +
                    "OR (:tuesday is not null AND e.daysOfWeek LIKE '%' || :tuesday || '%') " +
                    "OR (:wednesday is not null AND e.daysOfWeek LIKE '%' || :wednesday || '%') " +
                    "OR (:thursday is not null AND e.daysOfWeek LIKE '%' || :thursday || '%') " +
                    "OR (:friday is not null AND e.daysOfWeek LIKE '%' || :friday || '%') " +
                    "OR (:saturday is not null AND e.daysOfWeek LIKE '%' || :saturday || '%') " +
                    "OR (:sunday is not null AND e.daysOfWeek LIKE '%' || :sunday || '%')) " +
                    "AND (:dateStart<=e.endDate AND :dateEnd>=e.startDate)) " +
                    "WHERE (:ageRestriction is null OR a.ageRestrictions <= :ageRestriction) " +
                    "AND (:isIndividual is null OR a.isIndividual = :isIndividual) " +
                    "AND (:ratingFrom is null OR a.rating >= :ratingFrom) " +
                    "AND (:ratingTo is null OR a.rating <= :ratingTo) " +
                    "AND (:durationFrom is null OR a.duration >= :durationFrom) " +
                    "AND (:durationTo is null OR a.duration <= :durationTo) " +
                    "AND (:transport is null OR a.transportation.transportationId in :transport) " +
                    "AND (:categories is null OR a.category.categoryId in :categories) " +
                    "AND (:city is null OR a.city.cityId = :city) " +
                    "AND a.status = 'ACCEPTED' " +
                    "ORDER BY a.rating DESC"
    )
    fun getAdvertWithExtraByParams(
            id: Long,
            monday: String?,
            tuesday: String?,
            wednesday: String?,
            thursday: String?,
            friday: String?,
            saturday: String?,
            sunday: String?,
            dateStart: Calendar?,
            dateEnd: Calendar?,
            ageRestriction: Int?,
            isIndividual: Boolean?,
            ratingFrom: Float?,
            ratingTo: Float?,
            durationFrom: Float?,
            durationTo: Float?,
            transport: List<Long>?,
            categories: List<Long>?,
            city: Long?,
            priceFrom: Float?,
            priceTo: Float?
    ): Iterable<TicketPriceDto>

}