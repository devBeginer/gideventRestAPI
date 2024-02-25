package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.dto.AdvertisementWithPrice
import ru.gidevent.RestAPI.model.response.AdvertisementMainInfo


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

    @Query(
            "SELECT * FROM ADVERTISEMENT " +
                    "INNER JOIN TICKET_PRICE ON ADVERTISEMENT.advertisement_id = TICKET_PRICE.advertisement_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.transportation_id = TRANSPORT.transportation_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.category_id = CATEGORY.category_id " +
                    //"INNER JOIN TRANSPORT ON ADVERTISEMENT.city_id = CITY.city_id " +
                    "ORDER BY ADVERTISEMENT.rating DESC", nativeQuery = true
    )
    fun getAdvertWithExtra(): Iterable<AdvertisementWithPrice>


}