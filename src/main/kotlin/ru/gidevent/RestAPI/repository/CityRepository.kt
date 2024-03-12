package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.City
import ru.gidevent.RestAPI.model.dto.AdvertSuggestion
import ru.gidevent.RestAPI.model.dto.CitySuggestion

interface CityRepository: CrudRepository<City, Long> {

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.CitySuggestion(c.id, c.name) " +
                    "FROM City c WHERE c.name LIKE '%' || :query || '%' " +
                    "ORDER BY c.name ASC " +
                    "LIMIT 15"
    )
    fun getCitySuggestion(query: String): Iterable<CitySuggestion>
}